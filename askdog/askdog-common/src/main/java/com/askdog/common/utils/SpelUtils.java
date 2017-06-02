package com.askdog.common.utils;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class SpelUtils {

    public static <T> T parse(String expressionStr, Class<T> expectedResultType, EvaluationContext evaluationContext) {
        Expression expression = new SpelExpressionParser().parseExpression(expressionStr);
        return expression.getValue(evaluationContext, expectedResultType);
    }

    public static Object parse(String expressionStr, EvaluationContext evaluationContext) {
        Expression expression = new SpelExpressionParser().parseExpression(expressionStr);
        return expression.getValue(evaluationContext);
    }

    public static <T> T parse(String expressionStr, Class<T> expectedResultType) {
        return parse(expressionStr, expectedResultType, new StandardEvaluationContext());
    }

    public static String parse(String expressionStr) {
        return parse(expressionStr, String.class);
    }

}
