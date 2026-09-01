package com.yao.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yao.admin.dao.entity.AdminRolePermissionDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 后台角色权限关联 Mapper。
 */
@Mapper
public interface AdminRolePermissionMapper extends BaseMapper<AdminRolePermissionDO> {
}
