package com.yao.base.framework.common.enums;

import com.yao.base.framework.expection.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCodeEnum implements ErrorCode {
    SUCCESS(0, "success"),
    FILE(1,"系统异常"),

    LOGIN_PARAM_EMPTY(400, "用户名或密码不能为空"),
    NOT_LOGIN(401, "未登录！"),
    LOGIN_INVALID(402, "用户名或密码错误"),
    PRODUCT_NAME_EMPTY(403, "商品名称不能为空"),
    PRODUCT_SKU_PRICE_EMPTY(404, "SKU销售价格不能为空"),
    PRODUCT_SAVE_FAILED(405, "商品保存失败"),
    PRODUCT_SKU_SAVE_FAILED(406, "商品SKU保存失败"),
    DUPLICATE_SUBMIT(407, "请勿重复提交"),


    //活动有关
    ACTIVITY_IS_NULL(1001,"活动已结束!"),
    ACTIVITY_UN_START(1002,"活动尚未开始"),

    //模板有关
    TEMPLATE_ID_NULL(2001,"优惠券模板不存在!"),
    TEMPLATE_DECREMENT_FAIL(2002,"优惠券扣减失败");

    private final int code;
    private final String message;
}
