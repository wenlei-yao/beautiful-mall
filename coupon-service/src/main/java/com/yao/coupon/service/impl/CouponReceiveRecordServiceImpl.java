package com.yao.coupon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yao.coupon.entity.CouponReceiveRecord;
import com.yao.coupon.mapper.CouponReceiveRecordMapper;
import com.yao.coupon.service.CouponReceiveRecordService;
import org.springframework.stereotype.Service;

/**
 * 领券记录 Service 实现类
 */
@Service
public class CouponReceiveRecordServiceImpl extends ServiceImpl<CouponReceiveRecordMapper, CouponReceiveRecord> implements CouponReceiveRecordService {
}