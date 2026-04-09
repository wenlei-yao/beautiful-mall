package com.yao.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yao.coupon.dao.entity.CouponActivityDO;
import com.yao.coupon.dto.req.CouponActivityReqDTO;
import com.yao.coupon.dto.resp.CouponActivityQueryRespDTO;

/**
 * 优惠券活动 Service 接口
 */
public interface CouponActivityService extends IService<CouponActivityDO> {

    CouponActivityQueryRespDTO findCouponActivity(CouponActivityReqDTO couponActivityReqDTO);


}