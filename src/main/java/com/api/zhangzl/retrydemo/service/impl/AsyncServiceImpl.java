package com.api.zhangzl.retrydemo.service.impl;

import com.api.zhangzl.retrydemo.pojo.Result;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <h3>RetryDemo</h3>
 *
 * @author : zhangzl- zhangzhilin@igamebuy.com
 * @version :
 * @date : 2023/10/10 10:42
 **/
@Service
public class AsyncServiceImpl {

    @Resource
    protected WalletRetryImpl walletRetryImpl;

    @Async
    public void AsyncRetry(Result result){
        walletRetryImpl.retry(result);
    }
}
