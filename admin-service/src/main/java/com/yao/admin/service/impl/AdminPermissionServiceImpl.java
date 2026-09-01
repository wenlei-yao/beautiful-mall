package com.yao.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yao.admin.dao.entity.AdminPermissionDO;
import com.yao.admin.mapper.AdminPermissionMapper;
import com.yao.admin.service.AdminPermissionService;
import org.springframework.stereotype.Service;

/**
 * 后台权限 Service 实现。
 */
@Service
public class AdminPermissionServiceImpl extends ServiceImpl<AdminPermissionMapper, AdminPermissionDO>
        implements AdminPermissionService {
}
