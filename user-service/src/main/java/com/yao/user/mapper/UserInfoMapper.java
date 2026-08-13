package com.yao.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yao.user.dao.entity.UserInfoDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户基础信息 Mapper。
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfoDO> {
}
