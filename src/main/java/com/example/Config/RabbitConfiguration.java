package com.example.Config;

import com.example.Util.Const;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Configuration
@Slf4j
public class RabbitConfiguration {

    // 邮箱
    @Bean // 邮箱消息队列
    public Queue emailQueue(){
        Map<String,Object> args = new HashMap<>();
        //这里声明当前业务队列绑定的死信交换机
        args.put("x-dead-letter-exchange", Const.DEAD_LETTER_EXCHANGE_NAME);
        //这里声明当前业务队列的死信路由 key
        args.put("x-dead-letter-routing-key",Const.DEAD_LETTER_QUEUE_ROUTING_KEY);
        args.put("x-message-ttl",Const.X_TTL);
        args.put("x-max-length",Const.X_MAX_LENGTH);
        return QueueBuilder
                .durable(Const.EMAIL_QUEUE_NAME)
                .withArguments(args)
                .build();
    }
    @Bean // Topic交换机
    public TopicExchange mailExchange(){
        return new TopicExchange(Const.EMAIL_EXCHANGE_NAME,true,false);
    }
    @Bean // 交换机和队列相连
    public Binding mailBinding(){
        return BindingBuilder.bind(emailQueue()).to(mailExchange()).with(Const.EMAIL_ROUTING_KEY);
    }

    // 处理队列
    @Bean // 邮箱消息队列
    public Queue processQueue(){
        return QueueBuilder.durable(Const.PROCESS_QUEUE_NAME).build();
    }
    @Bean // Topic交换机
    public TopicExchange processExchange(){
        return new TopicExchange(Const.PROCESS_EXCHANGE_NAME,true,false);
    }
    @Bean // 交换机和队列相连
    public Binding processBinding(){
        return BindingBuilder.bind(processQueue()).to(processExchange()).with(Const.PROCESS_ROUTING_KEY);
    }


    // 死信
    @Bean // 死信队列
    public Queue deadEmailQueue(){
        return QueueBuilder.durable(Const.DEAD_LETTER_QUEUE_NAME).build();
    }
    @Bean // 死信交换机
    public DirectExchange deadEmailExchange(){
        return new DirectExchange(Const.DEAD_LETTER_EXCHANGE_NAME,true,false);
    }
    @Bean // 绑定死信队列与交换机
    public Binding deadEmailBinding(){
        return BindingBuilder.bind(deadEmailQueue()).to(deadEmailExchange()).with(Const.DEAD_LETTER_QUEUE_ROUTING_KEY);
    }
    // 消息传递
    // 消息转换器
    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }
    @Resource  // 连接池
    private CachingConnectionFactory connectionFactory;

    @Bean //消息发送确认回调
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        //RabbitMQ会调用Basic。Return命令将消息返还给生产者 无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(true);
        //消息是否成功发送到交换机
        rabbitTemplate.setConfirmCallback(((correlationData, ack, cause) -> {
            if(ack) log.info("消息成功传送到Exchange，消息ID为：{}", Objects.requireNonNull(correlationData).getId());
            else log.info("消息发送到Exchange失败，{}，cause：{}",correlationData,cause);
        }));
        // 交换机无法根据自身类型和key找到一个合适的队列时的处理方式

        // 消息从Exchange路由到Queue失败的回调
        rabbitTemplate.setReturnsCallback((message) ->{
            log.info("消息发送失败！消息内容：{}",message);
        });
        return rabbitTemplate;
    }

}




