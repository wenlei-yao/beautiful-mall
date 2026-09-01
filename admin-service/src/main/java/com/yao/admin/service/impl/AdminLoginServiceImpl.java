package com.yao.admin.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.yao.admin.dao.entity.AdminUserDO;
import com.yao.admin.dto.req.AccountLoginReqDTO;
import com.yao.admin.dto.resp.AccountLoginRespDTO;
import com.yao.admin.enums.AdminLoginErrorCodeEnum;
import com.yao.admin.service.AdminLoginService;
import com.yao.admin.service.AdminUserService;
import com.yao.base.framework.expection.BusinessException;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * 管理员登录 Service 实现。
 */
@Service
public class AdminLoginServiceImpl implements AdminLoginService {

    private static final String TOKEN_TYPE = "Bearer";

    private final AdminUserService adminUserService;

    public AdminLoginServiceImpl(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @Override
    public AccountLoginRespDTO login(AccountLoginReqDTO request) {
        validateRequest(request);

        String username = request.getUsername().trim();
        AdminUserDO adminUser = adminUserService.lambdaQuery()
                .eq(AdminUserDO::getUsername, username)
                .one();

        if (adminUser == null || !matchesPassword(request.getPassword(), adminUser.getPassword())) {
            throw new BusinessException(AdminLoginErrorCodeEnum.LOGIN_INVALID);
        }
        if (!Boolean.TRUE.equals(adminUser.getEnabled())) {
            throw new BusinessException(AdminLoginErrorCodeEnum.ACCOUNT_DISABLED);
        }

        StpUtil.login(adminUser.getId());
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        return AccountLoginRespDTO.builder()
                .accessToken(tokenInfo.getTokenValue())
                .tokenType(TOKEN_TYPE)
                .expiresIn(tokenInfo.getTokenTimeout())
                .build();
    }

    private void validateRequest(AccountLoginReqDTO request) {
        if (request == null || isBlank(request.getUsername()) || isBlank(request.getPassword())) {
            throw new BusinessException(AdminLoginErrorCodeEnum.LOGIN_PARAM_EMPTY);
        }
    }

    private boolean matchesPassword(String rawPassword, String storedPassword) {
        if (storedPassword == null) {
            return false;
        }
        return MessageDigest.isEqual(
                rawPassword.getBytes(StandardCharsets.UTF_8),
                storedPassword.getBytes(StandardCharsets.UTF_8)
        );
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
