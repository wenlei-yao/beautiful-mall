package com.yao.admin.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.yao.admin.dto.resp.CurrentAdminRespDTO;
import com.yao.admin.service.AdminCurrentUserService;
import com.yao.base.framework.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final AdminCurrentUserService adminCurrentUserService;

    public UserController(AdminCurrentUserService adminCurrentUserService) {
        this.adminCurrentUserService = adminCurrentUserService;
    }

    @GetMapping("/me")
    public Result<CurrentAdminRespDTO> getCurrentAdmin() {
        Long adminId = StpUtil.getLoginIdAsLong();
        return Result.success(adminCurrentUserService.getCurrentAdmin(adminId));
    }
}
