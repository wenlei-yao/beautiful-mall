package com.yao.coupon;

import com.yao.coupon.entity.CouponActivity;
import com.yao.coupon.service.CouponActivityService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootApplicationTests {

    @Autowired
    private CouponActivityService userService;

    @Test
    public void testAddUser() {
        CouponActivity byId = userService.getById(1L);
        System.out.println(byId);
    }

}