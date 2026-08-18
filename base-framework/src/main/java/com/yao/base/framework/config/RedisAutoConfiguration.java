package com.yao.base.framework.config;

import com.yao.base.framework.redis.RedisService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
@ConditionalOnProperty(prefix = "redis.starter", name = "enable", havingValue = "true")
public class RedisAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(RedisService.class)
    public RedisService redisService(StringRedisTemplate stringRedisTemplate) {
        return new RedisService(stringRedisTemplate);
    }
}
