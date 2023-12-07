package com.api.zhangzl.retrydemo.service.impl;

import com.api.zhangzl.retrydemo.annotation.AsyncRetry;
import com.api.zhangzl.retrydemo.annotation.CheckRetryValue;
import com.api.zhangzl.retrydemo.annotation.Retry;
import com.api.zhangzl.retrydemo.pojo.Result;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


/**
 * <h3>RetryDemo</h3>
 *
 * @author : zhangzl- zhangzhilin@igamebuy.com
 * @version :
 * @date : 2023/9/12 15:35
 **/
@Service
public class WalletRetryImpl  {


    @AsyncRetry(queue = "normal",exchange = "normalExchange",routingKey = "normalKey",maxAttempts = 2,delay = 5000L)
    public boolean retry(Result result){
        return result.getCode()+1==1000;
    }

//    @Retry(queue = "normal",exchange = "normalExchange",routingKey = "normalKey")
    @AsyncRetry(queue = "normal",exchange = "normalExchange",routingKey = "normalKey",maxAttempts = 10,delay = 5000L)
    public boolean retry2(Result result){
        return false;
    }


}
