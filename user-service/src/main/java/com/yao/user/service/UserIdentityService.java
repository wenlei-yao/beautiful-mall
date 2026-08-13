package com.yao.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yao.user.dao.entity.UserIdentityDO;

import java.util.List;
import java.util.Optional;

/**
 * 用户登录凭证 Service。
 */
public interface UserIdentityService extends IService<UserIdentityDO> {

    Optional<UserIdentityDO> findByIdentity(String identityType, String identifier);

    List<UserIdentityDO> listByUserId(Long userId);
}
