package com.yao.coupon.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CouponTemplateQueryRespDTO {
    private Long id;

    private String name;

    private String logo;

    private String description;

    /**
     * 券类型：1-满减券，2-折扣券，3-立减券，4-运费券
     */
    private Integer category;

    /**
     * 产品线：1-通用，2-品类，3-店铺，4-单品
     */
    private Integer productLine;

    private Integer couponCount;

    private Integer userLimit;

    /**
     * 使用门槛
     */
    private BigDecimal threshold;

    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;

    /**
     * 折扣率
     */
    private BigDecimal discountRate;

    /**
     * 最大优惠金额
     */
    private BigDecimal maxDiscount;

    /**
     * 有效期类型：1-固定时间，2-领取后N天
     */
    private Integer validType;

    private LocalDateTime validStartTime;

    private LocalDateTime validEndTime;

    private Integer validDays;

    /**
     * 使用规则JSON
     */
    private String ruleJson;

    private Integer status;
}
