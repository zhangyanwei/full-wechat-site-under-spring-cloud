package com.askdog.service.impl.event;

import com.askdog.model.common.EventType;
import com.askdog.service.MessageService;
import com.askdog.service.impl.event.TriggerEvent.TriggerEventItem;
import com.google.common.collect.Maps;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;

import static com.askdog.common.utils.SpelUtils.parse;
import static java.util.Arrays.stream;
import static org.springframework.util.StringUtils.isEmpty;

@Aspect
@Configuration
public class TriggerEventProcessor {

    @Autowired private MessageService messageService;

    @Around(value = "@annotation(triggerEvent)")
    public Object around(ProceedingJoinPoint pjp, TriggerEvent triggerEvent) throws Throwable {
        Object returnValue = pjp.proceed();
        parseTriggerEvents(createEvaluationContext(pjp, returnValue), triggerEvent);
        return returnValue;
    }

    private void parseTriggerEvents(StandardEvaluationContext evaluationContext, TriggerEvent triggerEvent) {
        stream(triggerEvent.value())
                .filter(triggerEventItem -> parse(triggerEventItem.condition(), Boolean.class, evaluationContext))
                .forEach(triggerEventItem -> parseTriggerEventItem(triggerEventItem, evaluationContext));
    }

    private void parseTriggerEventItem(TriggerEventItem triggerEventItem, StandardEvaluationContext evaluationContext) {
        EventType eventType = triggerEventItem.eventType();
        Long performerId = isEmpty(triggerEventItem.performer()) ? null : parse(triggerEventItem.performer(), Long.class, evaluationContext);
        Long targetId = isEmpty(triggerEventItem.target()) ? null : parse(triggerEventItem.target(), Long.class, evaluationContext);
        messageService.sendEventMessage(performerId, eventType, targetId);
    }

    private StandardEvaluationContext createEvaluationContext(ProceedingJoinPoint pjp, Object returnValue) {
        Map<String, Object> params = prepareParams(pjp);
        params.put("returnValue", returnValue);
        StandardEvaluationContext simpleContext = new StandardEvaluationContext(params);
        simpleContext.addPropertyAccessor(new MapAccessor());
        return simpleContext;
    }

    private Map<String, Object> prepareParams(JoinPoint pjp) {
        Map<String, Object> params = Maps.newHashMap();
        String[] parameterNames = ((MethodSignature) pjp.getSignature()).getParameterNames();
        for (int i = 0; i < parameterNames.length; i++) {
            params.put(parameterNames[i], pjp.getArgs()[i]);
        }
        return params;
    }
}