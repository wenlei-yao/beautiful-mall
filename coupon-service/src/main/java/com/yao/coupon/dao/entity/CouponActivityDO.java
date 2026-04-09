package com.yao.coupon.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 优惠券活动表实体
 */
@Data
@TableName("coupon_activity")
public class CouponActivityDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private Long templateId;

    /**
     * 活动类型：1-用户领取，2-系统发放，3-新人专享，4-会员专享
     */
    private Integer activityType;

    private Integer totalCount;

    private Integer remainingCount;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    /**
     * 目标用户（JSON：会员等级、用户标签等）
     */
    private String targetUser;

    /**
     * 状态：1-未开始，2-进行中，3-已结束，4-已停止
     */
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}