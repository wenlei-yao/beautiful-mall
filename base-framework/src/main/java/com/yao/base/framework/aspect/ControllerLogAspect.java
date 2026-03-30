package com.yao.base.framework.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Aspect
public class ControllerLogAspect {

    private static final Logger log = LoggerFactory.getLogger(ControllerLogAspect.class);
    private static final int MAX_ARGS_LOG_LENGTH = 2000;

    private final ObjectMapper objectMapper;

    public ControllerLogAspect(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Around("execution(* com.yao..controller..*(..))")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        String path = resolvePath();
        String method = resolveMethodName(joinPoint);
        String args = buildArgsLog(joinPoint.getArgs());
        log.info("request:path={} method={} args={}", path, method, args);
        try {
            Object response = joinPoint.proceed();
            long cost = System.currentTimeMillis() - start;
            log.info("response={} cost={}ms", buildResponseLog(response), cost);
            return response;
        } catch (Throwable ex) {
            long cost = System.currentTimeMillis() - start;
            log.error("request failed cost={}", cost, ex);
            throw ex;
        }
    }

    private String resolvePath() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes instanceof ServletRequestAttributes) {
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) attributes;
            HttpServletRequest request = servletRequestAttributes.getRequest();
            if (request != null) {
                return request.getRequestURI();
            }
        }
        return "N/A";
    }

    private String resolveMethodName(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getDeclaringTypeName() + "." + signature.getName();
    }

    private String buildArgsLog(Object[] args) {
        List<String> serializedArgs = new ArrayList<>();
        for (Object arg : args) {
            if (shouldSkip(arg)) {
                continue;
            }
            serializedArgs.add(serializeArg(arg));
        }
        String joined = serializedArgs.toString();
        if (joined.length() > MAX_ARGS_LOG_LENGTH) {
            return joined.substring(0, MAX_ARGS_LOG_LENGTH) + "...";
        }
        return joined;
    }

    private boolean shouldSkip(Object arg) {
        return arg instanceof HttpServletRequest
                || arg instanceof HttpServletResponse
                || arg instanceof MultipartFile
                || arg instanceof MultipartFile[]
                || arg instanceof Model
                || arg instanceof ModelMap
                || arg instanceof BindingResult;
    }

    private String serializeArg(Object arg) {
        if (arg == null) {
            return "null";
        }
        try {
            return objectMapper.writeValueAsString(arg);
        } catch (JsonProcessingException ex) {
            return "<" + arg.getClass().getName() + ">";
        }
    }

    private String buildResponseLog(Object response) {
        String value = serializeArg(response);
        if (value.length() > MAX_ARGS_LOG_LENGTH) {
            return value.substring(0, MAX_ARGS_LOG_LENGTH) + "...";
        }
        return value;
    }
}
