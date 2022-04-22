package kz.iitu.itse1910.issenbayev.controller.aop;

import kz.iitu.itse1910.issenbayev.controller.aop.util.AuthenticationHolder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Optional;

@ControllerAdvice
@Slf4j
@AllArgsConstructor
public class RequestBodyAdviceImpl extends RequestBodyAdviceAdapter {
    private final AuthenticationHolder authenticationHolder;
    private final HttpServletRequest request;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {
        String requestDetails = formatRequestDetails();
        String bodyString = requestBodyToString(body);
        log.info(requestDetails + "; body: " + bodyString);
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
                                  Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        String requestDetails = formatRequestDetails();
        log.info(requestDetails);
        return body;
    }

    private String formatRequestDetails() {
        String url = getRequestUrl();
        String method = request.getMethod();
        String clientIP = getClientIP();
        String usernameString = getUsername().map(uo -> "username: " + uo).orElse("");

        return method + " " + url + "; client IP: " + clientIP + " ," + usernameString;
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
        return request.getRequestURL().toString() + "?" + request.getQueryString();
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
