package com.yao.base.framework.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.codec.ErrorDecoder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign 统一错误解码自动配置。
 */
@Configuration
@ConditionalOnClass(ErrorDecoder.class)
public class FeignErrorDecoderAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ErrorDecoder.class)
    public ErrorDecoder resultFeignErrorDecoder(ObjectMapper objectMapper) {
        return new ResultFeignErrorDecoder(objectMapper);
    }
}
