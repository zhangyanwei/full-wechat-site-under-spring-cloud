package com.askdog.messaging.redis;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

import static com.google.common.base.Preconditions.checkState;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@Component
public class MessageListenerAutoRegistry implements BeanPostProcessor {

    @Autowired
    @Qualifier("message.listeners")
    private RedisMessageListenerContainer redisMessageListenerContainer;

    @Autowired
    @Qualifier("message.listeners")
    private RedisTemplate redisTemplate;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        MessageListener messageListener = bean.getClass().getAnnotation(MessageListener.class);
        if (messageListener != null) {
            String listenerMethod = messageListener.listenerMethod();
            MessageListenerAdapter adapter = new MessageListenerAdapter(bean, listenerMethod);
            adapter.setSerializer(redisTemplate.getValueSerializer());
            adapter.afterPropertiesSet();

            // check topics
            String[] topics = messageListener.value();
            if (topics.length == 0) {
                topics = messageListener.topic();
            }
            String[] patterns = messageListener.pattern();
            checkState(topics.length > 0 || patterns.length > 0, "at least one topic is required");

            // register topics
            if (topics.length > 0) {
                redisMessageListenerContainer.addMessageListener(adapter, stream(topics).map(ChannelTopic::new).collect(toList()));
            }

            if (patterns.length > 0) {
                redisMessageListenerContainer.addMessageListener(adapter, stream(patterns).map(PatternTopic::new).collect(toList()));
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
