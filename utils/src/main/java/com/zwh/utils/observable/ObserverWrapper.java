package com.zwh.utils.observable;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

/**
 * 不知道有啥用
 *
 * @param <T>
 */
public class ObserverWrapper<T> implements Observer<T> {

    private Observer<T> observer;

    public ObserverWrapper(Observer<T> observer) {
        this.observer = observer;
    }

    @Override
    public void onChanged(@NonNull T t) {
        if (observer != null) {
            if (isCallOnObserve()) {
                return;
            }
            observer.onChanged(t);
        }
    }

    private boolean isCallOnObserve() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace != null && stackTrace.length > 0) {
            for (StackTraceElement element : stackTrace) {
                if ("android.arch.lifecycle.LiveData".equals(element.getClassName()) &&
                        "observeForever".equals(element.getMethodName())) {
                    return true;
                }
            }
        }
        return false;
    }
}