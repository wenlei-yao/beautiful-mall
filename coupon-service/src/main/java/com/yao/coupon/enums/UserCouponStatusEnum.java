package com.yao.coupon.enums;

import lombok.Getter;

/**
 * 用户券状态枚举
 */
@Getter
public enum UserCouponStatusEnum {

    UNUSED(1, "未使用"),
    USED(2, "已使用"),
    EXPIRED(3, "已过期"),
    FROZEN(4, "已冻结");

    private final Integer code;
    private final String desc;

    UserCouponStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}