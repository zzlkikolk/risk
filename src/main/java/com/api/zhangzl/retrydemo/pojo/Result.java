package com.api.zhangzl.retrydemo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <h3>RetryDemo</h3>
 *
 * @author : zhangzl- zhangzhilin@igamebuy.com
 * @version :
 * @date : 2023/9/12 15:48
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private int Code;
    private String message;
    private Object data;
}
