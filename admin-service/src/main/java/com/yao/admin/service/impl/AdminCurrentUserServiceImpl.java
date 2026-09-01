package com.yao.admin.service.impl;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import com.yao.admin.dao.entity.AdminUserDO;
import com.yao.admin.dto.resp.CurrentAdminRespDTO;
import com.yao.admin.service.AdminAuthorizationService;
import com.yao.admin.service.AdminCurrentUserService;
import com.yao.admin.service.AdminUserService;
import org.springframework.stereotype.Service;

/**
 * 当前管理员信息 Service 实现。
 */
@Service
public class AdminCurrentUserServiceImpl implements AdminCurrentUserService {

    private final AdminUserService adminUserService;
    private final AdminAuthorizationService adminAuthorizationService;

    public AdminCurrentUserServiceImpl(AdminUserService adminUserService,
                                       AdminAuthorizationService adminAuthorizationService) {
        this.adminUserService = adminUserService;
        this.adminAuthorizationService = adminAuthorizationService;
    }

    @Override
    public CurrentAdminRespDTO getCurrentAdmin(Long adminId) {
        AdminUserDO adminUser = adminUserService.getById(adminId);
        if (adminUser == null || !Boolean.TRUE.equals(adminUser.getEnabled())) {
            StpUtil.logout();
            throw new NotLoginException(
                    "请先登录",
                    StpUtil.TYPE,
                    NotLoginException.INVALID_TOKEN
            );
        }

        return CurrentAdminRespDTO.builder()
                .id(adminUser.getId())
                .username(adminUser.getUsername())
                .displayName(adminUser.getDisplayName())
                .mobile(adminUser.getMobile())
                .enabled(adminUser.getEnabled())
                .roles(adminAuthorizationService.getRoles(adminId))
                .permissions(adminAuthorizationService.getPermissionCodes(adminId))
                .lastLoginTime(adminUser.getLastLoginTime())
                .lastLoginIp(adminUser.getLastLoginIp())
                .createdTime(adminUser.getCreatedTime())
                .build();
    }
}
