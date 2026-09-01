package com.yao.admin.dto.req;

import lombok.Data;

/**
 * 管理员账号登录请求。
 */
@Data
public class AccountLoginReqDTO {

    private String username;

    private String password;
}
