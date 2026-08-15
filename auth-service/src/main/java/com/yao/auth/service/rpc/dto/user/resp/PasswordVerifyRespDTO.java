package com.yao.auth.service.rpc.dto.user.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 账号密码校验响应。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordVerifyRespDTO {

    private Long userId;
}
