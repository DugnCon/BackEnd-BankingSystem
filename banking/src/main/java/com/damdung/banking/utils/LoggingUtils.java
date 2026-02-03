package com.damdung.banking.utils;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingUtils {
    @Pointcut("@within(com.damdung.banking.annotation.LoggingAnnotation)")
    public void logingExecutionMethods(){}

    /*@Before("logingExecutionMethods()")
    public void before(JoinPoint jp) {
        System.out.println("System before " + jp.getSignature().getName());
    }

    @After("logingExecutionMethods()")
    public void after(JoinPoint jp) {
        System.out.println("System after " + jp.getSignature().getName());
    }

    @AfterReturning(value = "logingExecutionMethods()", returning = "result")
    public void afterReturning(JoinPoint jp, Object rs) {
        System.out.println("System after returning " + rs);
    }

    @AfterThrowing(value = "logingExecutionMethods()", throwing = "ex")
    public  void afterThrowing(JoinPoint jp, Exception ex) {
        System.out.println("Exception system " + ex.getMessage());
    }*/

    @Around("logingExecutionMethods()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {

        String methodName = pjp.getSignature().toShortString();
        long start = System.currentTimeMillis();

        try {
            Object result = pjp.proceed();
            long time = System.currentTimeMillis() - start;

            log.info("Succeeded {} | resultType={} | time={}ms",
                    methodName,
                    result != null ? result.getClass().getSimpleName() : "null",
                    time);

            return result;

        } catch (Exception ex) {
            long time = System.currentTimeMillis() - start;

            log.error("Exception in {} | time={}ms | message={}",
                    methodName,
                    time,
                    ex.getMessage(),
                    ex);

            throw ex;
        }
    }
}
