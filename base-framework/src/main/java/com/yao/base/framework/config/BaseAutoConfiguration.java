package com.yao.base.framework.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yao.base.framework.aspect.ControllerLogAspect;
import com.yao.base.framework.config.interceptor.TraceIdInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
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
    public GlobalExceptionAdvice globalExceptionAdvice(){
        return new GlobalExceptionAdvice();
    }

    @Bean
    public ControllerLogAspect controllerLogAspect(){
        return new ControllerLogAspect(new ObjectMapper());
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer();

        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);
        template.afterPropertiesSet();
        return template;
    }
}
