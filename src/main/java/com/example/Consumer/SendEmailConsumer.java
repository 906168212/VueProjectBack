package com.example.Consumer;

import com.example.Service.EmailService;
import com.example.Util.Const;
import com.rabbitmq.client.Channel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Map;

//监听队列
@Component
@Slf4j
@RabbitListener(queues = Const.EMAIL_QUEUE_NAME,ackMode = "MANUAL")
public class SendEmailConsumer {
    @Resource
    EmailService emailService;
    @Value("${spring.mail.username}")
    String username;
    @Value("${spring.rabbitmq.listener.simple.retry.max-attempts}")
    private Integer retryCount;
    @Value("${spring.mail.limitTime}")
    long emailLimitTime;
    @Value("${spring.mail.expire}")
    long emailExpireTime;
    private Integer retryAttemptCount = 0;

    @RabbitHandler
    public void SendEmailProcess(Map<String,Object> message,Message amqpmessage, Channel channel) throws IOException {
        long deliveryTag = amqpmessage.getMessageProperties().getDeliveryTag();
        log.info("开始尝试消费");
        String uuid = (String) message.get("uuid");
        try{
            sendEmailMessage(message);
            retryAttemptCount = 0; // 重试次数清零
            channel.basicAck(deliveryTag,false); //发送邮件成功，手动ACK
            //消费完成，发送邮件成功，处理
            returnSendEmailSuccessResponse(message);
        }catch (Exception e){
            handleFailure(e,uuid,channel,deliveryTag);
        }
    }


    private void handleFailure(Exception e,String uuid,Channel channel,long deliveryTag) throws IOException {
        log.error("发送邮件失败，失败原因：{}",e.getMessage());
        if(retryAttemptCount.equals(retryCount-1)) {
            sendEmailFailedProcess(uuid,e);
            retryAttemptCount = 0; // 重试次数清零
            channel.basicReject(deliveryTag, false);  // 拒绝响应，送入死信队列
        } else {
            log.warn("开始进行第{}次重试消费",++retryAttemptCount);
            throw new RuntimeException(e);
        }
    }

    private void sendEmailMessage(Map<String,Object> emailData) throws Exception {
        emailService.sendEmailMessage(emailData);
    }
    private void sendEmailFailedProcess(String uuid,Exception e){
        emailService.sendEmailFailedProcess(uuid,e);
    }
    private void returnSendEmailSuccessResponse(Map<String,Object> message) throws Exception {
        emailService.returnSendEmailSuccessResponse(message);
    }
}
