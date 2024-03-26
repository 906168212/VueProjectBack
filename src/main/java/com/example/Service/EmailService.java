package com.example.Service;

import com.example.Entity.RestBeanNew;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface EmailService {
    void setEmailCompletionFuture(CompletableFuture<RestBeanNew<?>> emailCompletionFuture);
    void EmailVerifyCode(String type, String email, String ip);
    // type 消费类型 如注册、更改或其他 email 邮箱 ip 用于限制请求时间和次数,返回信息以及过期时间戳
    void prepareSendEmail(String type,String email,int code);

    void sendEmailMessage(Map<String,Object> emailData) throws Exception;

    void returnSendEmailSuccessResponse(Map<String,Object> emailData) throws Exception;
    void sendEmailFailedProcess(String uuid, Exception e);
    void sendEmail(Map<String,Object> data, CorrelationData correlationData);
}
