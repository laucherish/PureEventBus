package com.laucherish.pureeventbus.lib;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            Subscriber subscriber = method.getAnnotation(Subscriber.class);
            if (subscriber != null) {
                Class<?>[] types = method.getParameterTypes();
                if (types.length == 1) {
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
        Iterator<Object> iterator = set.iterator();
        while (iterator.hasNext()) {
            Object obj = iterator.next();
            List<SubscribeMethod> list = cacheMap.get(obj);
            if (list != null) {
                for (SubscribeMethod subscribeMethod : list) {
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
