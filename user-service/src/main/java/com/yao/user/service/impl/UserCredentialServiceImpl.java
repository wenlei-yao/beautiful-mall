package com.yao.user.service.impl;

import com.yao.base.framework.common.enums.ResponseCodeEnum;
import com.yao.base.framework.expection.BusinessException;
import com.yao.user.dao.entity.UserIdentityDO;
import com.yao.user.dao.entity.UserInfoDO;
import com.yao.user.dto.req.PasswordVerifyReqDTO;
import com.yao.user.dto.resp.PasswordVerifyRespDTO;
import com.yao.user.enums.UserIdentityTypeEnum;
import com.yao.user.enums.UserStatusEnum;
import com.yao.user.service.UserCredentialService;
import com.yao.user.service.UserIdentityService;
import com.yao.user.service.UserInfoService;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * 用户凭证校验 Service 实现。
 */
@Service
public class UserCredentialServiceImpl implements UserCredentialService {

    private final UserIdentityService userIdentityService;
    private final UserInfoService userInfoService;

    public UserCredentialServiceImpl(UserIdentityService userIdentityService,
                                     UserInfoService userInfoService) {
        this.userIdentityService = userIdentityService;
        this.userInfoService = userInfoService;
    }

    @Override
    public PasswordVerifyRespDTO verifyPassword(PasswordVerifyReqDTO request) {
        validateRequest(request);
        UserIdentityDO identity = userIdentityService
                .findByIdentity(UserIdentityTypeEnum.PASSWORD.getType(), request.getIdentifier())
                .orElseThrow(() -> new BusinessException(ResponseCodeEnum.LOGIN_INVALID));

        if (!matchesPlaintextPassword(request.getPassword(), identity.getCredential())) {
            throw new BusinessException(ResponseCodeEnum.LOGIN_INVALID);
        }

        UserInfoDO userInfo = userInfoService
                .findByUserId(identity.getUserId())
                .orElseThrow(() -> new BusinessException(ResponseCodeEnum.LOGIN_INVALID));

        if (!Integer.valueOf(UserStatusEnum.NORMAL.getCode()).equals(userInfo.getStatus())) {
            throw new BusinessException(ResponseCodeEnum.ACCOUNT_DISABLED);
        }

        return new PasswordVerifyRespDTO(userInfo.getUserId());
    }

    private void validateRequest(PasswordVerifyReqDTO request) {
        if (request == null
                || isBlank(request.getIdentifier())
                || isBlank(request.getPassword())) {
            throw new BusinessException(ResponseCodeEnum.LOGIN_PARAM_EMPTY);
        }
    }

    private boolean matchesPlaintextPassword(String rawPassword, String credential) {
        if (credential == null || credential.isEmpty()) {
            return false;
        }
        return MessageDigest.isEqual(
                rawPassword.getBytes(StandardCharsets.UTF_8),
                credential.getBytes(StandardCharsets.UTF_8)
        );
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
