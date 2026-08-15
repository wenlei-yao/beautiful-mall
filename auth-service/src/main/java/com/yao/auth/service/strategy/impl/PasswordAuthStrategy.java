package com.yao.auth.service.strategy.impl;

import com.yao.auth.service.dto.request.LoginRequest;
import com.yao.auth.service.entity.ChannelCredential;
import com.yao.auth.service.rpc.client.UserRpcClient;
import com.yao.auth.service.rpc.dto.user.req.PasswordVerifyReqDTO;
import com.yao.auth.service.rpc.dto.user.resp.PasswordVerifyRespDTO;
import com.yao.auth.service.strategy.AbstractAuthStrategy;
import com.yao.base.framework.common.Result;
import com.yao.base.framework.common.enums.ResponseCodeEnum;
import com.yao.base.framework.expection.BusinessException;
import com.yao.base.framework.expection.ClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PasswordAuthStrategy extends AbstractAuthStrategy {

    private final UserRpcClient userRpcClient;

    public PasswordAuthStrategy(UserRpcClient userRpcClient) {
        this.userRpcClient = userRpcClient;
    }

    @Override
    protected ChannelCredential doAuthenticate(LoginRequest request) {
        //查询用户账号密码
        PasswordVerifyReqDTO build = PasswordVerifyReqDTO.builder()
                .identifier(request.getPasswordParam().getUsername())
                .password(request.getPasswordParam().getPassword())
                .build();
        Result<PasswordVerifyRespDTO> result;
        try {
            result = userRpcClient.verifyPassword(build);
        } catch (ClientException e) {
            log.info("远程调用异常", e);
            throw new BusinessException(ResponseCodeEnum.LOGIN_INVALID);
        }
        return ChannelCredential.ofUserId(result.getData().getUserId());
    }

    @Override
    public String getLoginType() {
        return "password";
    }
}
