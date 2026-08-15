package com.yao.user.controller;

import com.yao.base.framework.common.Result;
import com.yao.user.dto.req.PasswordVerifyReqDTO;
import com.yao.user.dto.resp.PasswordVerifyRespDTO;
import com.yao.user.service.UserCredentialService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户凭证校验接口。
 */
@RestController
public class UserCredentialController {

    private final UserCredentialService userCredentialService;

    public UserCredentialController(UserCredentialService userCredentialService) {
        this.userCredentialService = userCredentialService;
    }

    @PostMapping("/api/users/password/verify")
    public Result<PasswordVerifyRespDTO> verifyPassword(
            @RequestBody PasswordVerifyReqDTO request) {
        return Result.success(userCredentialService.verifyPassword(request));
    }
}
