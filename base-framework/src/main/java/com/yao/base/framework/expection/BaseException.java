package com.yao.base.framework.expection;


import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {

    private final ErrorCode errorCode;

    protected BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    protected BaseException(ErrorCode errorCode, String customMessage) {
        super(resolveErrorCode(errorCode, customMessage).getMessage());
        this.errorCode = resolveErrorCode(errorCode, customMessage);
    }

    protected BaseException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

    public BaseException(ErrorCode errorCode, String s, Throwable cause) {
        super(s, cause);
        this.errorCode = errorCode;
    }

    private static ErrorCode resolveErrorCode(ErrorCode errorCode, String customMessage) {
        if (customMessage == null || customMessage.isEmpty()) {
            return errorCode;
        }
        return new ErrorCode() {
            @Override
            public int getCode() {
                return errorCode.getCode();
            }

            @Override
            public String getMessage() {
                return customMessage;
            }
        };
    }
}
