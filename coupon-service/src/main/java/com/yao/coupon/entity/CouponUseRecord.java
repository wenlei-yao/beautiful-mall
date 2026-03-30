package com.yao.coupon.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 优惠券使用记录表实体
 */
@Data
@TableName("coupon_use_record")
public class CouponUseRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String couponCode;

    private Long userId;

    private String orderNo;

    private BigDecimal orderAmount;

    private BigDecimal discountAmount;

    private LocalDateTime useTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}