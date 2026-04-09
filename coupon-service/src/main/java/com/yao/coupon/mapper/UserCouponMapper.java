package com.yao.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yao.coupon.dao.entity.UserCouponDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户优惠券 Mapper 接口
 */
@Mapper
public interface UserCouponMapper extends BaseMapper<UserCouponDO> {
}