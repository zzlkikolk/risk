package com.api.zhangzl.riskDemo.Risk.impl;

import com.api.zhangzl.riskDemo.Risk.Risk;

/**
 * <h3>RetryDemo</h3>
 *
 * @author : zhangzl- zhangzhilin@igamebuy.com
 * @version :
 * @date : 2023/12/7 9:49
 **/
public class IpRisk implements Risk {
    @Override
    public void execute() {
        System.out.println("ip检查器");
    }
}
