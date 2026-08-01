package com.yao.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yao.coupon.dao.entity.UserCouponDO;
import com.yao.coupon.dto.req.ReceiveCouponReqDTO;
import com.yao.coupon.dto.req.UserCouponQueryReqDTO;
import com.yao.coupon.dto.resp.UserCouponQueryRespDTO;

/**
 * 用户优惠券 Service 接口
 */
public interface UserCouponService extends IService<UserCouponDO> {

    //领取优惠券
    public void receiveCoupon(ReceiveCouponReqDTO receiveCouponReqDto);

    //用户可用优惠券查询
    public UserCouponQueryRespDTO listUserCoupon(UserCouponQueryReqDTO userCouponQueryReqDTO);
}