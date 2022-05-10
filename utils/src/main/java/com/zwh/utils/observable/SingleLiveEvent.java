package com.zwh.utils.observable;

import android.util.Log;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * google大神实现的一个复写类 SingleLiveEvent，
 * 其中的机制是用一个原子 AtomicBoolean记录一次setValue。在发送一次后在将AtomicBoolean设置为false，阻止后续前台重新触发时的数据发送。
 * @param <T>
 */
public class SingleLiveEvent<T> extends MutableLiveData<T> {
    private static final String TAG = "SingleLiveEvent";
    private final AtomicBoolean mPending = new AtomicBoolean(false);

    public SingleLiveEvent() {
    }

    @MainThread
    public void observe(@NonNull LifecycleOwner owner, @NonNull final Observer<? super T> observer) {
        if (this.hasActiveObservers()) {
            Log.w("SingleLiveEvent", "Multiple observers registered but only one will be notified of changes.");
        }

        super.observe(owner, new Observer<T>() {
            public void onChanged(@Nullable T t) {
                if (SingleLiveEvent.this.mPending.compareAndSet(true, false)) {
                    observer.onChanged(t);
                }

            }
        });
    }

    @MainThread
    public void setValue(@Nullable T t) {
        this.mPending.set(true);
        super.setValue(t);
    }

    @MainThread
    public void call() {
        this.setValue((T) null);
    }
}
