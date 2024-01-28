package com.amit.spotify.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Arrays;

@Slf4j
@Component
@Aspect
public class LoggingAspect {


    @Around("execution(* com.amit.spotify..*(..))")
    public Object logAllMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        CodeSignature signature = (CodeSignature) joinPoint.getSignature();

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Object result = joinPoint.proceed();

        stopWatch.stop();


        log.info("Entering {}.{}() with ParameterName[s]= {} and argumentValue[s]= {} in {} ms",
                signature.getDeclaringTypeName(), signature.getName(),
                Arrays.toString(signature.getParameterNames()), Arrays.toString(joinPoint.getArgs()),
                stopWatch.getTotalTimeMillis());


        return result;
    }


    @Pointcut("within(@org.springframework.stereotype.Repository *)" +
            " || within(@org.springframework.stereotype.Service *)" +
            " || within(@org.springframework.stereotype.Controller *)")
    public void springBeanPointcut() {
        //method is empty as this is just pointcut, implementations are in advices
    }


    @Pointcut("within(com.amit.spotify.dao..*) || within(com.amit.spotify.service..*) || within(com.amit.spotify.controller..*)")
    public void applicationPointcut() {
        //method is empty as this is just pointcut, implementations are in advices
    }


    @AfterThrowing(pointcut = "applicationPointcut() && springBeanPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        CodeSignature signature = (CodeSignature) joinPoint.getSignature();

        String exception = null != e.getCause() ? e.getMessage() + " -> " + e.getCause() : null;

        log.error("Exception occurred in {}.{}() with ParameterName[s]= {} and argumentValue[s]= {}",
                signature.getDeclaringTypeName(), signature.getName(),
                Arrays.toString(signature.getParameterNames()), Arrays.toString(joinPoint.getArgs()));
    }


}