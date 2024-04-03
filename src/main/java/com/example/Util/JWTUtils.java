package com.example.Util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.Service.HalihapiUserDetails;
import com.example.Service.Impl.HalihapiUser;
import com.example.Service.JwtBlacklistService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;

//JWT令牌工具类
@Component
@Slf4j
public class JWTUtils {
    @Resource
    JwtBlacklistService jwtBlacklistService;
    //JWT密钥，自定义，后面会经过HMAC256的加密算法进行加密组合，不可泄露！
    @Value("${spring.security.jwt.key}")
    String JWTKey;
    //令牌过期时间
    @Value("${spring.security.jwt.expire}")
    int expire;

    @Resource
    StringRedisTemplate template;

    public Date expireTime;


    //根据用户的信息，创建JWT令牌
    public String CreateJWT(HalihapiUserDetails halihapiUserDetails){
        // 创建算法对象，用于加密，验证签名
        Algorithm algorithm =Algorithm.HMAC256(JWTKey);
        //设置令牌失效时间
        expireTime = this.expireTime();

        //创建令牌
        return JWT.create()
                .withJWTId(UUID.randomUUID().toString())        //唯一的UUID，用于黑名单存储
                .withClaim("userID",halihapiUserDetails.getUserID())
                .withClaim("name",halihapiUserDetails.getUsername())    //配置JWT自定义信息
                .withClaim("authorities",halihapiUserDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .withExpiresAt(expireTime)      //失效时间
                .withIssuedAt(new Date())                  //签发时间 //获取当前的日期时间
                .sign(algorithm);
    }

    public Date expireTime(){
        Calendar calendar =Calendar.getInstance();          //获取Cal实例对象
        calendar.add(Calendar.SECOND,3600 * 24 * expire);   //设置过期时间
        return calendar.getTime();
    }

    //解析JWT信息,此处使用auth0框架下的JWT解码机制
    public DecodedJWT resolveJWT(String token){
        Algorithm algorithm = Algorithm.HMAC256(JWTKey);    //获取JWTkey的转化码
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();   //验证
       try {
           DecodedJWT verify = jwtVerifier.verify(token);       //验证是否被串改过
           // 验证成功，检查 token 是否在黑名单中
           return isBlackListed(token)?null:verify;
       }catch (JWTVerificationException e){
           return null;
       }
    }

    //加入黑名单
    public boolean invalidateJWT(String token){
        // 防止令牌为null
        if (token == null) return false;
        if(isBlackListed(token)) return true;  // 如果令牌已在黑名单内，不用再添加黑名单，直接算作退出成功
        addToBlacklist(token);
        return true;
    }

    //判断请求头的合法性
    public String convertToken(String headerToken){
        if(headerToken == null || !headerToken.startsWith("Bearer "))
            return null;
        return headerToken.substring(7);
    }

    //专门用于解析JWT的UserDetails，重新封装
    public HalihapiUserDetails toUserDetails(DecodedJWT jwt){
        Map<String, Claim> claims = jwt.getClaims();
        UserDetails user = User
                .withUsername(claims.get("name").toString())
                .password("*")
                .authorities(claims.get("authorities").asArray(String.class))
                .build();
        int userID = claims.get("userID").asInt();
        return new HalihapiUser(user,userID);
    }

    private boolean isBlackListed(String token){
        return jwtBlacklistService.isBlacklisted(token);
    }
    private void addToBlacklist(String token){
        jwtBlacklistService.addToBlacklist(token);
    }
}
