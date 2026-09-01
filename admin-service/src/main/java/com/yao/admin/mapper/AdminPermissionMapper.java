package com.yao.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yao.admin.dao.entity.AdminPermissionDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 后台权限 Mapper。
 */
@Mapper
public interface AdminPermissionMapper extends BaseMapper<AdminPermissionDO> {

    List<String> selectEnabledPermissionCodesByAdminId(@Param("adminId") Long adminId);
}
