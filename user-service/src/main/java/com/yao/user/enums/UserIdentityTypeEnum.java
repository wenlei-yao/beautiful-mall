package com.yao.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户登录凭证类型。
 */
@Getter
@AllArgsConstructor
public enum UserIdentityTypeEnum {

    PASSWORD("password"),
    SMS("sms");

    private final String type;
}
