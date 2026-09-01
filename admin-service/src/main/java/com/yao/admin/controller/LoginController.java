package com.yao.admin.controller;

import com.yao.admin.dto.req.AccountLoginReqDTO;
import com.yao.admin.dto.resp.AccountLoginRespDTO;
import com.yao.admin.service.AdminLoginService;
import com.yao.base.framework.common.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final AdminLoginService adminLoginService;

    public LoginController(AdminLoginService adminLoginService) {
        this.adminLoginService = adminLoginService;
    }

    @PostMapping("/account")
    public Result<AccountLoginRespDTO> accountLogin(
            @RequestBody(required = false) AccountLoginReqDTO request) {
        return Result.success(adminLoginService.login(request));
    }
}
