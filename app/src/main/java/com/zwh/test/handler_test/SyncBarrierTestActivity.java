package com.zwh.test.handler_test;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.zwh.test.R;
import com.zwh.test.databinding.ActivityTestBinding;

import java.lang.reflect.Method;


public class SyncBarrierTestActivity extends AppCompatActivity {

    private final String TAG = "SyncBarrierTestActivity";

    private Handler handler;
    private int token;
    public boolean b;

    public static final int MESSAGE_TYPE_SYNC = 1;
    public static final int MESSAGE_TYPE_ASYNC = 2;
    private ActivityTestBinding binding;
    private MessageQueue.IdleHandler idleHandler;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
        initHandler();
        initListener();
    }

    private void initListener() {
        binding.requestPermissions.setText("发送/删除 同步屏障");
        binding.bt0.setText("异步消息");
        binding.bt1.setText("同步消息");
        binding.requestPermissions.setOnClickListener(v -> sendAndDeleteBarrier());
        binding.bt0.setOnClickListener(v -> sendAsyncMessage());
        binding.bt1.setOnClickListener(v -> sendSyncMessage());
    }

    private void initView() {
        View view = getLayoutInflater().inflate(R.layout.activity_test, null);
        setContentView(view);
        DataBindingUtil.bind(view);
        binding = DataBindingUtil.getBinding(view);
    }

    private void initHandler() {
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if (msg.what == MESSAGE_TYPE_SYNC) {
                    Log.e(TAG, "收到同步消息");
                } else if (msg.what == MESSAGE_TYPE_ASYNC) {
                    Log.e(TAG, "收到异步消息");
                }
                return false;
            }
        });
        idleHandler = new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                Log.e(TAG, "queueIdle: 消息队列空闲");
//                handler.sendMessageDelayed(Message.obtain(), 2000);
                return false;
            }
        };
        handler.getLooper().getQueue().addIdleHandler(idleHandler);

    }

    /**
     * 发送普通(同步)消息
     */
    public void sendSyncMessage() {
        Log.e(TAG, "插入同步消息");
        Message message = Message.obtain();
        message.what = MESSAGE_TYPE_SYNC;
        message.setAsynchronous(false);
        handler.sendMessageDelayed(message, 1000);
    }

    /**
     * 发送异步消息
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public void sendAsyncMessage() {
        Log.e(TAG, "插入异步消息");
        Message message = Message.obtain();
        message.what = MESSAGE_TYPE_ASYNC;
        message.setAsynchronous(true);
        handler.sendMessageDelayed(message, 1000);
    }


    public void sendAndDeleteBarrier() {
        if (!b) {
            sendSyncBarrier();
        } else {
            removeSyncBarrier();
        }
    }

    /**
     * 往消息队列插入同步屏障
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void sendSyncBarrier() {
        try {
            Log.e(TAG, "插入同步屏障");
            MessageQueue queue = handler.getLooper().getQueue();
            Method method = MessageQueue.class.getDeclaredMethod("postSyncBarrier");
            method.setAccessible(true);
            token = (int) method.invoke(queue);
            b = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从消息队列移除同步屏障
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void removeSyncBarrier() {
        try {
            Log.e(TAG, "移除屏障");
            MessageQueue queue = handler.getLooper().getQueue();
            Method method = MessageQueue.class.getDeclaredMethod("removeSyncBarrier", int.class);
            method.setAccessible(true);
            method.invoke(queue, token);
            b = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
