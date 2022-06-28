package com.zwh.wifip2putil.task;

import android.util.Log;

import com.zwh.wifip2putil.callback.MsgListener;
import com.zwh.wifip2putil.common.Constants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class WifiClientTask extends Thread {

    private final String address;

    private Socket mSocket;

    private MsgListener msgListener;

    public void setMsgListener(MsgListener msgListener) {
        this.msgListener = msgListener;
    }


    public WifiClientTask(String address) {
        this.address = address;
    }

    private boolean isValidSocket() {
        return mSocket != null && !mSocket.isClosed();
    }

    /**
     * 连接线程
     */
//wifiP2pInfo.groupOwnerAddress.getHostAddress()
    @Override
    public void run() {
        try {
            //指定ip地址和端口号
            mSocket = new Socket(address, Constants.PORT);
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
        new Thread() {
            @Override
            public void run() {
                try {
                    if (isValidSocket()) {
                        DataOutputStream writer = new DataOutputStream(mSocket.getOutputStream());
                        writer.writeUTF("客户端发送消息："+msg); // 写一个UTF-8的信息
                    }

                } catch (IOException e) {
                    //todo 异常处理
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 接收消息
     */
    private void startReader() {
        new Thread() {
            @Override
            public void run() {
                DataInputStream reader;
                try {
                    // 获取读取流
                    reader = new DataInputStream(mSocket.getInputStream());
                    while (isValidSocket()) {
                        // 读取数据
                        String msg = reader.readUTF();
                        if (msgListener != null) {
                            msgListener.onReceiveMsg(msg);
                        }

//                        Message message = new Message();
//                        message.what = 1;
//                        message.obj = msg;
//                        handler.sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
