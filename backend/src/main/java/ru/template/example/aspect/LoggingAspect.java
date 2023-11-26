package ru.template.example.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Класс для логгирования методов
 */
@Component
@Aspect
@Slf4j
public class LoggingAspect {
    /**
     * Перехват всех методов из DocumentController
     */
    @Pointcut("within(ru.template.example.documents.controller.DocumentController)")
    public void pointcut() {
    }

    /**
     * Перехват всех методов из KafkaProducer
     */
    @Pointcut("within(ru.template.example.kafka.KafkaProducer)")
    public void pointcutKafka() {
    }

    /**
     * Вывод названия методов и переданных аргументов до выполнения метода
     *
     * @param joinPoint точка выполнения метода
     */
    @Before("pointcut()")
    public void logInfoMethodCallMethodArgs(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        log.info("Method name: " + methodName + ", args: " + List.of(args));
    }

    /**
     * Вывод названия метода и возвращаемогоо значения после выполнения метода
     *
     * @param joinPoint точка выполнения метода
     * @param result    возвращаемое значение
     */
    @AfterReturning(pointcut = "pointcut()", returning = "result")
    public void logAfterReeturning(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        log.info("Method " + methodName + " returned value: " + result);
    }

    /**
     * Вывод названия методов и переданных аргументов после выполнения метода
     *
     * @param joinPoint точка выполнения метода
     */
    @After("pointcutKafka()")
    public void logAfterSendingMessage(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        log.info("Method name: " + methodName + ", args: " + List.of(args));
    }
}
