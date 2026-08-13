package com.yao.base.framework.expection;

public class ClientException extends BaseException {

    public ClientException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ClientException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * 使用远程服务返回的动态错误码和错误消息创建客户端异常。
     */
    public ClientException(int code, String message) {
        super(new ErrorCode() {
            @Override
            public int getCode() {
                return code;
            }

            @Override
            public String getMessage() {
                return message;
            }
        });
    }
}
