package com.example.Consumer;

import com.example.Service.SendEmailLogService;
import com.example.Util.Const;
import com.rabbitmq.client.Channel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@Slf4j
@RabbitListener(queues = Const.PROCESS_QUEUE_NAME,ackMode = "MANUAL")
public class processConsumer {
    @Resource
    SendEmailLogService sendEmailLogService;
    @RabbitHandler
    public void process(Map<String,Object> message, Message amqpmessage, Channel channel) throws IOException {
        long deliveryTag = amqpmessage.getMessageProperties().getDeliveryTag();
        log.info("开始尝试消费");
        String uuid = (String) message.get("uuid");
        try {
            log.info("进行邮件日志数据库更新");
            sendEmailLogService.updateStatus(uuid,Const.SendEmailLogStatus.CONSUME_SUCCESS);
            channel.basicAck(deliveryTag,false); //手动ACK
        }catch (Exception e){
            channel.basicReject(deliveryTag, false);  // 拒绝响应，送入死信队列
        }
    }
}
