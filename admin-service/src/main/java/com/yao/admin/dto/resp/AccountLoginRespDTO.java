package com.yao.admin.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 管理员账号登录响应。
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountLoginRespDTO {

    private String accessToken;

    private String tokenType;

    private long expiresIn;
}
