package com.zwh.wifip2putil.task;

import android.util.Log;


import com.zwh.wifip2putil.callback.MsgListener;
import com.zwh.wifip2putil.common.Constants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WifiServiceTask {


    private volatile ServerSocket serverSocket;
    private volatile Socket socket;
    private volatile InputStream readerStream;
    private String TAG = getClass().getSimpleName();

    private MsgListener msgListener;
    private DataOutputStream writerStream;

    public void setMsgListener(MsgListener msgListener) {
        this.msgListener = msgListener;
    }

    private boolean isValidSocket() {
        return socket != null && !socket.isClosed();
    }

    /**
     * 手动开启子线程发送消息至服务端
     * todo 优化线程池
     */
    public void sendMsg(String msg) {
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                {
                    try {
                        if (isValidSocket()) {
                            writerStream = new DataOutputStream(socket.getOutputStream());
                            writerStream.writeUTF("" + msg); // 写一个UTF-8的信息
                            Log.e(TAG, "消息发送成功: " + msg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    public void start() {
        clean();
        run();
    }

    /**
     * 建立连接后保持循环监听消息
     */
    private void run() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DataInputStream reader;
                try {
//            if (socket == null || socket.isClosed()) {
                    Log.e(TAG, "build socket");
                    serverSocket = new ServerSocket();
                    serverSocket.setReuseAddress(true);
                    serverSocket.bind(new InetSocketAddress(Constants.PORT));
                    Log.e(TAG, "等待客户端连接");
                    socket = serverSocket.accept();
//            }

                    Log.e(TAG, "客户端IP地址 : " + socket.getInetAddress().getHostAddress());
                    readerStream = socket.getInputStream();

                    // 获取读取流
                    reader = new DataInputStream(socket.getInputStream());
                    while (isValidSocket()) {
                        // 读取数据
                        String msg = reader.readUTF();
                        Log.e(TAG, "客户端的信息:" + msg);
                        if (msgListener != null) {
                            msgListener.onReceiveMsg(msg);
                        }
//                 告知客户端消息收到
                        DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
                        writer.writeUTF(msg); // 写一个UTF-8的信息

                    }

                } catch (IOException e) {
                    Log.e(TAG, "socket失败：" + e.getMessage());
                    e.printStackTrace();
//                    start();
                } catch (Exception e) {
                    Log.e(TAG, "其他异常" + e.getMessage());
                }
            }
        }).start();
    }


    public void clean() {
        if (isValidSocket()) {
            try {
                socket.close();
                socket = null;
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
