package com.yao.auth.service.service;

import com.yao.auth.service.dto.request.LoginRequest;
import com.yao.auth.service.dto.response.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest loginRequest);
}
