package com.yao.coupon.dto.resp;

import lombok.Data;

import java.util.List;

/**
 * 优惠券使用规则DTO
 */
@Data
public class CouponRuleDTO {

    /**
     * 适用品类ID列表
     */
    private List<Long> categoryIds;

    /**
     * 适用店铺ID列表
     */
    private List<Long> shopIds;

    /**
     * 适用商品ID列表
     */
    private List<Long> productIds;

    /**
     * 排除品类ID列表
     */
    private List<Long> excludeCategoryIds;

    /**
     * 排除商品ID列表
     */
    private List<Long> excludeProductIds;

    /**
     * 是否可与其他券叠加
     */
    private Boolean canStack;

    /**
     * 叠加上限
     */
    private Integer stackLimit;

    /**
     * 适用渠道：1-APP，2-小程序，3-H5，4-全渠道
     */
    private List<Integer> channels;

    /**
     * 适用支付方式
     */
    private List<Integer> payTypes;
}