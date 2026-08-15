package com.yao.auth.service.strategy.impl;

import com.yao.auth.service.dto.request.LoginRequest;
import com.yao.auth.service.entity.ChannelCredential;
import com.yao.auth.service.strategy.AbstractAuthStrategy;
import org.springframework.stereotype.Component;

@Component
public class PasswordAuthStrategy extends AbstractAuthStrategy {

    @Override
    protected ChannelCredential doAuthenticate(LoginRequest request) {
        //查询用户账号密码
        return ChannelCredential.ofUserId(10001L);
    }

    @Override
    public String getLoginType() {
        return "password";
    }
}
