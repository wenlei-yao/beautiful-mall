package com.yao.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yao.coupon.dao.entity.CouponActivityDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 优惠券活动 Mapper 接口
 */
@Mapper
public interface CouponActivityMapper extends BaseMapper<CouponActivityDO> {

    int decrementCouponActivityStock(
            @Param("activityId") Long activityId,@Param("decrementNum")Long decrementNum
    );
}