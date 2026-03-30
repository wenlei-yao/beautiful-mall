package com.yao.base.framework.config;


import com.yao.base.framework.common.Result;
import com.yao.base.framework.common.enums.ResponseCodeEnum;
import com.yao.base.framework.expection.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionAdvice {

    @ExceptionHandler(BusinessException.class)
    public Result<Void> handlerLoginException(BusinessException e) {
        log.error("业务异常", e);
        return Result.fail(e.getErrorCode().getCode(), e.getErrorCode().getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handlerException(Exception e) {
        return Result.fail(ResponseCodeEnum.FILE);
    }
}
