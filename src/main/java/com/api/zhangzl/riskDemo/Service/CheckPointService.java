package com.api.zhangzl.riskDemo.Service;

import com.api.zhangzl.riskDemo.config.RiskRulesConfig;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <h3>RetryDemo</h3>
 *
 * @author : zhangzl- zhangzhilin@igamebuy.com
 * @version :
 * @date : 2023/12/7 9:54
 **/
@Service
public class CheckPointService {

    @Resource
    private RiskRulesConfig riskRulesConfig;

    public void checkPoint(){
        System.out.println("checkPoint");
    }
}
