package com.yao.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yao.user.dao.entity.UserIdentityDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户登录凭证 Mapper。
 */
@Mapper
public interface UserIdentityMapper extends BaseMapper<UserIdentityDO> {
}
