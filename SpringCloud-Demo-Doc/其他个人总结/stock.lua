local key = KEYS[1]
local stock = KEYS[2]
local lockTimeOut = ARGV[2]

-- 检测库存配置
local stockObj = redis.call("GET",stock);
if stockObj == nil then
	return -1
end

--判断库存
local stockNum = tonumber(stockObj)
if nil == stockNum or stockNum <= 0 then
	--库存不足
	return -1
end

-- 锁定成功，如果key不存在则添加 (SET if Not eXists)缩写
if redis.call("SETNX", key, 0) == 1 then
    -- 设置过期时间
    redis.call("EXPIRE", key, lockTimeOut)
    local stockNum = tonumber(redis.call("GET",stock))
    if nil ~= stockNum and stockNum > 0 then
		--库存减1
		local currentNum = redis.call("DECRBY", stock , 1)
		-- 库存不存在，返回库存不足
		if currentNum == -1 then
			-- 释放锁
			redis.call("DEL",key)
			return -1
		end
		-- 获得库存 释放锁
		redis.call("DEL",key)
	else
		-- 库存不足，释放锁
		redis.call("DEL",key)
		return -1
    end
    return 1
elseif redis.call("TTL", key) == -1 then
    redis.call("EXPIRE", key, lockTimeOut)
end
-- 已锁定
return 0