package com.yao.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yao.coupon.dao.entity.CouponUseRecordDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券使用记录 Mapper 接口
 */
@Mapper
public interface CouponUseRecordMapper extends BaseMapper<CouponUseRecordDO> {
}