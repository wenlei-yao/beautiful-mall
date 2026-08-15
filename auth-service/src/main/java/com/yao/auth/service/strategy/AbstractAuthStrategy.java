package com.yao.auth.service.strategy;

import com.yao.auth.service.dto.request.LoginRequest;
import com.yao.auth.service.entity.AuthUser;
import com.yao.auth.service.entity.ChannelCredential;

public abstract class AbstractAuthStrategy implements AuthStrategy{

    @Override
    public AuthUser authenticate(LoginRequest request) {
        ChannelCredential credential = doAuthenticate(request);
        Long userId;
        if(credential.isDirectUserId()){
            userId = credential.getUserId();
        }else {
            //根据 credential 中的 channelUserId获取每个渠道对应的主账号userId
            userId = 0L;
        }
        return buildAuthUser(userId, credential);
    }

    protected abstract ChannelCredential doAuthenticate(LoginRequest request);


    protected AuthUser buildAuthUser(Long userId, ChannelCredential credential) {
        return AuthUser.builder()
                .userId(userId)
                .build();
    }



}
