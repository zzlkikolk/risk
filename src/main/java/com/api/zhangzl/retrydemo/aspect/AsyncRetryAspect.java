package com.api.zhangzl.retrydemo.aspect;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.api.zhangzl.retrydemo.annotation.AsyncRetry;
import com.api.zhangzl.retrydemo.annotation.Retry;
import com.api.zhangzl.retrydemo.exception.RetryFailException;
import lombok.AllArgsConstructor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.retry.ExhaustedRetryException;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <h3>RetryDemo</h3>
 *
 * @author : zhangzl- zhangzhilin@igamebuy.com
 * @version :
 * @date : 2023/10/8 15:18
 **/
@Aspect
@Component
@AllArgsConstructor
public class AsyncRetryAspect {

    private static final Logger log = LoggerFactory.getLogger(AsyncRetryAspect.class);

    //RabbitTemplate是SpringBoot提供的用于操作RabbitMQ的模板类
    protected final RabbitTemplate rabbitTemplate;

    private final  RetryTemplate retryTemplate=new RetryTemplate();

    /**
     * 重试切面
     * @param joinPoint 切点
     * @throws Throwable 异常
     */
    @Around(value = "@annotation(com.api.zhangzl.retrydemo.annotation.AsyncRetry)")
    public Object retry(ProceedingJoinPoint joinPoint)  {
        //region    获取注解信息
        MethodSignature methodSignature= (MethodSignature) joinPoint.getSignature();
        Method method=methodSignature.getMethod();
        log.debug("重试方法"+method.getName());
        AsyncRetry asyncRetry=method.getAnnotation(AsyncRetry.class);
        //endregion

        retryTemplate.setBackOffPolicy(this.backOffPolicy(asyncRetry));
        retryTemplate.setRetryPolicy(this.retryPolicy(asyncRetry));
        //endregion

        try {
            return retryTemplate.execute(this.retryCallback(joinPoint));
        }catch (RetryFailException e) {
            log.warn("重试次数已用完，推送消息到MQ");
            Object[] args=joinPoint.getArgs();
            Map<String,Object> map=new ConcurrentHashMap<>();
            map.put("args",args);
            rabbitTemplate.convertAndSend(asyncRetry.exchange(),asyncRetry.routingKey(),new JSONObject(map));
            return e.getObject();
        }
    }


    /**
     * @param joinPoint   方法调用
     * @return  重试回调
     */
    protected RetryCallback<Object, RetryFailException> retryCallback(ProceedingJoinPoint joinPoint) {
        return context->{
            try {
                log.info("执行重试");
                Object value = null;
                try {
                    value = joinPoint.proceed();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
                if(value instanceof Boolean){
                    Boolean result= (Boolean) value;
                    if(!result){
                        throw new RetryFailException("重试失败",value);
                    }
                }
                return value;
            } catch (ExhaustedRetryException e){
                throw e;
            }
        };
    }
    /**
     * 设置重试补偿策略
     * FixedBackOffPolicy   固定时间间隔重试策略
     * ExponentialBackOffPolicy    指数时间间隔重试策略
     * UniformRandomBackOffPolicy  均匀随机重试策略
     * ExponentialRandomBackOffPolicy  指数随机重试策略
     * StatelessBackOffPolicy    无状态重试策略
     * @see ExponentialBackOffPolicy
     * @param asyncRetry    重试注解
     * @return        重试补偿策略
     */
    protected FixedBackOffPolicy backOffPolicy(AsyncRetry asyncRetry){
        FixedBackOffPolicy backOffPolicy=new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(asyncRetry.delay());//延迟时间
        return backOffPolicy;
    }

    /**
     * 设置重试策略
     * SimpleRetryPolicy
     * TimeoutRetryPolicy   超时重试策略
     * ExpressionRetryPolicy    表达式重试策略
     * CircuitBreakerRetryPolicy    断路器重试策略
     * CompositeRetryPolicy    组合重试策略
     * ExceptionClassifierRetryPolicy    异常分类重试策略
     * NeverRetryPolicy    不重试策略
     * AlwaysRetryPolicy   总是重试策略
     * @see org.springframework.retry.policy.SimpleRetryPolicy
     * @param asyncRetry    重试注解
     * @return      重试策略
     */
    protected SimpleRetryPolicy retryPolicy(AsyncRetry asyncRetry){
        SimpleRetryPolicy retryPolicy=new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(asyncRetry.maxAttempts()+1);
        return retryPolicy;
    }

}
