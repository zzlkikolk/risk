package com.api.zhangzl.retrydemo.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * <h3>RetryDemo</h3>
 *
 * @author : zhangzl- zhangzhilin@igamebuy.com
 * @version :
 * @date : 2023/9/18 11:37
 **/
@Configuration
public class RabbitMqDLXConfig {

    @Bean
    public DirectExchange normalExchange() {
        return new DirectExchange("normalExchange");
    }


    @Bean
    public Binding normalBinding() {
        return BindingBuilder.bind(normalQueueWithDlx())
                .to(normalExchange())
                .with("normalKey");
    }

    /**
     * 交换机
     * @return
     */
    @Bean
    public DirectExchange dlxExchange(){
        return new DirectExchange("dlxExchange");
    }

    /**
     * 队列
     * @return
     */
    @Bean
    public Queue dlxQueue(){
        return new Queue("dlxQueue");
    }

    /**
     * 绑定
     * @return
     */
    @Bean
    public Binding dlxBinding(){
        return BindingBuilder.bind(dlxQueue()).to(dlxExchange()).with("dlxKey");
    }

    /**
     * 设置队列的死信交换机和死信队列
     * @return
     */
    @Bean
    public Queue normalQueueWithDlx(){
        Map<String,Object> args=new HashMap<>();
        args.put("x-dead-letter-exchange", "dlxExchange"); // 设置死信交换机
        args.put("x-dead-letter-routing-key", "dlxKey"); // 设置死信RoutingKey
        return QueueBuilder.durable("normal").withArguments(args).build();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());

        return rabbitTemplate;
    }
}
