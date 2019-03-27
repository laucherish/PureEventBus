package com.laucherish.pureeventbus.lib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解类，用于标记订阅方法
 *
 * Created by laucherish on 2019/3/27.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Subscriber {
    ThreadMode threadMode() default ThreadMode.MAIN;
}
