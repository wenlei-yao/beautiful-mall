package com.yao.auth.service.rpc.dto.user.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 账号密码校验请求。
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordVerifyReqDTO {

    private String identifier;

    private String password;
}
