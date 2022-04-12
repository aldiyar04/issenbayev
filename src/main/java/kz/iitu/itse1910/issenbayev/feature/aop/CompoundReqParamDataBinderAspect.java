package kz.iitu.itse1910.issenbayev.feature.aop;

import kz.iitu.itse1910.issenbayev.controller.annotation.CompoundRequestParam;
import kz.iitu.itse1910.issenbayev.dto.RequestParamName;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Aspect
@Component
public class CompoundReqParamDataBinderAspect {
    private final Map<Class<?>, Map<String, String>> MULTICLASS_FIELD_TO_REQ_PARAM_CACHE = new ConcurrentHashMap<>();
    private final ConversionService conversionService;

    @Autowired
    public CompoundReqParamDataBinderAspect(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Pointcut("within(kz.iitu.itse1910.issenbayev.controller.*) && " +
            "@target(org.springframework.web.bind.annotation.RestController) && " +
            "@annotation(kz.iitu.itse1910.issenbayev.controller.annotation.MethodWithCompoundRequestParams)")
    public void isControllerMethodWithCompoundRequestParams() {}

    @Around("isControllerMethodWithCompoundRequestParams()")
    public Object bindRequestParams(ProceedingJoinPoint pjp) throws Throwable {
        Method method  = ((MethodSignature) pjp.getSignature()).getMethod();
        Set<Integer> compoundReqParamIndices = getCompoundRequestParamIndices(method.getParameterAnnotations());
        Map<Class<?>, Object> compoundReqArgs = getCompoundRequestArguments(method.getParameterTypes(), pjp.getArgs(),
                compoundReqParamIndices);
        bindReqArgsToFieldsOfCompoundReqArgs(compoundReqArgs);
        return pjp.proceed();
    }

    private Set<Integer> getCompoundRequestParamIndices(Annotation[][] paramAnnotations) {
        Set<Integer> compoundReqParamIndices = new HashSet<>();
        for (int i = 0; i < paramAnnotations.length; i++) {
            if (Arrays.stream(paramAnnotations[i]).anyMatch(annotation -> annotation instanceof CompoundRequestParam)) {
                compoundReqParamIndices.add(i);
            }
        }
        return compoundReqParamIndices;
    }

    private Map<Class<?>, Object> getCompoundRequestArguments(Class<?>[] argTypes, Object[] args,
                                                              Set<Integer> argIndices) {
        Map<Class<?>, Object> compoundReqArgs = new HashMap<>();
        for (int i : argIndices) {
            compoundReqArgs.put(argTypes[i], args[i]);
        }
        return compoundReqArgs;
    }

    private void bindReqArgsToFieldsOfCompoundReqArgs(Map<Class<?>, Object> compoundReqArgs) {
        for (Class<?> compoundReqParamClass: compoundReqArgs.keySet()) {
            Map<String, String> fieldToReqParamMap = getFieldToRequestParamMap(compoundReqParamClass);
            Object compoundReqArg = compoundReqArgs.get(compoundReqParamClass);
            Map<String, String> reqParamMap = getRequestParams();
            for (Field field: compoundReqParamClass.getDeclaredFields()) {
                String reqParamName = fieldToReqParamMap.get(field.getName());
                String fieldValueString = reqParamMap.get(reqParamName);
                Object fieldValue = conversionService.convert(fieldValueString, field.getType());
                field.setAccessible(true); // allow setting value on a private field
                try {
                    field.set(compoundReqArg, fieldValue);
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException("Tried to access a private field reflectively without " +
                            "pre-calling Field.setAccessible(true)");
                }
            }
        }
    }

    private Map<String, String> getFieldToRequestParamMap(Class<?> compoundReqParamClass) {
        if (MULTICLASS_FIELD_TO_REQ_PARAM_CACHE.containsKey(compoundReqParamClass)) {
            return MULTICLASS_FIELD_TO_REQ_PARAM_CACHE.get(compoundReqParamClass);
        }
        Map<String, String> fieldToReqParamMap = new HashMap<>();
        for (Field field: compoundReqParamClass.getDeclaredFields()) {
            RequestParamName reqParamName = field.getAnnotation(RequestParamName.class);
            if (reqParamName != null && !StringUtils.isEmpty(reqParamName.value())) {
                fieldToReqParamMap.put(field.getName(), reqParamName.value());
            }
        }
        MULTICLASS_FIELD_TO_REQ_PARAM_CACHE.put(compoundReqParamClass, fieldToReqParamMap);
        return fieldToReqParamMap;
    }

    private Map<String, String> getRequestParams() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            throw new IllegalStateException("Request attributes = null. " +
                    "CompoundReqParamDataBinderAspect.bindRequestParams() must only " +
                    "be advised on controller methods, which accept HTTP requests.");
        }
        HttpServletRequest request = requestAttributes.getRequest();
        Map<String, String[]> requestParams = request.getParameterMap();
        return requestParams.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), entry.getValue()[0]))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
