package com.yao.admin.service;

import com.yao.admin.dto.resp.CurrentAdminRoleRespDTO;

import java.util.List;

/**
 * 后台管理员授权信息 Service。
 */
public interface AdminAuthorizationService {

    /**
     * 查询管理员当前有效的角色。
     *
     * @param adminId 管理员 ID
     * @return 角色摘要列表
     */
    List<CurrentAdminRoleRespDTO> getRoles(Long adminId);

    /**
     * 查询管理员当前有效的角色编码。
     *
     * @param adminId 管理员 ID
     * @return 角色编码列表
     */
    List<String> getRoleCodes(Long adminId);

    /**
     * 查询管理员通过有效角色获得的有效权限编码。
     *
     * @param adminId 管理员 ID
     * @return 权限编码列表
     */
    List<String> getPermissionCodes(Long adminId);
}
