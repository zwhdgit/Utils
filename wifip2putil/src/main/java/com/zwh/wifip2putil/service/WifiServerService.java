package com.zwh.wifip2putil.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.zwh.wifip2putil.callback.MsgListener;
import com.zwh.wifip2putil.common.Constants;
import com.zwh.wifip2putil.model.FileTransfer;
import com.zwh.wifip2putil.util.Md5Util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * @Author: leavesC
 * @Date: 2018/2/14 21:09
 * @Desc: 服务器端接收文件
 * @Github：https://github.com/leavesC
 */
public class WifiServerService extends Service {

    private static final String TAG = "WifiServerService";

    private ServerSocket serverSocket;

    private InputStream inputStream;

    private MsgListener msgListener;

    private Socket socket;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new WifiServerBinder();
    }

    /**
     * 建立连接后保持循环监听消息
     */
    public void receptionMsg() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DataInputStream reader;
                try {
                    serverSocket = new ServerSocket();
                    serverSocket.setReuseAddress(true);
                    serverSocket.bind(new InetSocketAddress(Constants.PORT));
                    socket = serverSocket.accept();

                    Log.e(TAG, "客户端IP地址 : " + socket.getInetAddress().getHostAddress());
                    inputStream = socket.getInputStream();

                    // 获取读取流
                    reader = new DataInputStream(socket.getInputStream());
                    while (!socket.isClosed()) {
                        // 读取数据
                        String msg = reader.readUTF();
                        Log.d(TAG, "客户端的信息:" + msg);
                        if (msgListener != null) {
                            msgListener.onReceiveMsg(msg);
                        }

                        //告知客户端消息收到
//                DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
//                writer.writeUTF("收到:" + msg); // 写一个UTF-8的信息

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private boolean isValidSocket() {
        return socket != null && !socket.isClosed();
    }

    /**
     * 手动开启子线程发送消息至服务端
     * todo 优化线程池
     */
    public void sendMsg(String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DataOutputStream writerStream = null;
                try {
                    if (isValidSocket()) {
                        writerStream = new DataOutputStream(socket.getOutputStream());
                        writerStream.writeUTF(msg); // 写一个UTF-8的信息
                        Log.e(TAG, "消息发送成功: " + msg);
                    }
                } catch (Exception e) {
                    try {
                        if (writerStream != null) {
                            writerStream.close();
                            writerStream = null;
                        }
                    } catch (IOException ioe) {
                        Log.e(TAG, "流关闭异常：" + ioe.getMessage());
                    }
                }

            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clean();
    }

    public void setMsgListener(MsgListener msgListener) {
        this.msgListener = msgListener;
    }

    private void clean() {
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
                socket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
                serverSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (inputStream != null) {
            try {
                inputStream.close();
                inputStream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public class WifiServerBinder extends Binder {
        public WifiServerService getService() {
            return WifiServerService.this;
        }
    }

}
