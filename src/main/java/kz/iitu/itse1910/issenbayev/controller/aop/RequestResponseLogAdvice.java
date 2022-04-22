package kz.iitu.itse1910.issenbayev.controller.aop;

import kz.iitu.itse1910.issenbayev.controller.aop.util.AuthenticationHolder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Parameter;
import java.util.Optional;

@Aspect
@Component
@Slf4j
@AllArgsConstructor
public class RequestResponseLogAdvice {
    private final AuthenticationHolder authenticationHolder;
    private final HttpServletRequest request;

    @Pointcut("within(kz.iitu.itse1910.issenbayev.controller.*) && bean(*Controller)")
    public void isController() {}

    @Before("isController()")
    public void logRequest(JoinPoint joinPoint) {
        String requestDetails = getRequestDetails();
        Optional<Object> requestBodyOptional = getRequestBody(joinPoint);
        if (requestBodyOptional.isPresent()) {
            String requestBodyString = requestBodyToString(requestBodyOptional.get());
            requestDetails += "; body: " + requestBodyString;
        }
        log.info(requestDetails);
    }

    private Optional<Object> getRequestBody(JoinPoint joinPoint) {
        Object requestBody = null;
        Parameter[] params = ((MethodSignature) joinPoint.getSignature()).getMethod().getParameters();
        for (int i = 0; i < params.length; i++) {
            if (params[i].isAnnotationPresent(RequestBody.class)) {
                requestBody = joinPoint.getArgs()[i];
                break;
            }
        }
        return Optional.ofNullable(requestBody);
    }

    private String getRequestDetails() {
        String method = request.getMethod();
        String url = getRequestUrl();
        String clientIP = getClientIP();
        String usernameString = getUsername().map(uo -> "username: " + uo).orElse("");

        return method + " " + url + "; client IP: " + clientIP + "; " + usernameString;
    }

    private String requestBodyToString(Object body) {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        return bodyToString(requestWrapper.getContentAsByteArray(), request.getCharacterEncoding());
    }

    private String bodyToString(byte[] contentAsByteArray, String characterEncoding) {
        try {
            return new String(contentAsByteArray, 0, contentAsByteArray.length, characterEncoding);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Encoding " + characterEncoding + " not supported");
        }
    }

    private String getRequestUrl() {
        String queryString = request.getQueryString() == null ? "" : "?" + request.getQueryString();
        return request.getRequestURL().toString() + queryString;
    }

    private Optional<String> getUsername() {
        String username = authenticationHolder.getAuthentication().getName();
        return Optional.ofNullable(username);
    }

    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null){
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
