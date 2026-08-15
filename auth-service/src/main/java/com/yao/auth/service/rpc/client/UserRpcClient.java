package com.yao.auth.service.rpc.client;

import com.yao.auth.service.rpc.dto.user.req.PasswordVerifyReqDTO;
import com.yao.auth.service.rpc.dto.user.resp.PasswordVerifyRespDTO;
import com.yao.base.framework.common.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service")
public interface UserRpcClient {
    @PostMapping("/api/users/password/verify")
    Result<PasswordVerifyRespDTO> verifyPassword(@RequestBody PasswordVerifyReqDTO request);
}
