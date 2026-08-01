package com.yao.base.framework.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yao.base.framework.aspect.ControllerLogAspect;
import com.yao.base.framework.config.filter.TraceIdFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class BaseAutoConfiguration {

    @Bean
    public FilterRegistrationBean<TraceIdFilter> traceFilterRegistration() {
        FilterRegistrationBean<TraceIdFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new TraceIdFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registration.setName("traceFilter");
        return registration;
    }

    @Bean
    public GlobalExceptionAdvice globalExceptionAdvice(){
        return new GlobalExceptionAdvice();
    }

    @Bean
    public ControllerLogAspect controllerLogAspect(){
        return new ControllerLogAspect(new ObjectMapper());
    }

}
