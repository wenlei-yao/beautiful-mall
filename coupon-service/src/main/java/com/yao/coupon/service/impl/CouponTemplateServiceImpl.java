package com.yao.coupon.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yao.base.framework.common.enums.ResponseCodeEnum;
import com.yao.base.framework.expection.BusinessException;
import com.yao.coupon.constant.CouponRedisConstant;
import com.yao.coupon.dao.entity.CouponTemplateDO;
import com.yao.coupon.dto.req.CouponTemplateReqDTO;
import com.yao.coupon.dto.resp.CouponTemplateQueryRespDTO;
import com.yao.coupon.mapper.CouponTemplateMapper;
import com.yao.coupon.service.CouponTemplateService;
import lombok.AllArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class CouponTemplateServiceImpl extends ServiceImpl<CouponTemplateMapper, CouponTemplateDO> implements CouponTemplateService {


    private final RedissonClient redissonClient;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public CouponTemplateQueryRespDTO findTemplate(CouponTemplateReqDTO couponTemplateReqDTO) {
        String couponTemplateCacheKey = String.format(CouponRedisConstant.COUPON_TEMPLATE_KEY, couponTemplateReqDTO.getTemplateId());
        Map<Object, Object> couponTemplateCacheMap = stringRedisTemplate.opsForHash().entries(couponTemplateCacheKey);

        if (MapUtil.isEmpty(couponTemplateCacheMap)) {
            String templateCacheNullKey = String.format(CouponRedisConstant.COUPON_TEMPLATE_NULL_KEY, couponTemplateReqDTO.getTemplateId());
            if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(templateCacheNullKey))) {
                throw new BusinessException(ResponseCodeEnum.TEMPLATE_ID_NULL);
            }

            RLock lock = redissonClient.getLock(String.format(CouponRedisConstant.COUPON_TEMPLATE_LOCK_KEY, couponTemplateReqDTO.getTemplateId()));
            lock.lock();

            try {
                LambdaQueryWrapper<CouponTemplateDO> lambdaQueryWrapper = Wrappers.lambdaQuery(CouponTemplateDO.class)
                        .eq(CouponTemplateDO::getId, couponTemplateReqDTO.getTemplateId());
                CouponTemplateDO couponTemplateDO = this.baseMapper.selectOne(lambdaQueryWrapper);
                if (couponTemplateDO == null) {
                    stringRedisTemplate.opsForValue().set(templateCacheNullKey, "", 20, TimeUnit.SECONDS);
                    throw new BusinessException(ResponseCodeEnum.TEMPLATE_ID_NULL);
                }

                CouponTemplateQueryRespDTO actualCacheDTO = BeanUtil.toBean(couponTemplateDO, CouponTemplateQueryRespDTO.class);
                Map<String, Object> cacheTargetMap = BeanUtil.beanToMap(actualCacheDTO, false, true);
                Map<String, String> actualCacheTargetMap = cacheTargetMap.entrySet().stream().collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getKey() != null ? entry.getValue().toString() : ""
                ));
                List<String> keys = Collections.singletonList(couponTemplateCacheKey);
                List<String> args = new ArrayList<>();

                actualCacheTargetMap.forEach(
                        (key, value) -> {
                            args.add(key);
                            args.add(value);
                        }
                );
                //模板先不设置过期时间 后期优化
                String luaScript = "redis.call('HMSET', KEYS[1], unpack(ARGV, 1, #ARGV))";
                stringRedisTemplate.execute(
                        new DefaultRedisScript<>(luaScript, Long.class),
                        keys,
                        args.toArray()
                );
                return actualCacheDTO;
            } finally {
                lock.unlock();
            }
        }

        return BeanUtil.mapToBean(couponTemplateCacheMap, CouponTemplateQueryRespDTO.class, false, CopyOptions.create());
    }
}