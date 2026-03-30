package com.yao.coupon.enums;

import lombok.Getter;

/**
 * 产品线枚举
 */
@Getter
public enum ProductLineEnum {

    UNIVERSAL(1, "通用券"),
    CATEGORY(2, "品类券"),
    SHOP(3, "店铺券"),
    PRODUCT(4, "单品券");

    private final Integer code;
    private final String desc;

    ProductLineEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}