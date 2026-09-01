package com.yao.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yao.admin.dao.entity.AdminRolePermissionDO;
import com.yao.admin.mapper.AdminRolePermissionMapper;
import com.yao.admin.service.AdminRolePermissionService;
import org.springframework.stereotype.Service;

/**
 * 后台角色权限关联 Service 实现。
 */
@Service
public class AdminRolePermissionServiceImpl
        extends ServiceImpl<AdminRolePermissionMapper, AdminRolePermissionDO>
        implements AdminRolePermissionService {
}
