package com.yao.coupon.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Singleton;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yao.base.framework.common.enums.ResponseCodeEnum;
import com.yao.base.framework.expection.BusinessException;
import com.yao.coupon.constant.CouponRedisConstant;
import com.yao.coupon.dao.entity.CouponTemplateDO;
import com.yao.coupon.dao.entity.UserCouponDO;
import com.yao.coupon.dto.req.CouponActivityReqDTO;
import com.yao.coupon.dto.req.CouponTemplateReqDTO;
import com.yao.coupon.dto.req.ReceiveCouponReqDTO;
import com.yao.coupon.dto.req.UserCouponQueryReqDTO;
import com.yao.coupon.dto.resp.*;
import com.yao.coupon.enums.CouponCategoryEnum;
import com.yao.coupon.enums.CouponValidTypeEnum;
import com.yao.coupon.enums.RedisStockDecrementErrorEnum;
import com.yao.coupon.enums.UserCouponStatusEnum;
import com.yao.coupon.mapper.CouponActivityMapper;
import com.yao.coupon.mapper.UserCouponMapper;
import com.yao.coupon.order.dto.OrderItemDTO;
import com.yao.coupon.service.CouponActivityService;
import com.yao.coupon.service.CouponTemplateService;
import com.yao.coupon.service.UserCouponService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
            //计算优惠券使用时间
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

    @Override
    public UserCouponQueryRespDTO listUserCoupon(UserCouponQueryReqDTO userCouponQueryReqDTO) {

        LambdaQueryWrapper<UserCouponDO> queryWrapper = Wrappers.lambdaQuery(UserCouponDO.class)
                .eq(UserCouponDO::getStatus, UserCouponStatusEnum.UNUSED)
                .eq(UserCouponDO::getUserId, userCouponQueryReqDTO.getUserId());
        List<UserCouponDO> unUsedCoupons = userCouponMapper.selectList(queryWrapper);

        List<Long> couponTemplateIds = unUsedCoupons.stream().map(UserCouponDO::getTemplateId)
                .collect(Collectors.toList());
        //hGetAll 返回的 Map<byte[],byte[]>，但由于 StringRedisTemplate 会自动反序列化，实际上 rawCouponDataList 中的每个元素是 Map<String, String>（即 field-value 都是字符串）。
        List<Object> rawCouponDataList = stringRedisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            couponTemplateIds.forEach(each -> connection
                    .hashCommands().hGetAll(String.format(CouponRedisConstant.COUPON_TEMPLATE_KEY, each)
                            .getBytes(StandardCharsets.UTF_8)));
            return null;
        });

        List<CouponTemplateQueryRespDTO> couponTemplateCacheList = JSON.parseArray(JSON.toJSONString(rawCouponDataList), CouponTemplateQueryRespDTO.class);
        Map<Long, CouponTemplateQueryRespDTO> couponTemplateMap = couponTemplateCacheList.stream().collect(Collectors.toMap(CouponTemplateQueryRespDTO::getId, Function.identity()));
        //计算优惠券的使用
        List<UserCouponDetailRespDTO> discountCoupons = unUsedCoupons
                .stream().map(userCoupon -> calculateDiscountCoupon(couponTemplateMap.get(userCoupon.getTemplateId()), userCoupon, userCouponQueryReqDTO.getOrderItems()))
                .collect(Collectors.toList());

        Map<Boolean, List<UserCouponDetailRespDTO>> partitioned = discountCoupons.stream()
                .collect(Collectors.partitioningBy(UserCouponDetailRespDTO::getAvailable));

        List<UserCouponDetailRespDTO> availableCoupons = partitioned.getOrDefault(true, Collections.emptyList())
                .stream()
                .sorted(Comparator.comparing(
                        UserCouponDetailRespDTO::getCouponAmount,
                        Comparator.nullsLast(Comparator.reverseOrder())
                ))
                .collect(Collectors.toList());

        List<UserCouponDetailRespDTO> unavailableCoupons = partitioned.getOrDefault(false, Collections.emptyList());

        return UserCouponQueryRespDTO.builder()
                .availableCouponList(availableCoupons)
                .unavailableCouponList(unavailableCoupons)
                .build();
    }

    /**
     * 计算可用优惠券
     */
    public UserCouponDetailRespDTO calculateDiscountCoupon(
            CouponTemplateQueryRespDTO couponTemplate,UserCouponDO userCouponDO,List<OrderItemDTO> orderItems) {

        UserCouponDetailRespDTO userCouponDetailRespDTO = new UserCouponDetailRespDTO();
        BeanUtil.copyProperties(userCouponDO, userCouponDetailRespDTO);
        //todo 计算优惠券适用的商品

        //校验使用门槛
        BigDecimal totalAmount = orderItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (totalAmount.compareTo(couponTemplate.getThreshold()) < 0) {
            userCouponDetailRespDTO.setAvailable(false);
            userCouponDetailRespDTO.setUnavailableReason(String.format("商品未到%.2f元",couponTemplate.getThreshold()));
            return userCouponDetailRespDTO;
        }
        //校验优惠券是否在试用期内
        if (!LocalDateTimeUtil.isIn(LocalDateTimeUtil.now(),userCouponDO.getValidStartTime(),userCouponDO.getValidEndTime())) {
            userCouponDetailRespDTO.setAvailable(false);
            userCouponDetailRespDTO.setUnavailableReason("未到使用时间");
            return userCouponDetailRespDTO;
        }
        BigDecimal discountAmount = calculateDiscount(couponTemplate,totalAmount);
        userCouponDetailRespDTO.setCouponAmount(discountAmount);
        userCouponDetailRespDTO.setAvailable(true);
        return userCouponDetailRespDTO;
    }

    private BigDecimal calculateDiscount(CouponTemplateQueryRespDTO couponTemplate, BigDecimal totalAmount) {
        CouponCategoryEnum couponCategoryEnum = CouponCategoryEnum.of(couponTemplate.getCategory());

        switch (couponCategoryEnum) {
            //满减 立减
            case FULL_REDUCTION:
            case IMMEDIATE_REDUCTION:
                return couponTemplate.getDiscountAmount();
            case DISCOUNT:
                return totalAmount.
                        multiply(BigDecimal.ONE.subtract(couponTemplate.getDiscountRate()))
                        .setScale(2, RoundingMode.HALF_UP);
            default:
                return BigDecimal.ZERO;
        }
    }
}