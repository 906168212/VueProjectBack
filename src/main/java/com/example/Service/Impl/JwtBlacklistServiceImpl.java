package com.example.Service.Impl;

import com.example.Service.JwtBlacklistService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class JwtBlacklistServiceImpl implements JwtBlacklistService {
    private static final String BLACKLIST_KEY = "jwt:blacklist";
    private static final long BLACKLIST_TOKEN_EXPIRE_TIME = 3600 * 24 * 3;  // 3 days

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public void addToBlacklist(String token) {
        // 将 token 存储到黑名单中，值为当前时间戳
        stringRedisTemplate.opsForValue().set(BLACKLIST_KEY+token,String.valueOf(System.currentTimeMillis()),BLACKLIST_TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
    }

    @Override
    public boolean isBlacklisted(String token) {
        Boolean isBlackListed = stringRedisTemplate.hasKey(BLACKLIST_KEY+token);
        return isBlackListed != null && isBlackListed;
    }
}
