package com.yao.admin.service.impl;

import com.yao.admin.dao.entity.AdminRoleDO;
import com.yao.admin.dto.resp.CurrentAdminRoleRespDTO;
import com.yao.admin.mapper.AdminPermissionMapper;
import com.yao.admin.mapper.AdminRoleMapper;
import com.yao.admin.service.AdminAuthorizationService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 后台管理员授权信息 Service 实现。
 */
@Service
public class AdminAuthorizationServiceImpl implements AdminAuthorizationService {

    private final AdminRoleMapper adminRoleMapper;
    private final AdminPermissionMapper adminPermissionMapper;

    public AdminAuthorizationServiceImpl(AdminRoleMapper adminRoleMapper,
                                         AdminPermissionMapper adminPermissionMapper) {
        this.adminRoleMapper = adminRoleMapper;
        this.adminPermissionMapper = adminPermissionMapper;
    }

    @Override
    public List<CurrentAdminRoleRespDTO> getRoles(Long adminId) {
        if (adminId == null) {
            return Collections.emptyList();
        }
        return adminRoleMapper.selectEnabledRolesByAdminId(adminId)
                .stream()
                .map(this::toRoleResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getRoleCodes(Long adminId) {
        return getRoles(adminId)
                .stream()
                .map(CurrentAdminRoleRespDTO::getCode)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getPermissionCodes(Long adminId) {
        if (adminId == null) {
            return Collections.emptyList();
        }
        return adminPermissionMapper.selectEnabledPermissionCodesByAdminId(adminId);
    }

    private CurrentAdminRoleRespDTO toRoleResponse(AdminRoleDO role) {
        return CurrentAdminRoleRespDTO.builder()
                .id(role.getId())
                .code(role.getCode())
                .name(role.getName())
                .build();
    }
}
