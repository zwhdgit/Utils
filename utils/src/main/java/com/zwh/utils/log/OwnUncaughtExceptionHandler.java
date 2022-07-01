package com.zwh.utils.log;

import android.util.Log;

/**
 * 是一个当线程由于未捕获的异常突然终止而调用处理程序的接口.
 */
public class OwnUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    private String TAG=getClass().getSimpleName();

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

        Log.e(TAG, reason.toString());


        android.os.Process.killProcess(android.os.Process.myPid());

    }

}