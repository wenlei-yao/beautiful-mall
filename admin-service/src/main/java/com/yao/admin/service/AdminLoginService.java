package com.yao.admin.service;

import com.yao.admin.dto.req.AccountLoginReqDTO;
import com.yao.admin.dto.resp.AccountLoginRespDTO;

/**
 * 管理员登录 Service。
 */
public interface AdminLoginService {

    AccountLoginRespDTO login(AccountLoginReqDTO request);
}
