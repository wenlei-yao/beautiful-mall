package com.yao.coupon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yao.coupon.entity.CouponTemplate;
import com.yao.coupon.mapper.CouponTemplateMapper;
import com.yao.coupon.service.CouponTemplateService;
import org.springframework.stereotype.Service;

@Service
public class CouponTemplateServiceImpl extends ServiceImpl<CouponTemplateMapper, CouponTemplate> implements CouponTemplateService {
}