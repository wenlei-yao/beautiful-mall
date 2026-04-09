package com.yao.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yao.coupon.dao.entity.CouponReceiveRecordDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 领券记录 Mapper 接口
 */
@Mapper
public interface CouponReceiveRecordMapper extends BaseMapper<CouponReceiveRecordDO> {
}