package com.zwh.test;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.zwh.utils.log.DingDingErrorApi;
import com.zwh.utils.log.ErrorBean;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostErrorService extends Service {

    private String TAG = "PostErrorService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String error = intent.getStringExtra("error");
        postLandscapeError(error);
        return super.onStartCommand(intent, flags, startId);
    }


    public void postLandscapeError(String s) {
        ErrorBean errorBean = new ErrorBean();
        ErrorBean.TextBean textBean = new ErrorBean.TextBean();
        textBean.setContent("方向盘演示版异常：" + s);
        errorBean.setText(textBean);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://oapi.dingtalk.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        DingDingErrorApi dingDingErrorApi = retrofit.create(DingDingErrorApi.class);
        dingDingErrorApi.postLandscapeError(errorBean)
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(Object value) {
                        Log.e(TAG, "onNext: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: ");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}


