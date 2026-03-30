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
    DUPLICATE_SUBMIT(407, "请勿重复提交");

    private final int code;
    private final String message;
}
