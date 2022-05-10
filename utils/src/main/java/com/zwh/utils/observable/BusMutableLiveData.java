package com.zwh.utils.observable;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 默认无粘性事件，粘性监听请使用
 *
 * @see #observeSticky(LifecycleOwner, Observer)
 */
public class BusMutableLiveData<T> extends MutableLiveData<T> {

    private Map<Observer, Observer> observerMap = new HashMap<>();
    private String TAG = getClass().getSimpleName();

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        try {
            hook((Observer<T>) observer);
        } catch (Exception e) {
            Log.e(TAG, "观察事件失败");
            e.printStackTrace();
        }
    }

    /**
     * 粘性观察事件
     */
    public void observeSticky(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        super.observe(owner, observer);
    }


//    @Override
//    public void observeForever(@NonNull Observer<? super T> observer) {
//        if (!observerMap.containsKey(observer)) {
//            observerMap.put(observer, new ObserverWrapper(observer));
//        }
//        super.observeForever(observerMap.get(observer));
//    }
//
//    @Override
//    public void removeObserver(@NonNull Observer<? super T> observer) {
//        Observer realObserver = null;
//        if (observerMap.containsKey(observer)) {
//            realObserver = observerMap.remove(observer);
//        } else {
//            realObserver = observer;
//        }
//        super.removeObserver(realObserver);
//    }

    private void hook(@NonNull Observer<T> observer) throws Exception {
        //get wrapper's version
        Class<LiveData> classLiveData = LiveData.class;
        Field fieldObservers = classLiveData.getDeclaredField("mObservers");
        fieldObservers.setAccessible(true);
        Object objectObservers = fieldObservers.get(this);
        Class<?> classObservers = objectObservers.getClass();
        Method methodGet = classObservers.getDeclaredMethod("get", Object.class);
        methodGet.setAccessible(true);
        Object objectWrapperEntry = methodGet.invoke(objectObservers, observer);
        Object objectWrapper = null;
        if (objectWrapperEntry instanceof Map.Entry) {
            objectWrapper = ((Map.Entry) objectWrapperEntry).getValue();
        }
        if (objectWrapper == null) {
            throw new NullPointerException("Wrapper can not be bull!");
        }
        Class<?> classObserverWrapper = objectWrapper.getClass().getSuperclass();
        Field fieldLastVersion = classObserverWrapper.getDeclaredField("mLastVersion");
        fieldLastVersion.setAccessible(true);
        //get livedata's version
        Field fieldVersion = classLiveData.getDeclaredField("mVersion");
        fieldVersion.setAccessible(true);
        Object objectVersion = fieldVersion.get(this);
        //set wrapper's version
        fieldLastVersion.set(objectWrapper, objectVersion);
    }
}
