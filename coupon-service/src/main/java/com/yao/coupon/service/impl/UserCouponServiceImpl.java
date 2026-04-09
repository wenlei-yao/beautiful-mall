package com.yao.coupon.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Singleton;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yao.base.framework.common.enums.ResponseCodeEnum;
import com.yao.base.framework.expection.BusinessException;
import com.yao.coupon.constant.CouponRedisConstant;
import com.yao.coupon.dao.entity.CouponTemplateDO;
import com.yao.coupon.dao.entity.UserCouponDO;
import com.yao.coupon.dto.req.CouponActivityReqDTO;
import com.yao.coupon.dto.req.CouponTemplateReqDTO;
import com.yao.coupon.dto.req.ReceiveCouponReqDTO;
import com.yao.coupon.dto.resp.CouponActivityQueryRespDTO;
import com.yao.coupon.dto.resp.CouponTemplateQueryRespDTO;
import com.yao.coupon.enums.CouponValidTypeEnum;
import com.yao.coupon.enums.RedisStockDecrementErrorEnum;
import com.yao.coupon.enums.UserCouponStatusEnum;
import com.yao.coupon.mapper.CouponActivityMapper;
import com.yao.coupon.mapper.UserCouponMapper;
import com.yao.coupon.service.CouponActivityService;
import com.yao.coupon.service.CouponTemplateService;
import com.yao.coupon.service.UserCouponService;
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
 * 用户优惠券 Service 实现类
 */
@Service
@AllArgsConstructor
public class UserCouponServiceImpl extends ServiceImpl<UserCouponMapper, UserCouponDO> implements UserCouponService {


    private final static String STOCK_DECREMENT_AND_SAVE_USER_RECEIVE_LUA_PATH = "lua/stock_decrement_and_save_user_receive.lua";

    private final CouponActivityService couponActivityService;
    private final CouponTemplateService couponTemplateService;

    private final CouponActivityMapper couponActivityMapper;
    private final UserCouponMapper userCouponMapper;

    private final StringRedisTemplate stringRedisTemplate;
    private final PlatformTransactionManager transactionManager;


    @Override
    public void receiveCoupon( ReceiveCouponReqDTO receiveCouponReqDto) {
        CouponActivityQueryRespDTO couponActivity = couponActivityService.findCouponActivity(BeanUtil.copyProperties(receiveCouponReqDto, CouponActivityReqDTO.class));
        boolean isInTime = LocalDateTimeUtil.isIn(LocalDateTime.now(), couponActivity.getStartTime(), couponActivity.getEndTime());
        if (!isInTime) {
            throw new BusinessException(ResponseCodeEnum.ACTIVITY_UN_START);
        }

        CouponTemplateQueryRespDTO couponTemplate = couponTemplateService.findTemplate(new CouponTemplateReqDTO(couponActivity.getTemplateId()));
        Integer userLimit = couponTemplate.getUserLimit();
        String couponActivityCacheKey = String.format(CouponRedisConstant.COUPON_ACTIVITY_KEY, receiveCouponReqDto.getActivityId());
        String couponTemplateLimitCacheKey = String.format(CouponRedisConstant.COUPON_USER_ACTIVITY_LIMIT, receiveCouponReqDto.getUserId(), receiveCouponReqDto.getActivityId());

        DefaultRedisScript<Long> buildLuaScript = Singleton.get(STOCK_DECREMENT_AND_SAVE_USER_RECEIVE_LUA_PATH, () -> {
            DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
            redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(STOCK_DECREMENT_AND_SAVE_USER_RECEIVE_LUA_PATH)));
            redisScript.setResultType(Long.class);
            return redisScript;
        });

        Long stockDecrementLuaResult = stringRedisTemplate.execute(
                buildLuaScript,
                Arrays.asList(couponActivityCacheKey, couponTemplateLimitCacheKey),
                String.valueOf(userLimit),
                String.valueOf(couponActivity.getEndTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
        );
        if (RedisStockDecrementErrorEnum.isFail(stockDecrementLuaResult)) {
            throw new BusinessException(ResponseCodeEnum.TEMPLATE_DECREMENT_FAIL, RedisStockDecrementErrorEnum.fromType(stockDecrementLuaResult));
        }

        //通过编程式事务，扣减优惠券数量并增加用户优惠券
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            int decremented = couponActivityMapper.decrementCouponActivityStock(receiveCouponReqDto.getActivityId(), 1L);
            if (decremented == 0) {
                throw new BusinessException(ResponseCodeEnum.TEMPLATE_DECREMENT_FAIL, "优惠券模板被抢完啦");
            }
            CouponValidTypeEnum.TimeRange CouponValidTime = CouponValidTypeEnum
                    .fromCode(couponTemplate.getValidType())
                    .calculate(BeanUtil.copyProperties(couponTemplate, CouponTemplateDO.class), LocalDateTime.now());

            UserCouponDO userCouponDO = UserCouponDO.builder()
                    .couponCode(UUID.randomUUID().toString().replace("-",""))
                    .userId(receiveCouponReqDto.getUserId())
                    .templateId(couponTemplate.getId())
                    .activityId(couponActivity.getId())
                    .status(UserCouponStatusEnum.UNUSED.getCode())
                    .receiveTime(LocalDateTime.now())
                    .validStartTime(CouponValidTime.getStart())
                    .validEndTime(CouponValidTime.getEnd())
                    .build();
            userCouponMapper.insert(userCouponDO);
            //todo 记录用户领券记录到 redis
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
    }
}