package com.yao.user.dto.req;

import lombok.Data;

/**
 * 账号密码校验请求。
 */
@Data
public class PasswordVerifyReqDTO {

    private String identifier;

    private String password;
}
