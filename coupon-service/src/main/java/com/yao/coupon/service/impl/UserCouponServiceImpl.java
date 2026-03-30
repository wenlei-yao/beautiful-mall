package com.yao.coupon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yao.coupon.entity.UserCoupon;
import com.yao.coupon.mapper.UserCouponMapper;
import com.yao.coupon.service.UserCouponService;
import org.springframework.stereotype.Service;

/**
 * 用户优惠券 Service 实现类
 */
@Service
public class UserCouponServiceImpl extends ServiceImpl<UserCouponMapper, UserCoupon> implements UserCouponService {
}