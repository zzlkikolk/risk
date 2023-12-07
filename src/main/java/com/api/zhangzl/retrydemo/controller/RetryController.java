package com.api.zhangzl.retrydemo.controller;

import com.api.zhangzl.retrydemo.pojo.Result;

import com.api.zhangzl.retrydemo.service.impl.AsyncServiceImpl;
import com.api.zhangzl.retrydemo.service.impl.WalletRetryImpl;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <h3>RetryDemo</h3>
 *
 * @author : zhangzl- zhangzhilin@igamebuy.com
 * @version :
 * @date : 2023/9/12 15:53
 **/
@RestController
@RequestMapping("/retry")
public class RetryController {

    @Resource
    private WalletRetryImpl walletRetryImpl;

    @Resource
    protected AsyncServiceImpl asyncService;

    @PostMapping("/send")
    public Result send1(@RequestBody Result result){
        asyncService.AsyncRetry(result);
      return new Result(200,"success",null);
    }

    @PostMapping("/send2")
    public Result send2(@RequestBody Result result){
        walletRetryImpl.retry2(result);
        return new Result(200,"success",null);
    }

}
