package com.example.Util;

import com.example.Service.HalihapiUserDetails;
import com.example.Service.Impl.HalihapiUser;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Component
@Slf4j
public class CommonUtils {
    @Resource
    RedisTemplate<String,Object> redisTemplate;
    public Integer randomFiveCode(){return new Random().nextInt(899999) + 100000;}
    public String getUUID(){
        return UUID.randomUUID().toString();
    }

    public String getHashMapKeyValue(String key,String hashKey,String mapKey){
        HashOperations<String,String,Object> hashMap = redisTemplate.opsForHash();
        Map<String,Object> map = (Map<String, Object>) hashMap.get(key,hashKey);
        if (map != null) return (String) map.get(mapKey);
        return null;
    }

    public boolean changeHashJSONValue(String key,String hashKey,String mapKey,String newValue) throws Exception {
        HashOperations<String, String, Object> hashOps = redisTemplate.opsForHash();
        Map<String, Object> map = (Map<String, Object>) hashOps.get(key, hashKey); // 获取哈希键中的 Map 对象
        if (map != null) {
            map.put(mapKey, newValue); // 修改 Map 对象中的某个键值对
            hashOps.put(key, hashKey, map); // 更新哈希键中的 Map 对象
            return true;
        }else throw new Exception("请求参数异常");
    }

    public String getAuthenticationUserNameOrEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null && authentication.isAuthenticated()){
            User user =(User) authentication.getPrincipal();
            String usernameOrEmail = user.getUsername().replace("\"", "");;
            return usernameOrEmail;
        }else return "";
    }

    public Integer getAuthenticationUserID() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null && authentication.isAuthenticated()){
            HalihapiUserDetails user = (HalihapiUserDetails) authentication.getPrincipal();
            return user.getUserID();
        }else {
            throw new Exception("内部参数错误");
        }
    }
}
