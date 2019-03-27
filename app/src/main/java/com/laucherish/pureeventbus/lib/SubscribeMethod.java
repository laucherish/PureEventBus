package com.laucherish.pureeventbus.lib;

import java.lang.reflect.Method;

/**
 * 订阅的方法类
 * <p>
 * Created by laucherish on 2019/3/27.
 */

public class SubscribeMethod {

    private Method method; // 方法
    private ThreadMode threadMode; // 线程模式
    private Class<?> type; // 参数类

    public SubscribeMethod(Method method, ThreadMode threadMode, Class<?> type) {
        this.method = method;
        this.threadMode = threadMode;
        this.type = type;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public ThreadMode getThreadMode() {
        return threadMode;
    }

    public void setThreadMode(ThreadMode threadMode) {
        this.threadMode = threadMode;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }
}
