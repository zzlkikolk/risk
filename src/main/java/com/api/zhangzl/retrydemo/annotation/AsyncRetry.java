package com.api.zhangzl.retrydemo.annotation;

import org.springframework.lang.NonNull;


import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AsyncRetry {

    String queue();//队列名称

    String routingKey();//路由键

    String exchange();//交换机名称

    int maxAttempts() default 3;//最大重试次数

    long delay() default 1000;//重试延迟时间 单位毫秒

}
