package com.zwh.utils.observable;

import androidx.lifecycle.MutableLiveData;

import java.util.HashMap;
import java.util.Map;

/**
 * 简单封装 MutableLiveData 用于观察监听事件
 */
public class LiveDataBus {

    private final Map<String, MutableLiveData<Object>> bus;
    private static volatile LiveDataBus liveDataBus;

    private LiveDataBus() {
        bus = new HashMap<>();
    }

    public static LiveDataBus getInstance() {
        if (liveDataBus == null) {
            synchronized (LiveDataBus.class) {
                if (liveDataBus == null) {
                    liveDataBus = new LiveDataBus();
                }
            }
        }
        return liveDataBus;
    }

    public <T> MutableLiveData<T> with(String key, Class<T> type) {
        if (!bus.containsKey(key)) {
            bus.put(key, new MutableLiveData<>());
        }
        return (MutableLiveData<T>) bus.get(key);
    }

    public MutableLiveData<Object> with(String key) {
        return with(key, Object.class);
    }

//    public <T> T withBean(String key, Class<T> type) {
//        T t = null;
//        try {
//            if (!beans.containsKey(key)) {
//                t = type.newInstance();
//                beans.put(key, t);
//            }
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        }
//
//        return (T) beans.get(key);
//    }


}