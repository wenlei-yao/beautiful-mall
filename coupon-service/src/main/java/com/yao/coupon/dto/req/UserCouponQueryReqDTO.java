package com.yao.coupon.dto.req;

import com.yao.coupon.order.dto.OrderItemDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserCouponQueryReqDTO {

    private Long userId;

    List<OrderItemDTO> orderItems;

}
