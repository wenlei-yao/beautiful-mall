package com.yao.auth.service.dto.request;

import lombok.Data;

@Data
public class LoginRequest {

    private String loginType;

    private PasswordParam passwordParam;

    private SmsParam smsParam;

    private WechatParam wechatParam;


    @Data
    public static class PasswordParam{
        private String username;
        private String password;
    }
    @Data
    public static class SmsParam{
        private String phone;
        private String code;
    }

    @Data
    public static class WechatParam{
        private String code;
    }

}
