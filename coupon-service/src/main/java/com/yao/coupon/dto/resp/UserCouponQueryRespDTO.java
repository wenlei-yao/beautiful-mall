package com.yao.coupon.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCouponQueryRespDTO {

    List<UserCouponDetailRespDTO> availableCouponList;

    List<UserCouponDetailRespDTO> unavailableCouponList;

}
