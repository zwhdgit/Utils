package com.zwh.test;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;

public class MyApplication extends Application implements Thread.UncaughtExceptionHandler {

    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(this::uncaughtException);
    }

    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {

        StackTraceElement[] elements = e.getStackTrace();

        StringBuilder reason = new StringBuilder(e.toString());

        if (elements != null && elements.length > 0) {

            for (StackTraceElement element : elements) {

                reason.append("\n");

                reason.append(element.toString());

            }

        }
        // 崩溃时开启新进程通知钉钉
        Intent intent = new Intent(getApplicationContext(), PostErrorService.class);
        intent.putExtra("error", reason.toString());
        startService(intent);
    }

}
