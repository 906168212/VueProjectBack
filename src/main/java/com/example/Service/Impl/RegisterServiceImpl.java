package com.example.Service.Impl;

import com.example.Entity.RestBeanNew;
import com.example.Entity.dto.Account;
import com.example.Entity.vo.request.EmailRegisterVO;
import com.example.Repo.AccountRepository;
import com.example.Service.EmailService;
import com.example.Service.RegisterService;
import com.example.Util.CommonUtils;
import com.example.Util.Const;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RegisterServiceImpl implements RegisterService {

    @Resource
    RedisTemplate<String,Object> redisTemplate;

    @Resource
    AccountRepository accountRepository;

    @Resource
    PasswordEncoder encoder;
    @Resource
    EmailService emailService;
    @Resource
    CommonUtils commonUtils;


    @Override
    public RestBeanNew<String> registerAccount(EmailRegisterVO vo) throws Exception {
        //保险起见，我们在数据库内加入索引，username、email唯一
        String email = vo.getMail();
        String username = vo.getUsername();
        String code = commonUtils.getHashMapKeyValue(Const.VERIFY_EMAIL_DATA+":register",email,"code");
        if (code == null) return failureResponse("请先获取验证码");
        if(!code.equals(vo.getCode())) return failureResponse("验证码错误");
        //判断是否注册过
        if (this.existAccountByEmail(email)) return failureResponse("此电子邮箱已被注册");
        if (this.existAccountByUsername(username)) return failureResponse("此账号已被注册");
        // 注册
        String password = encoder.encode(vo.getPassword());
        createAccount(username,password,email,"user");
        return handleAccountCreation(username,email);
    }
    private RestBeanNew<String> handleAccountCreation(String username,String email){
        if (accountRepository.findAccountByUsername(username) != null){
            deleteRedisVerifyCode(email);
            sendEmail(email);
            return null;
        }else
            return failureResponse("内部错误，请联系管理员");
    }
    private void createAccount(String username,String password,String email,String role){
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        account.setEmail(email);
        account.setRole(role);
        accountRepository.save(account);
    }
    private void sendEmail(String email){
        emailService.prepareSendEmail("registerSuccess",email,0);
    }
    private void deleteRedisVerifyCode(String email){
        redisTemplate.opsForHash().delete(Const.VERIFY_EMAIL_DATA+":register",email);
    }

    private boolean existAccountByEmail(String email){
        return accountRepository.findAccountByEmail(email) != null;
    }

    private boolean existAccountByUsername(String username){
        return accountRepository.findAccountByUsername(username) != null;
    }
    private RestBeanNew<String> failureResponse(String message){
        return RestBeanNew.failure(400,message);
    }

}
