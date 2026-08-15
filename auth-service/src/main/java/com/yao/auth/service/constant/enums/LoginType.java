package com.yao.auth.service.constant.enums;

import lombok.Getter;

@Getter
public enum LoginType {

    PASSWORD("password");

    private final String route;

    LoginType(String route) {
        this.route = route;
    }
}
