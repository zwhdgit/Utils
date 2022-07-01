package com.zwh.wifip2putil.task;

import com.zwh.wifip2putil.callback.MsgListener;
import com.zwh.wifip2putil.common.WifiP2pConstants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WifiClientTask extends Thread {

    private final String address;

    private Socket mSocket;

    private MsgListener msgListener;
    private DataInputStream readerStream;
    private DataOutputStream writerStream;

    public void setMsgListener(MsgListener msgListener) {
        this.msgListener = msgListener;
    }


    public WifiClientTask(String address) {
        this.address = address;
    }

    private boolean isValidSocket() {
        return mSocket != null && !mSocket.isClosed();
    }

//    public ExceptionListener exceptionListener;
//
//    public void setExceptionListener(ExceptionListener exceptionListener) {
//        this.exceptionListener = exceptionListener;
//    }
//
//    public interface ExceptionListener{
//        void onException();
//    }
    /**
     * 连接线程
     */
//wifiP2pInfo.groupOwnerAddress.getHostAddress()
    @Override
    public void run() {
        try {
            //指定ip地址和端口号
            mSocket = new Socket(address, WifiP2pConstants.PORT);
            //获取输出流、输入流
//                mOutStream = mSocket.getOutputStream();
//                mInStream = mSocket.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        startReader();
    }

    /**
     * 发送消息
     *
     * @param msg
     */
    public void sendMessage(final String msg) {
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                {
                    try {
                        if (isValidSocket()) {
                            writerStream = new DataOutputStream(mSocket.getOutputStream());
                            writerStream.writeUTF("" + msg); // 写一个UTF-8的信息
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
//                        WifiClientTask.this.run();
                    }
                }
            }
        });

    }

    /**
     * 接收消息
     */
    private void startReader() {
        new Thread() {
            @Override
            public void run() {
                readerStream = null;
                try {
                    // 获取读取流
                    readerStream = new DataInputStream(mSocket.getInputStream());
                    while (isValidSocket()) {
                        // 读取数据
                        String msg = readerStream.readUTF();
                        if (msgListener != null) {
                            msgListener.onReceiveMsg(msg);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void clean() {
        if (isValidSocket()) {
            try {
                mSocket.close();
                mSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (readerStream != null) {
            try {
                readerStream.close();
                readerStream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (writerStream != null) {
            try {
                writerStream.close();
                writerStream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
