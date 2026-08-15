package com.yao.auth.service.service.impl;

import com.yao.auth.service.dto.request.LoginRequest;
import com.yao.auth.service.dto.response.LoginResponse;
import com.yao.auth.service.entity.AuthUser;
import com.yao.auth.service.service.AuthService;
import com.yao.auth.service.service.TokenService;
import com.yao.auth.service.strategy.AuthStrategy;
import com.yao.auth.service.strategy.AuthStrategyFactory;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthStrategyFactory authStrategyFactory;

    private final TokenService tokenService;

    public AuthServiceImpl(AuthStrategyFactory authStrategyFactory, TokenService tokenService) {
        this.authStrategyFactory = authStrategyFactory;
        this.tokenService = tokenService;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        // 1. 获取对应的策略
        AuthStrategy strategy = authStrategyFactory.getStrategy(loginRequest.getLoginType());
        // 2. 策略执行认证，组装并返回 AuthUser
        AuthUser authenticate = strategy.authenticate(loginRequest);
        // 3. 将整个 AuthUser 对象交给 TokenService 签发 JWT
        return LoginResponse.builder()
                .token(tokenService.createToken(authenticate.getUserId()))
                .build();
    }
}
