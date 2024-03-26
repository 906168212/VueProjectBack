package com.example.Service.Impl;

import com.example.Entity.RestBeanNew;
import com.example.Entity.dto.Account;
import com.example.Entity.vo.request.EmailVerifyVO;
import com.example.Entity.vo.request.resetPasswordStatusVO;
import com.example.Entity.vo.request.resetPasswordVO;
import com.example.Repo.AccountRepository;
import com.example.Service.ResetPasswordService;
import com.example.Util.CommonUtils;
import com.example.Util.Const;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class ResetPasswordServiceImpl implements ResetPasswordService {

    @Resource
    RedisTemplate<String,Object> redisTemplate;
    @Resource
    CommonUtils utils;
    @Resource
    AccountRepository accountRepository;
    @Resource
    PasswordEncoder encoder;

    private final String email_resetKey = Const.VERIFY_EMAIL_DATA+":resetPassword";
    private final String reset_passwordKey = Const.RESET_PASSWORD_ACCOUNT+":password";
    @Override
    public RestBeanNew<?> sendEmailRequirement(resetPasswordStatusVO vo){
        String reset_id = vo.getReset_uuid();
        String email = vo.getEmail();
        if (!resetIdVerify(reset_id)) return failureResponse("未请求账号");
        log.info("检测邮箱是否正确");
        String real_email = getRealEmail(reset_id);
        if (!real_email.equals(email)) return failureResponse("请输入正确的邮箱");
        return null;
    }

    @Override // 验证身份
    public RestBeanNew<?> verifyIdentify(EmailVerifyVO vo) throws Exception {
        String reset_uuid = vo.getReset_uuid();
        String email = vo.getEmail();
        String codeResponse = verifyEmailCode(vo);
        if(codeResponse!=null) return failureResponse(codeResponse);
        log.info("验证码通过，删除验证码");
        deleteRedisEmailCode(email);
        String identify_uuid = updateResetAccountInfo(reset_uuid);
        return successResponse(Map.of("identify_uuid",identify_uuid),"验证成功");
    }
    @Override // 获取真实邮箱
    public String getRealEmail(String reset_id){
        return utils.getHashMapKeyValue(reset_passwordKey,reset_id,"email");
    }
    @Override // 更新重置账号状态
    public String updateResetAccountInfo(String reset_uuid) throws Exception {
        log.info("生产验证成功UUID，存入redis");
        String identify_uuid = utils.getUUID();
        if(utils.changeHashJSONValue(reset_passwordKey,reset_uuid,"identifyId",identify_uuid)){
            log.info("修改后端状态成功，用户重置验证完毕，redis修改成功");
            return identify_uuid;
        }else throw new Exception("修改后端状态失败，内部Redis错误");
    }

    @Override // 重置密码
    public RestBeanNew<?> resetPassword(resetPasswordVO vo) {
        String reset_id = vo.getReset_uuid();
        String identify_id = vo.getIdentify_uuid();
        String new_password = vo.getNew_password();
        if (!resetIdVerify(reset_id)) return failureResponse("未请求账号");
        if (!identifyIdVerify(reset_id,identify_id)) return failureResponse("未通过身份验证");
        log.info("校验全部通过，开始设置新密码");
        String username = getResetPasswordUsername(reset_id);
        if (username==null) return failureResponse("请求参数错误");
        Account account = accountRepository.findAccountByUsername(username);
        if(encoder.matches(new_password,account.getPassword())) {
            log.error("新旧密码相同，重置密码失败");
            return failureResponse("新密码不能与旧密码相同");
        }
        else {
            account.setPassword(encoder.encode(new_password));
            accountRepository.save(account);
            deleteRedisResetInfo(reset_id);
            return successResponse(null,"重置密码成功");
        }
    }
    private String verifyEmailCode(EmailVerifyVO vo) throws Exception {
        log.info("检测验证码");
        String email = vo.getEmail();
        String code = getResetPasswordEmailCode(email);
        if (code==null) return "请先获取验证码";
        if(!code.equals(vo.getEmailCode())) return "验证码错误";
        return null;
    }
    private boolean resetIdVerify(String reset_id){
        log.info("重置ID检测");
        return redisTemplate.opsForHash().hasKey(reset_passwordKey,reset_id);
    }
    private boolean identifyIdVerify(String reset_id,String identify_id){
        log.info("验证ID检测");
        String real_identifyId = utils.getHashMapKeyValue(reset_passwordKey,reset_id,"identifyId");
        return identify_id.equals(real_identifyId);
    }
    private String getResetPasswordEmailCode(String email){
        return utils.getHashMapKeyValue(email_resetKey,email,"code");
    }

    private String getResetPasswordUsername(String reset_id){
        return utils.getHashMapKeyValue(reset_passwordKey,reset_id,"username");
    }
    private void deleteRedisResetInfo(String reset_id){
        log.info("新密码修改完毕，删除用户重置状态");
        redisTemplate.opsForHash().delete(reset_passwordKey,reset_id);
    }
    private void deleteRedisEmailCode(String email){
        redisTemplate.opsForHash().delete(email_resetKey,email);
    }
    private RestBeanNew<?> successResponse(Map<String,Object> responseData,String message){
        return RestBeanNew.success(responseData,message);
    }
    private RestBeanNew<?> failureResponse(String message){
        return RestBeanNew.failure(400,message);
    }
}
