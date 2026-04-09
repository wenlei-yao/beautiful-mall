package com.yao.coupon.dto.resp;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户券信息DTO（展示用）
 */
@Data
public class UserCouponDTO {

    private Long id;

    private String couponCode;

    private String name;

    private String description;

    private Integer category;

    private String categoryDesc;

    private BigDecimal threshold;

    private BigDecimal discountAmount;

    private BigDecimal discountRate;

    private Integer status;

    private String statusDesc;

    private LocalDateTime validStartTime;

    private LocalDateTime validEndTime;

    /**
     * 是否即将过期（3天内）
     */
    private Boolean expiringSoon;

    /**
     * 使用范围描述
     */
    private String scopeDesc;
}