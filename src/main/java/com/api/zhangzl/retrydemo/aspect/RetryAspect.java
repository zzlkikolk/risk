package com.api.zhangzl.retrydemo.aspect;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.api.zhangzl.retrydemo.annotation.Retry;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * <h3>RetryDemo</h3>
 * 重试切面类
 * @author : zhangzl- zhangzhilin@igamebuy.com
 * @version :
 * @date : 2023/10/7 14:03
 **/
@Aspect
@Component
@AllArgsConstructor
public class RetryAspect {

    //RabbitTemplate是SpringBoot提供的用于操作RabbitMQ的模板类
    private final RabbitTemplate rabbitTemplate;


    /**
     * 重试切面
     * @param joinPoint 切点
     * @throws Throwable 异常
     */
    @Around(value = "@annotation(com.api.zhangzl.retrydemo.annotation.Retry)")
    public Object retry(ProceedingJoinPoint joinPoint) throws Throwable {
        //region    获取注解信息
        MethodSignature methodSignature= (MethodSignature) joinPoint.getSignature();
        Method method=methodSignature.getMethod();
        Retry retryAnnotation=method.getAnnotation(Retry.class);
        //endregion

        //重试次数 优化成异步
        for(int i=0;i<retryAnnotation.maxAttempts();i++){
            Object proceed=joinPoint.proceed();
            if(!(proceed instanceof Boolean)){
                throw new RuntimeException("方法返回值必须为boolean类型");
            }
            boolean result= (boolean) proceed;
            if(result) {
                return true;
            }
            TimeUnit.MILLISECONDS.sleep(retryAnnotation.delay());// 延迟时间
            if(i==retryAnnotation.maxAttempts()-1){
                //获取方法参数
                Object[] args=joinPoint.getArgs();
                Map<String,Object> map=new HashMap<>();
                map.put("args",args);
                //重试次数用完，发送消息到MQ
                rabbitTemplate.convertAndSend(retryAnnotation.exchange(),retryAnnotation.routingKey(), JSONUtil.parse(map));
            }
        }
        return false;
    }

}
