package com.intelliacademy.orizonroute.librarymanagmentsystem.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* com.intelliacademy.orizonroute.librarymanagmentsystem.service.*.*(..))")
    public void allServiceMethods() {}

    @Around("allServiceMethods()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        log.info("Method {} called with args: {}", joinPoint.getSignature(), joinPoint.getArgs());

        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            log.error("Exception in method {}: {}", joinPoint.getSignature(), e.getMessage());
            throw e;
        }

        long executionTime = System.currentTimeMillis() - start;
        log.info("Method {} executed in {} ms and returned: {}", joinPoint.getSignature(), executionTime, result);

        return result;
    }
}
