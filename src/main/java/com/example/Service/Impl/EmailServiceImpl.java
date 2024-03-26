package com.example.Service.Impl;


import com.example.Entity.RestBeanNew;
import com.example.Service.EmailMessageService;
import com.example.Service.EmailService;
import com.example.Service.SendEmailLogService;
import com.example.Util.CommonUtils;
import com.example.Util.Const;
import com.example.Util.FlowUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
    @Resource
    JavaMailSender sender;
    @Resource
    SendEmailLogService sendEmailLogService;
    @Resource
    FlowUtils flowUtils;
    @Resource
    RabbitTemplate rabbitTemplate;
    @Resource
    CommonUtils commonUtils;
    @Resource
    RedisTemplate<String,Object> redisTemplate;
    @Resource
    EmailMessageService emailMessageService;
    @Value("${spring.mail.limitTime}")
    long emailLimitTime;
    @Value("${spring.mail.expire}")
    long emailExpireTime;

    private CompletableFuture<RestBeanNew<?>> emailCompletionFuture;
    @Override
    public void setEmailCompletionFuture(CompletableFuture<RestBeanNew<?>> emailCompletionFuture) {
        this.emailCompletionFuture = emailCompletionFuture;
    }
    @Override
    public void EmailVerifyCode(String type, String email, String ip) {

        // 为了防止同一请求超多调用，如100次、200次同时，还得加把锁,排队
        synchronized (ip.intern()){
            if(!this.checkVerifyLimit(ip)) failureResponse(400,"操作请求频繁");
            else {
                int code = commonUtils.randomFiveCode(); //生成随机验证码
                prepareSendEmail(type,email,code);
            }
        }
    }
    @Override
    public void prepareSendEmail(String type,String email,int code){
        String uuid = commonUtils.getUUID(); //生成每一个邮箱发送的唯一UUID
        Map<String,Object> data = prepareEmailData(type, email, code, uuid); // map用于存入一会需要放入消息队列的内容
        storeEmailLog(uuid,email,type); // 存储邮件发送记录日志
        sendEmail(data,new CorrelationData(uuid)); // 生产消息
    }

    @Override
    public void sendEmailMessage(Map<String,Object> emailData) throws Exception {
        SimpleMailMessage sendEmailMessage = emailMessage(emailData);
        if(sendEmailMessage==null) throw new Exception("请求参数错误");
        log.info("发送邮件");
        sender.send(sendEmailMessage);
    }

    @Override
    public void returnSendEmailSuccessResponse(Map<String,Object> emailData) throws Exception {
        //邮件发送成功相关处理
        String uuid = (String)emailData.get("uuid");
        log.info("数据库状态更新，消息投递完毕");
        sendEmailLogService.updateStatus(uuid,Const.SendEmailLogStatus.CONSUME_SUCCESS);
        String type = (String)emailData.get("type");
        String email = (String)emailData.get("email");
        Integer code = (Integer)emailData.get("code");

        if(type.equals("register") || type.equals("resetPassword")){
            verifyCodeSuccessProcess(type,email,code);
        }else if(type.equals("registerSuccess")){
            registerSuccessProcess();
        }else throw new Exception("请求参数错误");
    }


    @Override
    public void sendEmailFailedProcess(String uuid, Exception e) {
        log.error("重试次数达到上限，邮件发送失败,进入死信队列消费");
        sendEmailLogService.updateStatus(uuid,Const.SendEmailLogStatus.CONSUME_FAILURE);
        log.info("数据库更新完毕！消息投递失败");
        failureResponse(500,"邮件发送失败，请检查邮箱是否正确");
    }
    // 发送邮件
    @Override
    public void sendEmail(Map<String,Object> data, CorrelationData correlationData) {
        rabbitTemplate.convertAndSend(Const.EMAIL_EXCHANGE_NAME, Const.EMAIL_ROUTING_KEY, data, correlationData);
    }

    private SimpleMailMessage emailMessage(Map<String,Object> emailData) {
        String type = (String) emailData.get("type");
        String email = (String) emailData.get("email");
        Integer code = (Integer) emailData.get("code");
        return switch (type){
            case "register" -> emailMessageService.createRegisterMessage(code,email);
            case "resetPassword" -> emailMessageService.createResetPasswordMessage(code,email);
            case "registerSuccess" -> emailMessageService.createRegisterSuccessMessage(email);
            default -> null;
        };
    }

    private void verifyCodeSuccessProcess(String type,String email,int code){
        log.info("验证码Redis存储");
        storeEmailCodeToRedis(type,email,code);
        long LimitTimestamp = System.currentTimeMillis() + emailLimitTime * 1000; //计算过期时间戳
        Map<String,Object> sendSuccessData = Map.of("HasLimit",false,"LimitTimestamp",LimitTimestamp);
        successResponse(sendSuccessData,"验证码已发送，请注意查收");
    }
    private void registerSuccessProcess(){
        successResponse(null,"注册成功");
    }
    private Map<String,Object> prepareEmailData(String type, String email, int code, String uuid){
        return Map.of("type", type, "email", email, "code", code, "uuid", uuid);
    }
    private void storeEmailLog(String uuid, String email, String type) { // 存储邮件日志
        sendEmailLogService.storeBasicLog(uuid, email, type, Const.EMAIL_EXCHANGE_NAME, Const.EMAIL_ROUTING_KEY);
    }
    private boolean checkVerifyLimit(String ip){ //检查冷却
        String key = Const.VERIFY_EMAIL_LIMIT + ip;
        return flowUtils.limitOnceCheck(key,emailLimitTime);
    }
    private void storeEmailCodeToRedis(String type,String email,int code){
        Map<String,Object> storeData = Map.of("code",String.valueOf(code));
        setVerificationCode(type,email,storeData,emailExpireTime);
    }
    private void setVerificationCode(String hashName, String email, Map<String, Object> values, long expirationTime) {
        redisTemplate.opsForHash().putAll(Const.VERIFY_EMAIL_DATA + ":" + hashName, Map.of(email,values));
        redisTemplate.expire(Const.VERIFY_EMAIL_DATA + ":" + hashName, expirationTime, TimeUnit.MINUTES);
    }
    private void successResponse(Map<String,Object> emailData,String message){
        emailCompletionFuture.complete(RestBeanNew.success(emailData,message));
    }
    private void failureResponse(int code,String message){
        emailCompletionFuture.complete(RestBeanNew.failure(code,message));
    }
}
