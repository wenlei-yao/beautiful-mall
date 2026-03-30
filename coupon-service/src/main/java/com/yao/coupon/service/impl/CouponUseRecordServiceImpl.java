package com.yao.coupon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yao.coupon.entity.CouponUseRecord;
import com.yao.coupon.mapper.CouponUseRecordMapper;
import com.yao.coupon.service.CouponUseRecordService;
import org.springframework.stereotype.Service;

/**
 * 优惠券使用记录 Service 实现类
 */
@Service
public class CouponUseRecordServiceImpl extends ServiceImpl<CouponUseRecordMapper, CouponUseRecord> implements CouponUseRecordService {
}