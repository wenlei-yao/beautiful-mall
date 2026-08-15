package com.yao.user.service;

import com.yao.user.dto.req.PasswordVerifyReqDTO;
import com.yao.user.dto.resp.PasswordVerifyRespDTO;

/**
 * 用户凭证校验 Service。
 */
public interface UserCredentialService {

    PasswordVerifyRespDTO verifyPassword(PasswordVerifyReqDTO request);
}
