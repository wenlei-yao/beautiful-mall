package com.yao.coupon.dto.req;

import lombok.Data;

/**
 * 领券请求
 */
@Data
public class ReceiveCouponReqDTO {

    private Long userId;
    /*活动 ID*/
    private Long activityId;
}
