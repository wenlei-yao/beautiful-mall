package com.yao.auth.service.strategy;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AuthStrategyFactory {
    private final Map<String, AuthStrategy> strategyMap = new ConcurrentHashMap<>();

    public AuthStrategyFactory(List<AuthStrategy> authStrategies) {
        for (AuthStrategy authStrategy : authStrategies) {
            strategyMap.put(authStrategy.getLoginType(), authStrategy);
        }
    }

    public AuthStrategy getStrategy(String loginType) {
        AuthStrategy strategy = strategyMap.get(loginType);
        if (strategy == null) {
            throw new IllegalArgumentException("不支持的登录类型: " + loginType);
        }
        return strategy;
    }
}
