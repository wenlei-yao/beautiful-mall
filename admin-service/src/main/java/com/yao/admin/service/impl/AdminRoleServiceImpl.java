package com.yao.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yao.admin.dao.entity.AdminRoleDO;
import com.yao.admin.mapper.AdminRoleMapper;
import com.yao.admin.service.AdminRoleService;
import org.springframework.stereotype.Service;

/**
 * 后台角色 Service 实现。
 */
@Service
public class AdminRoleServiceImpl extends ServiceImpl<AdminRoleMapper, AdminRoleDO>
        implements AdminRoleService {
}
