package com.zwh.utils.log;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * UncaughtExceptionHandler 是一个当线程由于未捕获的异常突然终止而调用处理程序的接口.
 */
public class OwnUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    //    private String TAG = getClass().getSimpleName();
    private static String TAG = "OwnUncaughtExceptionHandler";

    @Override

    public void uncaughtException(Thread thread, Throwable ex) {

        StackTraceElement[] elements = ex.getStackTrace();

        StringBuilder reason = new StringBuilder(ex.toString());

        if (elements != null && elements.length > 0) {

            for (StackTraceElement element : elements) {

                reason.append("\n");

                reason.append(element.toString());

            }

        }

//        Log.e(TAG, reason.toString());
        postLandscapeError(reason.toString());

        System.out.println("uncaughtException");


    }

    public static void postLandscapeError(String s) {
        ErrorBean errorBean = new ErrorBean();
        ErrorBean.TextBean textBean = new ErrorBean.TextBean();
        textBean.setContent("方向盘演示版异常：" + s.substring(0,1000));
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
                        Log.e(TAG, "onNext: " + value.toString());
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