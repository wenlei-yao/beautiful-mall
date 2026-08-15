package com.yao.auth.service.service;

import com.yao.auth.service.properties.JwtProperties;
import com.yao.auth.service.utils.JwtUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TokenService {
    private final JwtUtil jwtUtil;
    private final JwtProperties jwtProperties;

    public TokenService(JwtUtil jwtUtil, JwtProperties jwtProperties) {
        this.jwtUtil = jwtUtil;
        this.jwtProperties = jwtProperties;
    }

    public String createToken(Long userId) {
        // 1. 设置 Payload 扩展参数
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        return jwtUtil.generateToken(
                userId,
                claims,
                jwtProperties.getAccessTokenExpiration()
        );
    }


}
