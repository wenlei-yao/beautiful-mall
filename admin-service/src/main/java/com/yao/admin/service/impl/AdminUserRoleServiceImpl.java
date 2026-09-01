package com.yao.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yao.admin.dao.entity.AdminUserRoleDO;
import com.yao.admin.mapper.AdminUserRoleMapper;
import com.yao.admin.service.AdminUserRoleService;
import org.springframework.stereotype.Service;

/**
 * 后台管理员角色关联 Service 实现。
 */
@Service
public class AdminUserRoleServiceImpl extends ServiceImpl<AdminUserRoleMapper, AdminUserRoleDO>
        implements AdminUserRoleService {
}
