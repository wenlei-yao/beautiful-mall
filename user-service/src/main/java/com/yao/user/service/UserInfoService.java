package com.yao.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yao.user.dao.entity.UserInfoDO;

import java.util.List;
import java.util.Optional;

/**
 * 用户基础信息 Service。
 */
public interface UserInfoService extends IService<UserInfoDO> {

    Optional<UserInfoDO> findByUserId(Long userId);

    List<UserInfoDO> listByMobile(String mobile);
}
