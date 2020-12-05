package com.tk.sz.service.impl;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tk.sz.service.IRedisService;

@Service
@Transactional
public class RedisServiceImpl implements IRedisService {

	@Resource
	private RedisTemplate redisTemplate;

	@Override
	public void setKey(String key, String value) {
		ValueOperations<String, String> ops = redisTemplate.opsForValue();
		ops.set(key, value, 300, TimeUnit.SECONDS);// 5分钟过期:   5 * 60S
	}

	@Override
	public String getValue(String key) {
		ValueOperations<String, String> ops = redisTemplate.opsForValue();
		return ops.get(key);
	}

	@Override
	public void delete(String key) {
		redisTemplate.delete(key);
	}

}
