package com.zwh.test;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class MyApplication extends Application implements Thread.UncaughtExceptionHandler {


    private String TAG;

    public static String SCREEN_CAPTURE_CHANNEL_ID = "Screen Capture ID";
    public static String SCREEN_CAPTURE_CHANNEL_NAME = "Screen Capture";

    @Override
    public void onCreate() {
        super.onCreate();
//        Thread.setDefaultUncaughtExceptionHandler(this::uncaughtException);
        createScreenCaptureNotificationChannel();
    }

    private void createScreenCaptureNotificationChannel() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Create the channel for the notification
        NotificationChannel screenCaptureChannel = new NotificationChannel(SCREEN_CAPTURE_CHANNEL_ID, SCREEN_CAPTURE_CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
        // Set the Notification Channel for the Notification Manager.
        notificationManager.createNotificationChannel(screenCaptureChannel);

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
        saveCrashInfo2File(e);
    }

    private void saveCrashInfo2File(Throwable ex) {
//        Map<String, String> info = new HashMap<>();
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "test.txt");
//        File file = new File(getCacheDir(), "test" + "/" + "test.txt");
        try {
            boolean newFile = file.createNewFile();

        } catch (Exception e) {
            Log.e(TAG, "saveCrashInfo2File: " + e.toString());
        }
        Log.e(TAG, "saveCrashInfo2File: " + file.exists());
        StringBuffer sb = new StringBuffer();
        sb.append(new Date().toString() + "：发生崩溃的异常，设备的信息如下：******************************************************分割线***********************" + "\r\n");
//        for (Map.Entry<String, String> entry : info.entrySet()) {
//            String key = entry.getKey();
//            String value = entry.getValue();
//            sb.append(key + "\t=\t" + value + "\r\n");
//        }
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        ex.printStackTrace(pw);
        Throwable cause = ex.getCause();
        // 循环着把所有的异常信息写入writer中
        while (cause != null) {
            cause.printStackTrace(pw);
            cause = cause.getCause();
        }
        pw.close();// 记得关闭
        String result = writer.toString();
        sb.append("发生崩溃的异常信息如下：" + "\r\n");
        sb.append(result);
        Log.e("TAG", result);
        // 保存文件
        try {
            //判断文件夹是否存在
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdir();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file, true);
            fos.write(sb.toString().getBytes("UTF-8"));
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

