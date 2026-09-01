package com.yao.admin.service;

import com.yao.admin.dto.resp.CurrentAdminRespDTO;

/**
 * 当前管理员信息 Service。
 */
public interface AdminCurrentUserService {

    /**
     * 查询当前管理员的基本信息和授权信息。
     *
     * @param adminId 管理员 ID
     * @return 当前管理员信息
     */
    CurrentAdminRespDTO getCurrentAdmin(Long adminId);
}
