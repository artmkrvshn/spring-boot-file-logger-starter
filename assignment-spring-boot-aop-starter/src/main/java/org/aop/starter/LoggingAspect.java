package org.aop.starter;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

@Aspect
@Component
public class LoggingAspect {

    private final List<Class<?>> ignoredArgumentTypes = List.of(byte[].class, char[].class);

    @Before("execution(public * *(..)) && @within(service)")
    public void logMethodCall(JoinPoint joinPoint, Service service) {
        Class<?> targetClass = joinPoint.getTarget().getClass();
        String methodName = joinPoint.getSignature().getName();

        Logger logger = LoggerFactory.getLogger(targetClass);

        Object[] args = joinPoint.getArgs();
        StringJoiner argsStringJoiner = new StringJoiner(", ");

        for (Object arg : args) {
            if (arg == null) {
                argsStringJoiner.add("null");
            } else if (ignoredArgumentTypes.stream().anyMatch(type -> type.isInstance(arg))) {
                argsStringJoiner.add("ignored");
            } else {
                argsStringJoiner.add(limitString(arg.toString(), 100));
            }
        }

        logger.info("{} args: [{}]", methodName, argsStringJoiner);
    }

    private String limitString(String str, int limit) {
        if (str.length() > limit) {
            return str.substring(0, limit) + "...";
        }
        return str;
    }

}
