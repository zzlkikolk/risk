package com.api.zhangzl.retrydemo.Consumer;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * <h3>RetryDemo</h3>
 *
 * @author : zhangzl- zhangzhilin@igamebuy.com
 * @version :
 * @date : 2023/9/18 11:17
 **/
@Component
@Slf4j
public class normalConsumer {

    @RabbitHandler
    @RabbitListener(queues = "normal")
    public void receiveMessage(Message message) {
        String msg = new String(message.getBody());
        JSONObject jsonObject= JSONUtil.parseObj(msg);
        JSONArray jsonArray=jsonObject.getJSONArray("args");
        String info=jsonArray.getJSONObject(0).getStr("message");
        log.info("Received message: " + msg);
        if (info.equals("Hello RabbitMQ DLX Message")) {
            // 拒绝消息，使其进入死信队列
            throw new AmqpRejectAndDontRequeueException("死信队列");
        } else {
            // 处理其他消息
            log.info("收到消息"+msg);
        }
    }

    @RabbitListener(queues = "dlxQueue")
    public void receiveDlxMessage(Message message) {
        String msg = new String(message.getBody());
        log.info("死信队列消息: " + msg);
        // 处理死信队列中的消息，如记录日志、发送警报等。也可以再次拒绝消息，使其继续进入死信队列。
    }
}
