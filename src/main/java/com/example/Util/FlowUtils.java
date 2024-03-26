package com.example.Util;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class FlowUtils {
    //限流工具类

    @Resource
    StringRedisTemplate template;


    //针对单次请求限制，blockTime s/次
    public boolean limitOnceCheck(String key,long blockTime){
        if(Boolean.TRUE.equals(template.hasKey(key))){      //冷却状态
            return false;   // 冷却
        }else{      // 非冷却状态
            template.opsForValue().set(key,"",blockTime, TimeUnit.SECONDS);
            // 如果没有冷却，即可以发送，则丢一个冷却键进去
            return true;
        }
    }
}
