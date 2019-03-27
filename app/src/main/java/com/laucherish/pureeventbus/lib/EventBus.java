package com.laucherish.pureeventbus.lib;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * EventBus核心类
 *
 * Created by laucherish on 2019/3/27.
 */

public class EventBus {

    private static volatile EventBus instance;
    private Map<Object, List<SubscribeMethod>> cacheMap;

    private EventBus() {
        cacheMap = new HashMap<>();
    }

    /**
     * 单例模式
     * @return instance
     */
    public static EventBus getInstance() {
        if (instance == null) {
            synchronized (EventBus.class) {
                if (instance == null) {
                    instance = new EventBus();
                }
            }
        }
        return instance;
    }

    /**
     * 订阅者注册，提取Subscriber注解的方法
     * @param obj 订阅者
     */
    public void register(Object obj) {
        List<SubscribeMethod> list;
        list = cacheMap.get(obj);
        if (list == null) {
            list = new ArrayList<>();
        }
        Class clazz = obj.getClass();
        // 通过反射获取订阅类的所有方法
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            // 提取出注解的方法
            Subscriber subscriber = method.getAnnotation(Subscriber.class);
            if (subscriber != null) {
                // 获取方法的所有参数类
                Class<?>[] types = method.getParameterTypes();
                // 只支持一个参数，多个参数也可以封装成一个参数
                if (types.length == 1) {
                    // 将SubscribeMethod添加到list
                    list.add(new SubscribeMethod(method, subscriber.threadMode(), types[0]));
                } else {
                    throw new RuntimeException("只支持一个参数！");
                }
            }
        }
        cacheMap.put(obj, list);
    }

    /**
     * 发布者发布事件
     * @param type 事件参数
     */
    public void post(Object type) {
        Set<Object> set = cacheMap.keySet();
        // 通过foreach遍历cacheMap
        for (Object obj : set) {
            // 取出每一个订阅者的所有SubscribeMethod
            List<SubscribeMethod> list = cacheMap.get(obj);
            if (list != null) {
                for (SubscribeMethod subscribeMethod : list) {
                    // 判断订阅方法的参数类和事件参数类型大致相同
                    if (subscribeMethod.getType().isAssignableFrom(type.getClass())) {
                        invoke(obj, subscribeMethod, type);
                    }
                }
            }
        }
    }

    /**
     * 通过反射调用对象的方法
     * @param obj 对象
     * @param method 方法
     * @param type 参数
     */
    private void invoke(Object obj, SubscribeMethod method, Object type) {
        try {
            method.getMethod().invoke(obj, type);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
