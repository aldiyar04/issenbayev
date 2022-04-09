package kz.iitu.itse1910.issenbayev.feature.aop;

import kz.iitu.itse1910.issenbayev.feature.exception.apiexception.ApiException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class LoggingAspect {
    private final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("within(kz.iitu.itse1910.issenbayev.controller.*) && bean(*Controller)")
    public void isController() {}

    @Pointcut("within(kz.iitu.itse1910.issenbayev.service.*) && bean(*Service)")
    public void isService() {}

    @Pointcut("within(kz.iitu.itse1910.issenbayev.repository.*) && bean(*Repository*)")
    public void isRepository() {}

    @Before("isController() || isService() || isRepository()")
    public void logCalling(JoinPoint joinPoint) {
        String classMethod = getClassMethod(joinPoint);
        String methodArgs = getMethodArguments(joinPoint);
        log.debug("Calling {}. Arguments: [{}]", classMethod, methodArgs);
    }

    private String getClassMethod(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        return className + "." + methodName + "()";
    }

    private String getMethodArguments(JoinPoint joinPoint) {
        CodeSignature signature = (CodeSignature) joinPoint.getSignature();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < joinPoint.getArgs().length; i++) {
            String parameterName = signature.getParameterNames()[i];
            builder.append(parameterName);
            builder.append(": ");
            builder.append(joinPoint.getArgs()[i].toString());
            if (i != joinPoint.getArgs().length - 1) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    @AfterReturning(value = "isController() || isService() || isRepository()", returning = "retVal")
    public void logReturnValue(JoinPoint joinPoint, Object retVal) {
        if (isMethodVoid(joinPoint)) {
            return;
        }
        String classMethod = getClassMethod(joinPoint);
        log.debug("Exiting {} with return value: {}", classMethod, retVal);
    }

    @AfterThrowing(value = "isController()", throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {
        if (!(ex instanceof ApiException)) {
            String classMethod = getClassMethod(joinPoint);
            String methodArgs = getMethodArguments(joinPoint);
            log.error("Method: {}; arguments: [{}].\n{}; message: {}", classMethod, methodArgs,
                    ex.getClass().getSimpleName(), ex.getLocalizedMessage());
        }
    }

    private boolean isMethodVoid(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Class retType = signature.getReturnType();
        return retType == Void.TYPE;
    }

    @Around("isController()")
    public Object logExecutionTimeIfSlow(ProceedingJoinPoint pjp) throws Throwable {
        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        Object retVal = pjp.proceed();
        stopWatch.stop();

        long executionTimeMs = stopWatch.getTotalTimeMillis();
        if (isExecutionTimeSlow(executionTimeMs)) {
            String classMethod = getClassMethod(pjp);
            log.warn("SLOW EXECUTION TIME. {} : {} ms", classMethod, executionTimeMs);
        }

        return retVal;
    }

    private boolean isExecutionTimeSlow(long executionTimeMs) {
        final int EXECUTION_TIME_LIMIT_MS = 300;
        return executionTimeMs >= EXECUTION_TIME_LIMIT_MS;
    }
}
