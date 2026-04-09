package com.yao.coupon.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Singleton;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yao.base.framework.common.enums.ResponseCodeEnum;
import com.yao.base.framework.expection.BusinessException;
import com.yao.coupon.constant.CouponRedisConstant;
import com.yao.coupon.dao.entity.CouponReceiveRecordDO;
import com.yao.coupon.dao.entity.CouponTemplateDO;
import com.yao.coupon.dao.entity.UserCouponDO;
import com.yao.coupon.dto.req.CouponActivityReqDTO;
import com.yao.coupon.dto.req.CouponTemplateReqDTO;
import com.yao.coupon.dto.resp.CouponActivityQueryRespDTO;
import com.yao.coupon.dto.resp.CouponTemplateQueryRespDTO;
import com.yao.coupon.enums.CouponValidTypeEnum;
import com.yao.coupon.enums.RedisStockDecrementErrorEnum;
import com.yao.coupon.enums.UserCouponStatusEnum;
import com.yao.coupon.mapper.CouponActivityMapper;
import com.yao.coupon.mapper.CouponReceiveRecordMapper;
import com.yao.coupon.dto.req.ReceiveCouponReqDTO;
import com.yao.coupon.service.*;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.UUID;

/**
 * 领券记录 Service 实现类
 */
@AllArgsConstructor
@Service
public class CouponReceiveRecordServiceImpl extends ServiceImpl<CouponReceiveRecordMapper, CouponReceiveRecordDO> implements CouponReceiveRecordService {

}