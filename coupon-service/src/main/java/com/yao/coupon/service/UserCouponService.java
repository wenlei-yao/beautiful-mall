package com.yao.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yao.coupon.dao.entity.UserCouponDO;
import com.yao.coupon.dto.req.ReceiveCouponReqDTO;

/**
 * 用户优惠券 Service 接口
 */
public interface UserCouponService extends IService<UserCouponDO> {

    //领取优惠券
    public void receiveCoupon(ReceiveCouponReqDTO receiveCouponReqDto);
}