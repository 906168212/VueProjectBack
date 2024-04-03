package com.example.Service.Impl;

import com.example.Entity.RestBeanNew;
import com.example.Entity.dto.Account;
import com.example.Repo.AccountRepository;
import com.example.Service.AccountService;
import com.example.Util.CommonUtils;
import com.example.Util.Const;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AccountServiceImpl implements UserDetailsService, AccountService {

    @Resource
    AccountRepository repository;
    @Resource
    CommonUtils utils;

    @Resource
    RedisTemplate<String,Map<String,Object>> redisTemplate;
    @Value("${spring.reset.expireTime}")
    long resetPassword_account_expireTime;
    private final String reset_passwordKey = Const.RESET_PASSWORD_ACCOUNT+":password";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = repository.findAccountByUsernameOrEmail(username,username);
        if (account == null)
            throw new UsernameNotFoundException("用户名或密码错误");
        int userID = account.getSid();
        UserDetails user = User
                .withUsername(username)
                .password(account.getPassword())
                .roles(account.getRole())
                .build();
        return new HalihapiUser(user,userID);
    }
    @Override
    public RestBeanNew<?> confirmResetAccount(String username) throws Exception {
        //确认这个账号存在,这个Service中以确保账号存在，否则会抛出用户名不存在异常，所以此处获取的一定是存在的Account
        Account account = findAccount(username);
        String email = account.getEmail();
        //生成UUID，信息脱敏，给前端
        Map<String,Object> askAccountSuccessMessage = askAccountSuccessMessage(account);
        String reset_uuid = (String) askAccountSuccessMessage.get("reset_uuid");
        //改变重设密码状态
        updateResetAccountInfo(username,email, reset_uuid);
        return RestBeanNew.success(askAccountSuccessMessage,"账号存在");
    }

    private Account findAccount(String username) {
        log.info("检测账号是否存在");
        return Optional.ofNullable(repository.findAccountByUsername(username))
                .orElseThrow(()-> new UsernameNotFoundException("账号不存在，请重新输入"));
    }

    private Map<String, Object> askAccountSuccessMessage(Account account) throws Exception {
        log.info("生成reset_uuid");
        String reset_uuid = utils.getUUID(); //生成UUID
        log.info("执行邮箱脱敏");
        String maskedEmail = maskSensitiveInfo(account.getEmail());
        return askAccountResponseData(reset_uuid,maskedEmail);
    }

    //忘记密码 - 请求用户信息存储键值UUID，email，状态
    private void updateResetAccountInfo(String username,String email,String reset_uuid){
        log.info("重置ID和真实邮箱存入redis");
        Map<String,Object> resetAccount = resetAccountStatus(username,email);
        //临时存入redis，设置请求忘记密码后一直不执行重置操作的过期时间
        setResetStatus(reset_uuid,resetAccount,resetPassword_account_expireTime);
    }
    //用户信息脱敏
    private String maskSensitiveInfo(String email) throws Exception {
        String[] emailParts = email.split("@");
        StringBuilder maskedEmailBuilder = new StringBuilder();
        if(emailParts[0].length() > 4){ // 保留邮箱地址前2位与后两位，其余用 * 替代
            maskedEmailBuilder
                    .append(emailParts[0],0,2)
                    .append("*".repeat(emailParts[0].length()-4))
                    .append(emailParts[0], emailParts[0].length() - 2, emailParts[0].length());
        }else throw new Exception("邮箱长度出错");
        maskedEmailBuilder.append("@").append(emailParts[1]);
        return maskedEmailBuilder.toString();
    }
    private Map<String,Object> askAccountResponseData(String reset_uuid,String maskedEmail){
        return Map.of("reset_uuid",reset_uuid,"maskedEmail",maskedEmail);
    }
    private Map<String,Object> resetAccountStatus(String username,String email){
        return Map.of("username",username,"email",email,"identifyId", "");
    }

    private void setResetStatus(String reset_uuid, Map<String, Object> values, long expirationTime) {
        redisTemplate.opsForHash().putAll(reset_passwordKey, Map.of(reset_uuid,values));
        redisTemplate.expire(reset_passwordKey, expirationTime, TimeUnit.MINUTES);
    }

}
