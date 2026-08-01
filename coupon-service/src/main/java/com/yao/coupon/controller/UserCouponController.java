package com.yao.coupon.controller;

import com.yao.base.framework.common.Result;
import com.yao.coupon.dto.req.ReceiveCouponReqDTO;
import com.yao.coupon.dto.req.UserCouponQueryReqDTO;
import com.yao.coupon.dto.resp.UserCouponQueryRespDTO;
import com.yao.coupon.service.UserCouponService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户优惠券服务
 */
@RestController
@AllArgsConstructor
public class UserCouponController {

    private final UserCouponService userCouponService;

    /**
     * 领取优惠券
     */
    @PostMapping("/api/user-coupon/receive")
    public Result<Void> receive(@RequestBody ReceiveCouponReqDTO receiveCouponReqDTO){
        userCouponService.receiveCoupon(receiveCouponReqDTO);
        return Result.success();
    }
    /**
     * 查询用户可用优惠券
     */
    @PostMapping("/api/user-coupon/coupon-query")
    public Result<UserCouponQueryRespDTO> listUserCoupon(@RequestBody UserCouponQueryReqDTO userCouponQueryReqDTO){
        return Result.success(userCouponService.listUserCoupon(userCouponQueryReqDTO));
    }


}
