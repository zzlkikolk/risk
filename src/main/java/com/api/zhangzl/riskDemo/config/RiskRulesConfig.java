package com.api.zhangzl.riskDemo.config;

import com.api.zhangzl.riskDemo.Rule.Rule;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <h3>RetryDemo</h3>
 *
 * @author : zhangzl- zhangzhilin@igamebuy.com
 * @version :
 * @date : 2023/12/7 9:58
 **/
@Component
@ConfigurationProperties(prefix = "risk")
public class RiskRulesConfig {

    private List<Rule> rules;

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }
}
