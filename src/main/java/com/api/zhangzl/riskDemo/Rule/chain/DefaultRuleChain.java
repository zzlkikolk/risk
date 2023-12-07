package com.api.zhangzl.riskDemo.Rule.chain;

import com.api.zhangzl.riskDemo.Risk.Risk;

import java.util.List;

/**
 * <h3>RetryDemo</h3>
 * 规则执行链
 * @author : zhangzl- zhangzhilin@igamebuy.com
 * @version :
 * @date : 2023/12/7 17:00
 **/
public class DefaultRuleChain implements RuleChain{

    private final List<Risk> risks;

    private final int index;

    public DefaultRuleChain(List<Risk> risks) {
        this.risks = risks;
        this.index = 0;
    }


    private DefaultRuleChain(DefaultRuleChain defaultRuleChain, int index) {
        this.risks = defaultRuleChain.getRisks();
        this.index = index;
    }

    public List<Risk> getRisks() {
        return risks;
    }

    @Override
    public void doExecute() {
        if(this.index<this.risks.size()){
            Risk risk = risks.get(index);
            DefaultRuleChain defaultRuleChain=new DefaultRuleChain(this,this.index+1);
            risk.execute();
        }
    }
}
