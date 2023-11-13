package ru.template.example.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@Aspect
public class LoggingAspect {

    private final Logger logger = Logger.getLogger(LoggingAspect.class.getName());

    /**
     * Перехват всех методов из DocumentController
     */
    @Pointcut("within(ru.template.example.documents.controller.DocumentController)")
    public void pointcut() {}

    /**
     * Что нужно сделать когда произошел перехват
     * @param joinPoint
     */
    @Before("pointcut()")
    public void logInfoMethodCallMethodArgs(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        logger.log(Level.INFO, "Название метода: " + methodName + ", аргументы: " + List.of(args));
    }

    @AfterReturning(pointcut = "pointcut()", returning = "result")
    public void logAfterReeturning(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        logger.log(Level.INFO, "Метод " + methodName + " вернул значение: " + result);
    }
}
