package com.yao.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yao.user.dao.entity.UserInfoDO;
import com.yao.user.mapper.UserInfoMapper;
import com.yao.user.service.UserInfoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 用户基础信息 Service 实现。
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfoDO> implements UserInfoService {

    @Override
    public Optional<UserInfoDO> findByUserId(Long userId) {
        UserInfoDO userInfo = lambdaQuery()
                .eq(UserInfoDO::getUserId, userId)
                .one();
        return Optional.ofNullable(userInfo);
    }

    @Override
    public List<UserInfoDO> listByMobile(String mobile) {
        return lambdaQuery()
                .eq(UserInfoDO::getMobile, mobile)
                .list();
    }
}
