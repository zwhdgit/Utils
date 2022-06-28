package com.zwh.wifip2putil.task;

import android.util.Log;

import androidx.annotation.Nullable;

import com.zwh.wifip2putil.callback.AcceptListener;
import com.zwh.wifip2putil.callback.MsgListener;
import com.zwh.wifip2putil.common.Constants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class WifiServiceTask extends Thread {


    private ServerSocket serverSocket;
    private Socket socket;
    private InputStream inputStream;
    private String TAG;

    private MsgListener msgListener;

    public void setMsgListener(MsgListener msgListener) {
        this.msgListener = msgListener;
    }

    private boolean isValidSocket() {
        return socket != null && !socket.isClosed();
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
                        writerStream.writeUTF("服务器消息：" + msg); // 写一个UTF-8的信息
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

    /**
     * 建立连接后保持循环监听消息
     */
    @Override
    public void run() {
        DataInputStream reader;
        try {
            if (socket == null || socket.isClosed()) {
                serverSocket = new ServerSocket();
                serverSocket.setReuseAddress(true);
                serverSocket.bind(new InetSocketAddress(Constants.PORT));
                socket = serverSocket.accept();
            }

            Log.e(TAG, "客户端IP地址 : " + socket.getInetAddress().getHostAddress());
            inputStream = socket.getInputStream();

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
            e.printStackTrace();
            clean();
        }

    }
}
