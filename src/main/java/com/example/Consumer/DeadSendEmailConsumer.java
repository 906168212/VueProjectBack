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
@RabbitListener(queues = Const.DEAD_LETTER_QUEUE_NAME,ackMode = "MANUAL")
public class DeadSendEmailConsumer {

    @Resource
    SendEmailLogService sendEmailLogService;

    @RabbitHandler
    public void deadSendEmailProcess(Map<String,Object> message,Message amqpmessage, Channel channel) throws IOException {
        log.info("消息进入死信队列，路由键为：{}",amqpmessage.getMessageProperties().getReceivedRoutingKey());

        long deliveryTag = amqpmessage.getMessageProperties().getDeliveryTag();
        String uuid = (String) message.get("uuid");
        try {
            sendEmailLogService.updateStatus(uuid,Const.SendEmailLogStatus.CONSUMED);
            channel.basicAck(deliveryTag,false); // 不批量确认
            log.info("成功消费死信消息");
        }catch (Exception e){
            channel.basicAck(deliveryTag,false); // 不批量确认
            log.info("死信队列中消息的消费失败,原因：{}", e.getMessage());
        }
    }
}
