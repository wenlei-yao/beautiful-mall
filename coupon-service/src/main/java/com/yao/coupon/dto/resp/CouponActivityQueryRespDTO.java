package com.yao.coupon.dto.resp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CouponActivityQueryRespDTO {
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
}
