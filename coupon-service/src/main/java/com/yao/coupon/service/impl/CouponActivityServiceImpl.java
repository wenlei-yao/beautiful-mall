package com.yao.coupon.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yao.base.framework.common.enums.ResponseCodeEnum;
import com.yao.base.framework.expection.BusinessException;
import com.yao.coupon.constant.CouponRedisConstant;
import com.yao.coupon.dao.entity.CouponActivityDO;
import com.yao.coupon.dto.req.CouponActivityReqDTO;
import com.yao.coupon.dto.resp.CouponActivityQueryRespDTO;
import com.yao.coupon.mapper.CouponActivityMapper;
import com.yao.coupon.service.CouponActivityService;
import lombok.AllArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 优惠券活动 Service 实现类
 */
@Service
@AllArgsConstructor
public class CouponActivityServiceImpl extends ServiceImpl<CouponActivityMapper, CouponActivityDO> implements CouponActivityService {

    private final static String SET_VALUES_FOR_HASE_AND_EXPIREAT_LUA_PATH = "lua/set_values_for_hash_and_expireat.lua";


    private final StringRedisTemplate stringRedisTemplate;
    private final RedissonClient redissonClient;
    @Override
    public CouponActivityQueryRespDTO findCouponActivity(CouponActivityReqDTO CouponActivityReqDTO) {

        //1.查询缓存是否有值
        String couponActivityCacheKey = String.format(CouponRedisConstant.COUPON_ACTIVITY_KEY, CouponActivityReqDTO.getActivityId());
        Map<Object, Object> activityCacheMap = stringRedisTemplate.opsForHash().entries(couponActivityCacheKey);

        if (MapUtil.isEmpty(activityCacheMap)){
            String couponActivityNullCache = String.format(CouponRedisConstant.COUPON_ACTIVITY_NULL_KEY, CouponActivityReqDTO.getActivityId());
            if(Boolean.TRUE.equals(stringRedisTemplate.hasKey(couponActivityNullCache))){
                throw new BusinessException(ResponseCodeEnum.ACTIVITY_IS_NULL);
            }

            RLock lock = redissonClient.getLock(String.format(CouponRedisConstant.COUPON_ACTIVITY_LOCK_KEY, CouponActivityReqDTO.getActivityId()));
            lock.lock();

            try {
                //2.查询数据库活动信息
                LambdaQueryWrapper<CouponActivityDO> lambdaQueryWrapper = Wrappers.lambdaQuery(CouponActivityDO.class)
                        .eq(CouponActivityDO::getId, CouponActivityReqDTO.getActivityId());
                CouponActivityDO couponActivityDO = this.baseMapper.selectOne(lambdaQueryWrapper);
                if (couponActivityDO == null) {
                    // 3.缓存控制防止缓存击穿
                    stringRedisTemplate.opsForValue().set(couponActivityNullCache,"",30, TimeUnit.SECONDS);
                    throw new BusinessException(ResponseCodeEnum.ACTIVITY_IS_NULL);
                }
                CouponActivityQueryRespDTO actualCacheDTO = BeanUtil.toBean(couponActivityDO, CouponActivityQueryRespDTO.class);
                Map<String, Object> cacheTargetMap = BeanUtil.beanToMap(actualCacheDTO, false, true);
                Map<String, String> actualCacheTargetMap = cacheTargetMap.entrySet().stream().collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getKey() != null ? entry.getValue().toString() : ""
                ));
                List<String> keys = Collections.singletonList(couponActivityCacheKey);
                List<String> args = new ArrayList<>();
                actualCacheTargetMap.forEach(
                        (key, value) -> {
                            args.add(key);
                            args.add(value);
                        }
                );
                long cacheExpireTime = actualCacheDTO.getEndTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                args.add(String.valueOf(cacheExpireTime));

                // 获取 LUA 脚本，并保存到 Hutool 的单例管理容器，下次直接获取不需要加载
                DefaultRedisScript<Long> buildLuaScript = Singleton.get(SET_VALUES_FOR_HASE_AND_EXPIREAT_LUA_PATH, () -> {
                    DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
                    redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(SET_VALUES_FOR_HASE_AND_EXPIREAT_LUA_PATH)));
                    redisScript.setResultType(Long.class);
                    return redisScript;
                });
                stringRedisTemplate.execute(
                        buildLuaScript,
                        keys,
                        args.toArray()
                );
                return actualCacheDTO;
            }finally {
                lock.unlock();
            }

        }

        return BeanUtil.mapToBean(activityCacheMap,CouponActivityQueryRespDTO.class,false, CopyOptions.create());
    }
}