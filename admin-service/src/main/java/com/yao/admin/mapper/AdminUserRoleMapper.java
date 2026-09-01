package com.yao.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yao.admin.dao.entity.AdminUserRoleDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 后台管理员角色关联 Mapper。
 */
@Mapper
public interface AdminUserRoleMapper extends BaseMapper<AdminUserRoleDO> {
}
