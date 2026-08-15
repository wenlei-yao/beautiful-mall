package com.yao.user.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 账号密码校验响应。
 */
@Data
@AllArgsConstructor
public class PasswordVerifyRespDTO {

    private Long userId;
}
