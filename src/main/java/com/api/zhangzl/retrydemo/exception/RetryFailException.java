package com.api.zhangzl.retrydemo.exception;

/**
 * <h3>RetryDemo</h3>
 *
 * @author : zhangzl- zhangzhilin@igamebuy.com
 * @version :
 * @date : 2023/10/8 16:20
 **/
public class RetryFailException extends Exception{

    private Object object;

    public RetryFailException(String message,Object object) {
            super(message);
            this.object = object;
        }

    public RetryFailException() {
            super();
        }

    public RetryFailException(String message) {
            super(message);
        }

    public Object getObject() {
        return object;
    }
}
