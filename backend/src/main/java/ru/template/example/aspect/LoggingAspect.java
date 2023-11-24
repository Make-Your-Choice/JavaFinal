package ru.template.example.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@Aspect
@Slf4j
public class LoggingAspect {

    //private final Logger logger = Logger.getLogger(LoggingAspect.class.getName());

    /**
     * Перехват всех методов из DocumentController
     */
    @Pointcut("within(ru.template.example.documents.controller.DocumentController)")
    public void pointcut() {}

    @Pointcut("within(ru.template.example.kafka.KafkaProducer)")
    public void pointcutKafka() {}

    /**
     * Что нужно сделать когда произошел перехват
     * @param joinPoint
     */
    @Before("pointcut()")
    public void logInfoMethodCallMethodArgs(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        log.info("Method name: " + methodName + ", args: " + List.of(args));
        //logger.log(Level.INFO, "Method name: " + methodName + ", args: " + List.of(args));
    }

    @AfterReturning(pointcut = "pointcut()", returning = "result")
    public void logAfterReeturning(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        log.info("Method " + methodName + " returned value: " + result);
        //logger.log(Level.INFO, "Method " + methodName + " returned value: " + result);
    }

    @After("pointcutKafka()")
    public void logAfterSendingMessage(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        log.info("Method name: " + methodName + ", args: " + List.of(args));
        //logger.log(Level.INFO, "Method name: " + methodName + ", args: " + List.of(args));
    }
}
