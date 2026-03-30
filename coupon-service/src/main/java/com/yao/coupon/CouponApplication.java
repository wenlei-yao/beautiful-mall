package com.yao.coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 优惠券服务启动类
 * @author ywl
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.yao.base.framework","com.yao"})
public class CouponApplication {
    public static void main(String[] args) {
        SpringApplication.run(CouponApplication.class, args);
    }
}
