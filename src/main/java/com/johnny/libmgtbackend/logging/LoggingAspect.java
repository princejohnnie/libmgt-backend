package com.johnny.libmgtbackend.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Autowired
    private ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);


    @Pointcut("execution( * com.johnny.libmgtbackend.controller.AuthenticationController*.*(..)) || " +
            "execution( * com.johnny.libmgtbackend.controller.BookController*.*(..)) || " +
            "execution( * com.johnny.libmgtbackend.controller.LibrarianController*.*(..)) || " +
            "execution( * com.johnny.libmgtbackend.controller.PatronController*.*(..))")
    private void controllerMethodsToLog() {
    }


    @Before(value = "controllerMethodsToLog()")
    public void logBefore(JoinPoint joinPoint) throws JsonProcessingException {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        logger.info("Start >> {} {}() called with payload - {}", className, methodName, objectMapper.writeValueAsString(args));
    }

    @AfterReturning(value = "controllerMethodsToLog()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        logger.info("End << {} {}() returned with status - {}", className, methodName, ((ResponseEntity<?>)result).getStatusCode());
    }

    @AfterThrowing(pointcut = "controllerMethodsToLog()", throwing = "exception")
    public void logException(JoinPoint joinPoint, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();
        logger.info("Error << {}() with message: - {}", methodName, exception.getMessage());
    }

}
