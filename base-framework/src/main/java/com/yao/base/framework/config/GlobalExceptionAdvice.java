package com.yao.base.framework.config;


import com.yao.base.framework.common.Result;
import com.yao.base.framework.common.enums.ResponseCodeEnum;
import com.yao.base.framework.expection.BusinessException;
import com.yao.base.framework.expection.ClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionAdvice {

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handlerLoginException(BusinessException e) {
        return Result.fail(e.getErrorCode().getCode(), e.getErrorCode().getMessage());
    }

    @ExceptionHandler(ClientException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handlerClientException(ClientException e) {
        return Result.fail(e.getErrorCode().getCode(), e.getErrorCode().getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handlerException(Exception e) {
        log.error("Unhandled system exception", e);
        return Result.fail(ResponseCodeEnum.FILE);
    }
}
