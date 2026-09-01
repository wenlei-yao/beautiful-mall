package com.yao.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yao.admin.dao.entity.AdminUserDO;
import com.yao.admin.mapper.AdminUserMapper;
import com.yao.admin.service.AdminUserService;
import org.springframework.stereotype.Service;

/**
 * 后台管理员 Service 实现。
 */
@Service
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUserDO>
        implements AdminUserService {
}
