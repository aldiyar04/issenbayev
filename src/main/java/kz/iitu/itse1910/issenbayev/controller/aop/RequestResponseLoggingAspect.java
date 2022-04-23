package kz.iitu.itse1910.issenbayev.controller.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kz.iitu.itse1910.issenbayev.controller.aop.util.AuthenticationHolder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Parameter;
import java.util.Optional;
import java.util.stream.IntStream;

@Aspect
@Component
@Slf4j
@AllArgsConstructor
public class RequestResponseLoggingAspect {
    private final AuthenticationHolder authenticationHolder;
    private final HttpServletRequest request;
    private final ObjectMapper objectMapper;

    @Pointcut("within(kz.iitu.itse1910.issenbayev.controller.*) && bean(*Controller)")
    public void isController() {}

    @Pointcut("within(kz.iitu.itse1910.issenbayev.controller.aop.*) && bean(exceptionHandlerDelegate)")
    public void isExceptionHandlerDelegate() {}

    @Before("isController()")
    public void logRequest(JoinPoint joinPoint) {
        String requestDetails = getRequestDetails();
        Optional<Object> requestBodyOptional = getRequestBodyOptional(joinPoint);
        if (requestBodyOptional.isPresent()) {
            requestDetails += "; request body: " + toJson(requestBodyOptional.get());
        }
        log.info("REQUEST: " + requestDetails);
    }

    @AfterReturning(value = "isController()", returning = "responseEntity")
    public void logNonErrorResponse(JoinPoint joinPoint, ResponseEntity<?> responseEntity) {
        String details = getRequestDetails();
        if (responseEntity != null) {
            details += getResponseDetails(responseEntity);
        }
        log.info("RESPONSE: " + details);
    }

    @AfterReturning(value = "isExceptionHandlerDelegate())", returning = "responseEntity")
    public void logErrorResponse(JoinPoint joinPoint, ResponseEntity<Object> responseEntity) {
        String details = getRequestDetails();
        details += getResponseDetails(responseEntity);
        log.info("RESPONSE: " + details);
    }

    private String getRequestDetails() {
        String method = request.getMethod();
        String url = getRequestUrl();
        String clientIP = getClientIP();
        String usernameString = getUsername().map(uname -> "username: " + uname).orElse("");
        return method + " " + url + "; client IP: " + clientIP + "; " + usernameString;
    }

    private String getRequestUrl() {
        String queryString = request.getQueryString() == null ? "" : "?" + request.getQueryString();
        return request.getRequestURL().toString() + queryString;
    }

    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null){
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    private Optional<String> getUsername() {
        String username = authenticationHolder.getAuthentication().getName();
        return Optional.ofNullable(username);
    }

    private Optional<Object> getRequestBodyOptional(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Parameter[] params  = ((MethodSignature) joinPoint.getSignature()).getMethod().getParameters();
        return IntStream.range(0, params.length)
                .filter(i -> params[i].isAnnotationPresent(RequestBody.class))
                .mapToObj(i -> args[i])
                .findFirst();
    }

    private String getResponseDetails(ResponseEntity<?> responseEntity) {
        String httpStatus = responseEntity.getStatusCode().toString();
        return "; status: " + httpStatus + "; response body: " + toJson(responseEntity.getBody());
    }

    private String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not convert object to JSON string");
        }
    }
}
