package com.api.zhangzl.retrydemo.annotation;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.SOURCE)
public @interface CheckRetryValue {
}
