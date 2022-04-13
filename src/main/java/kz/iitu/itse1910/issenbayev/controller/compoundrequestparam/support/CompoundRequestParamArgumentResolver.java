package kz.iitu.itse1910.issenbayev.controller.compoundrequestparam.support;

import kz.iitu.itse1910.issenbayev.controller.compoundrequestparam.annotation.CompoundRequestParam;
import kz.iitu.itse1910.issenbayev.controller.compoundrequestparam.annotation.RequestParamName;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.validation.Valid;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class CompoundRequestParamArgumentResolver implements HandlerMethodArgumentResolver {
    private final Map<Class<?>, Map<String, String>> MULTICLASS_FIELD_TO_REQ_PARAM_CACHE = new HashMap<>();
    private final ConversionService conversionService;
    private final SpringValidatorAdapter validator;

    @Autowired
    public CompoundRequestParamArgumentResolver(ConversionService conversionService,
                                                SpringValidatorAdapter validator) {
        this.conversionService = conversionService;
        this.validator = validator;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CompoundRequestParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
            throws MethodArgumentNotValidException {
        Class<?> argClass = parameter.getParameterType();
        Constructor<?> constructor = getConstructor(argClass);
        Object[] fieldValues = mapReqParamValuesToFieldValues(webRequest, argClass);
        Object arg = newInstance(constructor, fieldValues);
        validateIfNecessary(arg, parameter);
        return arg;
    }

    private Constructor<?> getConstructor(Class<?> containingClass) {
        try {
            Class<?>[] fieldClasses = getFieldClasses(containingClass);
            return ReflectionUtils.accessibleConstructor(containingClass, fieldClasses);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Class " + containingClass + " must have an all args constructor");
        }
    }

    private Class<?>[] getFieldClasses(Class<?> containingClass) {
        return Arrays.stream(containingClass.getDeclaredFields())
                .map(Field::getType)
                .toArray(Class<?>[]::new);
    }

    private Object[] mapReqParamValuesToFieldValues(NativeWebRequest request, Class<?> containingClass) {
        List<Object> fieldValues = new ArrayList<>();
        Map<String, String> fieldToReqParamMap = getFieldToRequestParamMap(containingClass);
        Map<String, String> reqParamMap = getRequestParams(request);
        for (Field field: containingClass.getDeclaredFields()) {
            String reqParamName = fieldToReqParamMap.get(field.getName());
            String fieldValueString = reqParamMap.get(reqParamName);
            Object fieldValue = conversionService.convert(fieldValueString, field.getType());
            fieldValues.add(fieldValue);
        }
        return fieldValues.toArray();
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

    private Map<String, String> getRequestParams(NativeWebRequest webRequest) {
        Map<String, String[]> requestParams = webRequest.getParameterMap();
        return requestParams.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), entry.getValue()[0]))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Object newInstance(Constructor<?> constructor, Object[] fieldValues) {
        try {
            return constructor.newInstance(fieldValues);
        } catch (InstantiationException e) {
            throw new IllegalStateException(constructor.getDeclaringClass().getName() +
                    " must not be abstract, but it is", e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(constructor.getDeclaringClass().getName() +
                    " must have a public all args constructor", e);
        } catch (InvocationTargetException e) {
            throw new IllegalStateException(constructor.getDeclaringClass().getName() + " threw an exception", e);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private void validateIfNecessary(Object obj, MethodParameter parameter) throws MethodArgumentNotValidException {
        if (!parameter.hasParameterAnnotation(Valid.class)) {
            return;
        }
        BindingResult bindingResult = new BeanPropertyBindingResult(obj, parameter.getParameterType().getSimpleName());
        validator.validate(obj, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(parameter, bindingResult);
        }
    }
}
