package com.yao.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yao.admin.dao.entity.AdminRoleDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 后台角色 Mapper。
 */
@Mapper
public interface AdminRoleMapper extends BaseMapper<AdminRoleDO> {

    List<AdminRoleDO> selectEnabledRolesByAdminId(@Param("adminId") Long adminId);
}
