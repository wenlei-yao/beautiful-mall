package com.yao.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yao.admin.dao.entity.AdminUserDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 后台管理员 Mapper。
 */
@Mapper
public interface AdminUserMapper extends BaseMapper<AdminUserDO> {
}
