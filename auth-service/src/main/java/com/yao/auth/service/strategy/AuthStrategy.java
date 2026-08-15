package com.yao.auth.service.strategy;

import com.yao.auth.service.dto.request.LoginRequest;
import com.yao.auth.service.entity.AuthUser;
import com.yao.auth.service.entity.ChannelCredential;

public interface AuthStrategy {

    /**
     * 策略标识（如 password, sms, wechat_mini）
     */
    String getLoginType();

    /**
     * 执行具体认证逻辑
     * @return 登录成功的 userId
     */
    AuthUser authenticate(LoginRequest request);
}
