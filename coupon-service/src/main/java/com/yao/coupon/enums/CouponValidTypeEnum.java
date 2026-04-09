package com.yao.coupon.enums;

import com.yao.coupon.dao.entity.CouponTemplateDO;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.function.BiFunction;

/**
 * 券有效期类型枚举
 */
public enum CouponValidTypeEnum {
    /**
     * 固定时间区间
     */
    FIXED(1, (template, receiveTime) -> {
        LocalDateTime start = template.getValidStartTime();
        LocalDateTime end = template.getValidEndTime();
        if (start == null || end == null) {
            throw new IllegalStateException("固定时间类型必须提供起止时间");
        }
        return new TimeRange(start, end);
    }),

    /**
     * 领取后 N 天内有效
     */
    AFTER_RECEIVE(2, (template, receiveTime) -> {
        Integer days = template.getValidDays();
        if (days == null || days <= 0) {
            throw new IllegalStateException("领取后有效天数必须为正整数");
        }
        // 通常开始时间为领取时刻，结束时间为开始时间 + days 天
        LocalDateTime start = receiveTime;
        LocalDateTime end = receiveTime.plusDays(days);
        return new TimeRange(start, end);
    });

    @Getter
    private final int code;
    private final BiFunction<CouponTemplateDO, LocalDateTime, TimeRange> calculator;

    CouponValidTypeEnum(int code, BiFunction<CouponTemplateDO, LocalDateTime, TimeRange> calculator) {
        this.code = code;
        this.calculator = calculator;
    }

    /**
     * 计算有效期范围
     *
     * @param template    券模板（包含有效期相关字段）
     * @param receiveTime 用户领券时间
     * @return 有效期起止时间
     */
    public TimeRange calculate(CouponTemplateDO template, LocalDateTime receiveTime) {
        return calculator.apply(template, receiveTime);
    }

    /**
     * 根据 code 获取枚举实例
     */
    public static CouponValidTypeEnum fromCode(int code) {
        for (CouponValidTypeEnum type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("无效的有效期类型: " + code);
    }

    /**
     * 内部类：表示一段时间区间（不可变）
     */
    @Getter
    public static class TimeRange {
        private final LocalDateTime start;
        private final LocalDateTime end;

        public TimeRange(LocalDateTime start, LocalDateTime end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public String toString() {
            return "TimeRange{" + "start=" + start + ", end=" + end + '}';
        }
    }
}