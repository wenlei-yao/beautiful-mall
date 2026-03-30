package com.yao.coupon.controller;

import com.yao.base.framework.common.Result;
import com.yao.base.framework.common.enums.ResponseCodeEnum;
import com.yao.base.framework.expection.BusinessException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("/test")
    public Result<String> test(){
        throw new BusinessException(ResponseCodeEnum.DUPLICATE_SUBMIT);
    }
}
