package com.yao.coupon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yao.coupon.entity.CouponActivity;
import com.yao.coupon.mapper.CouponActivityMapper;
import com.yao.coupon.service.CouponActivityService;
import org.springframework.stereotype.Service;

/**
 * 优惠券活动 Service 实现类
 */
@Service
public class CouponActivityServiceImpl extends ServiceImpl<CouponActivityMapper, CouponActivity> implements CouponActivityService {
}