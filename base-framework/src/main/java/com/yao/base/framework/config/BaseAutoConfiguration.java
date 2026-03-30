package com.yao.base.framework.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yao.base.framework.aspect.ControllerLogAspect;
import com.yao.base.framework.config.interceptor.TraceIdInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class BaseAutoConfiguration {

    @Bean
    public TraceIdInterceptor traceIdInterceptor() {
        return new TraceIdInterceptor();
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer(TraceIdInterceptor traceIdInterceptor) {
        return new WebMvcConfig(traceIdInterceptor);
    }


    @Bean
    public ControllerLogAspect controllerLogAspect(){
        return new ControllerLogAspect(new ObjectMapper());
    }
}
