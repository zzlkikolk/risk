package com.api.zhangzl.riskDemo.Rule;

import lombok.Data;

import java.util.List;

/**
 * <h3>RetryDemo</h3>
 * 规则类型
 * @author : zhangzl- zhangzhilin@igamebuy.com
 * @version : 1.0.0
 * @date : 2023/12/6 12:20
 **/
@Data
public class Rule {
    /**
     * 唯一标识
     */
    private String id;
    /**
     * 规则列表
     */
    private List<String> risks;
    /**
     * 规则目标类
     */
    private String targetClass;

    /**
     * 目标规则方法
     */
    private String targetMethod;
}
