package com.yao.coupon.enums;

import lombok.Getter;

/**
 * 优惠券类型枚举
 */
@Getter
public enum CouponCategoryEnum {

    FULL_REDUCTION(1, "满减券"),
    DISCOUNT(2, "折扣券"),
    IMMEDIATE_REDUCTION(3, "立减券"),
    FREIGHT(4, "运费券");

    private final Integer code;
    private final String desc;

    CouponCategoryEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static CouponCategoryEnum of(Integer code) {
        for (CouponCategoryEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}