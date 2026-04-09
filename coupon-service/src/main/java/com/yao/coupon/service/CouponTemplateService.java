package com.yao.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yao.coupon.dao.entity.CouponTemplateDO;
import com.yao.coupon.dto.req.CouponTemplateReqDTO;
import com.yao.coupon.dto.resp.CouponTemplateQueryRespDTO;

public interface CouponTemplateService extends IService<CouponTemplateDO> {

    CouponTemplateQueryRespDTO findTemplate(CouponTemplateReqDTO couponTemplateReqDTO);
}