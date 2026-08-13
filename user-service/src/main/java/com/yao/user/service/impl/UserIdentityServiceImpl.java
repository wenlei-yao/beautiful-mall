package com.yao.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yao.user.dao.entity.UserIdentityDO;
import com.yao.user.mapper.UserIdentityMapper;
import com.yao.user.service.UserIdentityService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 用户登录凭证 Service 实现。
 */
@Service
public class UserIdentityServiceImpl extends ServiceImpl<UserIdentityMapper, UserIdentityDO> implements UserIdentityService {

    @Override
    public Optional<UserIdentityDO> findByIdentity(String identityType, String identifier) {
        UserIdentityDO userIdentity = lambdaQuery()
                .eq(UserIdentityDO::getIdentityType, identityType)
                .eq(UserIdentityDO::getIdentifier, identifier)
                .one();
        return Optional.ofNullable(userIdentity);
    }

    @Override
    public List<UserIdentityDO> listByUserId(Long userId) {
        return lambdaQuery()
                .eq(UserIdentityDO::getUserId, userId)
                .list();
    }
}
