package com.yao.coupon.controller;

import com.yao.base.framework.common.Result;
import com.yao.coupon.dto.req.ReceiveCouponReqDTO;
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

    @PostMapping("/api/user-coupon/receive")
    public Result<Void> receive(@RequestBody ReceiveCouponReqDTO receiveCouponReqDTO){
        userCouponService.receiveCoupon(receiveCouponReqDTO);
        return Result.success();
    }
}
