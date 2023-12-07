package com.api.zhangzl.riskDemo.Rule.chain;

/**
 * <h3>RetryDemo</h3>
 * 规则执行链
 * @author : zhangzl- zhangzhilin@igamebuy.com
 * @version :
 * @date : 2023/12/7 16:59
 **/
public interface RuleChain {

    /**
     * TODO 参数应该为通用
     */
    void doExecute();
}
