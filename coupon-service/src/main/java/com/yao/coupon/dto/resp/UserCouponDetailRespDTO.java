package com.yao.coupon.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class UserCouponDetailRespDTO {
    /** 优惠券编码 */
    private String couponCode;
    /** 优惠券名称 */
    private String name;
    /** 描述 */
    private String description;
    /** 优惠金额 */
    private BigDecimal couponAmount;
    /** 优惠券类型 */
    private String category;
    /** 优惠券开始使用时间 */
    private LocalDateTime validStartTime;
    /** 优惠券到期时间 */
    private LocalDateTime validEndTime;
    /** 是否可用 */
    private Boolean available;
    /** 不可用原因 */
    private String unavailableReason;
}
