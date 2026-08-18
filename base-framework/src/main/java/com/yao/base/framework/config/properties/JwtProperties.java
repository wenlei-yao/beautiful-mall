package com.yao.base.framework.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secret;
    //默认时效时间是2个小时
    private long accessTokenExpiration = 60 * 60 * 2;
    private long refreshTokenExpiration = 60 * 60 * 24 * 7;
}