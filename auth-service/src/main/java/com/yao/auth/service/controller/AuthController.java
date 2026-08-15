package com.yao.auth.service.controller;


import com.yao.auth.service.dto.request.LoginRequest;
import com.yao.auth.service.dto.response.LoginResponse;
import com.yao.auth.service.service.AuthService;
import com.yao.base.framework.common.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return Result.success(authService.login(loginRequest));
    }
}
