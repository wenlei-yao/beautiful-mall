package com.yao.coupon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum RedisStockDecrementErrorEnum {
    /**
     * 库存不足
     */
    STOCK_INSUFFICIENT(-1, "优惠券已被领取完啦"),

    /**
     * 用户已经达到领取上限
     */
    LIMIT_REACHED(-2, "用户已经达到领取上限");


    @Getter
    private final long code;
    @Getter
    private final String message;

    public static boolean isFail(long code) {
        return code < 0;
    }

    public static String fromType(long code) {
        for (RedisStockDecrementErrorEnum method : values()) {
            if (method.getCode() == code) {
                return method.getMessage();
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}
