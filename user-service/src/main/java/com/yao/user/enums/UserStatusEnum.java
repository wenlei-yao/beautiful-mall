package com.yao.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户账号状态。
 */
@Getter
@AllArgsConstructor
public enum UserStatusEnum {

    DISABLED(0, "禁用/冻结"),
    NORMAL(1, "正常");

    private final int code;
    private final String description;
}
