package com.yao.base.framework.expection;

public class ClientException extends BaseException {
    public ClientException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ClientException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}