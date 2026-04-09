package com.yao.coupon.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 领券记录表实体
 */
@Data
@TableName("coupon_receive_record")
public class CouponReceiveRecordDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long templateId;

    private Long activityId;

    private Integer receiveCount;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}