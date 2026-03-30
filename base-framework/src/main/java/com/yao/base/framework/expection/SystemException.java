package com.yao.base.framework.expection;

public class SystemException extends BaseException {
    public SystemException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public SystemException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message != null ? message : errorCode.getMessage(), cause);
    }
}