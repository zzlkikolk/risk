package com.api.zhangzl.retrydemo.aspect;

import com.alibaba.fastjson.JSONObject;
import com.api.zhangzl.retrydemo.annotation.AsyncRetry;
import com.api.zhangzl.retrydemo.exception.RetryFailException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.retry.ExhaustedRetryException;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <h3>RetryDemo</h3>
 *  重试拦截器
 * @author : zhangzl- zhangzhilin@igamebuy.com
 * @version :
 * @date : 2023/10/9 16:16
 **/
public class RetryInterceptor implements MethodInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RetryInterceptor.class);


    //重试模板
    private final RetryTemplate retryTemplate=new RetryTemplate();

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        Method method = invocation.getMethod();
        log.info("执行方法前"+method.getName());
        AsyncRetry asyncRetry=method.getAnnotation(AsyncRetry.class);

        retryTemplate.setBackOffPolicy(this.backOffPolicy(asyncRetry));
        retryTemplate.setRetryPolicy(this.retryPolicy(asyncRetry));

        try {
            return retryTemplate.execute(this.retryCallback(invocation));
        }catch (RetryFailException e) {
            log.warn("重试次数已用完，推送消息到MQ");
//            Object[] args=invocation.getArguments();
//            Map<String,Object> map=new ConcurrentHashMap<>();
//            map.put("args",args);
//            rabbitTemplate.convertAndSend(asyncRetry.exchange(),asyncRetry.routingKey(),new JSONObject(map));
            return e.getObject();
        }

    }


    /**
     *
         * @param invocation
         * @return  重试回调
     */
    protected RetryCallback<Object, RetryFailException> retryCallback(MethodInvocation invocation) {
        return context->{
            try {
                log.info("执行重试");
                Object value = null;
                try {
                    value = invocation.proceed();
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
    protected ExponentialBackOffPolicy backOffPolicy(AsyncRetry asyncRetry){
        ExponentialBackOffPolicy backOffPolicy=new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(asyncRetry.delay());//延迟时间
        backOffPolicy.setMultiplier(1); //指数
        backOffPolicy.setMaxInterval(asyncRetry.maxAttempts()+1); //最大重试次数
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
