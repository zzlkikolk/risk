package com.api.zhangzl.retrydemo.config;

import cn.hutool.Hutool;
import cn.hutool.http.HttpUtil;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * <h3>RetryDemo</h3>
 *
 * @author : zhangzl- zhangzhilin@igamebuy.com
 * @version :
 * @date : 2023/9/12 15:03
 **/
@Configuration
public class RabbitMqDirectConfig {

    @Value("${queueName}")
    private String queueName;

    @Value("${exchangeName}")
    private String exchangeName;

    @Value("${routingKey}")
    private String routingKey;
    @Bean
    public Queue getQueue(){
        return new Queue(queueName);
    }

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Binding bing(){
        return BindingBuilder.bind(getQueue()).to(directExchange()).with(routingKey);
    }

    //=======================================

    public static void main(String[] args) {
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                String json=HttpUtil.get("https://wallet-web-test.igamebuy.com/fast-buy/servers/21379?cid=21379&timestamp=1698048741677&_rdn=lo2mdmi5&comefrom=igame&area_code=hk");
                System.out.println(json);
            }
        };
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue(1 * 2 * 10);
        ThreadFactory threadFactory =Executors.defaultThreadFactory();
        //拒绝执行处理器
        RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();
        ThreadPoolExecutor threadPoolExecutor=new ThreadPoolExecutor(10,20,6000, TimeUnit.SECONDS,workQueue,threadFactory,handler);
    }
}
