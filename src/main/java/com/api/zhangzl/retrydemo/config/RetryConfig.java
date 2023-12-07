package com.api.zhangzl.retrydemo.config;

import com.api.zhangzl.retrydemo.aspect.RetryInterceptor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * <h3>RetryDemo</h3>
 *
 * @author : zhangzl- zhangzhilin@igamebuy.com
 * @version :
 * @date : 2023/10/8 14:33
 **/
@Configuration
@EnableRetry
@EnableAsync
public class RetryConfig {

//    @Bean
//    public DefaultPointcutAdvisor defaultPointcutAdvisor(){
//        RetryInterceptor retryInterceptor = new RetryInterceptor();
//        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
//        pointcut.setExpression("@annotation(com.api.zhangzl.retrydemo.annotation.AsyncRetry)");
//        //配置切点
//        DefaultPointcutAdvisor defaultPointcutAdvisor = new DefaultPointcutAdvisor();
//        defaultPointcutAdvisor.setPointcut(pointcut);
//        defaultPointcutAdvisor.setAdvice(retryInterceptor);
//        return  defaultPointcutAdvisor;
//    }
}
