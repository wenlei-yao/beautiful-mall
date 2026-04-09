package com.yao.coupon.constant;

public class CouponRedisConstant {
    /**
     * 优惠券活动 key
     */
    public final static String COUPON_ACTIVITY_KEY = "coupon:activity:%s";
    /**
     * 优惠券活动 lock key
     */
    public final static String COUPON_ACTIVITY_LOCK_KEY = "coupon:activity:lock:%s";

    /**
     * 活动空值对象
     */
    public final static String COUPON_ACTIVITY_NULL_KEY = "coupon:activity:is_null:%s";
    /**
     * 优惠券库存 key
     */
    public final static String COUPON_ACTIVITY_STOCK_KEY = "coupon:activity:stock:%s";
    /**
     * 优惠券模板 key
     */
    public final static String COUPON_TEMPLATE_KEY = "coupon:template:%s";
    /**
     * 优惠券模板 lock key
     */
    public final static String COUPON_TEMPLATE_LOCK_KEY = "coupon:template:lock:%s";
    /**
     * 优惠券模板空值
     */
    public final static String COUPON_TEMPLATE_NULL_KEY = "coupon:template:is_null:%s";
    /**
     * 用户领取数量 用户 id 和 活动 id
     */
    public final static String COUPON_USER_ACTIVITY_LIMIT = "coupon:user-activity-limit:%s_%s";
}
