package com.yao.coupon.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户优惠券实体
 */
@Data
@TableName("user_coupon")
@Builder
public class UserCouponDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 券码
     */
    private String couponCode;

    private Long userId;

    private Long templateId;

    private Long activityId;

    /**
     * 状态：1-未使用，2-已使用，3-已过期，4-已冻结
     */
    private Integer status;

    private LocalDateTime receiveTime;

    private LocalDateTime validStartTime;

    private LocalDateTime validEndTime;

    private LocalDateTime useTime;

    private String orderNo;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}