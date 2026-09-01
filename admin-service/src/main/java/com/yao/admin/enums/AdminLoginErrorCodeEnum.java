package com.yao.admin.enums;

import com.yao.base.framework.expection.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 管理员登录错误码。
 */
@Getter
@AllArgsConstructor
public enum AdminLoginErrorCodeEnum implements ErrorCode {

    LOGIN_PARAM_EMPTY(30001, "用户名或密码不能为空"),
    LOGIN_INVALID(30002, "用户名或密码错误"),
    ACCOUNT_DISABLED(30003, "管理员账号已禁用");

    private final int code;
    private final String message;
}
