-- 和优惠券领取 lua 脚本
-- KEY[1] 库存 KEY
-- KEY[2] 用户 KEY
-- ARGV[1] 用户限领数量
-- ARGV[2]: 优惠券有效期结束时间 (timestamp)
local stockKey = KEYS[1]
local userKey = KEYS[2]
local userLimit = tonumber(ARGV[1])
local expireTime = tonumber(ARGV[2])

-- 检查库存
local stock = tonumber(redis.call('HGET',stockKey,'remainingCount') or 0)
if stock <= 0 then
    return -1
end

-- 获取用户领取优惠券的数量
local userCouponCount = tonumber(redis.call('GET',userKey) or 0)

-- 判断用户是否已经达到领取上限
if userCouponCount >= userLimit then
    return -2
end

-- 增加用户领取优惠券的数量
redis.call('INCR',userKey)
redis.call('EXPIREAT',userKey,expireTime)

-- 扣减优惠券的库存
redis.call("HINCRBY",stockKey,'remainingCount',-1)

-- 返回剩余库存
return stock - 1
