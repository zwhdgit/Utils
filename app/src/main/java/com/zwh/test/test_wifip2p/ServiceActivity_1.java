package com.zwh.test.test_wifip2p;

import android.app.Service;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.zwh.test.R;
import com.zwh.wifip2putil.WifiP2pHelper;
import com.zwh.wifip2putil.callback.DirectActionListener;
import com.zwh.wifip2putil.callback.MsgListener;
import com.zwh.wifip2putil.task.WifiServiceTask;

import java.util.Collection;
import java.util.Date;

public class ServiceActivity_1 extends AppCompatActivity {

    private TextView log;
    private WifiP2pHelper wifiP2pHelper;
    private String TAG = getClass().getSimpleName();

    private View send;

    private WifiServiceTask serviceTask = new WifiServiceTask() {
        {
            setMsgListener(new MsgListener() {
                @Override
                public void onReceiveMsg(String msg) {
                    ServiceActivity_1.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            addLog("客户端：" + msg);
                        }
                    });
                }
            });
        }
    };
    private String curClientDeviceName = "";
    private boolean connectionInfoAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        initView();
        initData();
    }

    private void initView() {
        log = findViewById(R.id.log);

        findViewById(R.id.createGroup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wifiP2pHelper.createGroup(new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        showToast("成功");
                    }

                    @Override
                    public void onFailure(int reason) {
                        showToast("失败");
                    }
                });
            }
        });

        findViewById(R.id.deleteGroup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wifiP2pHelper.deleteGroup(new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        showToast("成功");
                    }

                    @Override
                    public void onFailure(int reason) {
                        showToast("失败");
                    }
                });
            }
        });

        EditText text = findViewById(R.id.text);
        send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceTask.sendMsg(text.getText().toString());
            }
        });
    }

    private void initData() {
        wifiP2pHelper = WifiP2pHelper.getInstance();
        wifiP2pHelper.init(getApplication(), new DirectActionListener() {
            @Override
            public void wifiP2pEnabled(boolean enabled) {
                if (!enabled) {
                    showToast("请打开wifi进行连接");
                }
                Log.e(TAG, "wifiP2pEnabled: " + enabled);
            }

            /**
             * 1、创建群组成功，本机也会连接回调
             * 2、其他设备连接时
             * @param wifiP2pInfo
             */
            @Override
            public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
                if (!connectionInfoAvailable) {
                    startConnectTask();
                    connectionInfoAvailable = true;
                }
            }
            /**
             * 删除群组时调用
             */
            @Override
            public void onDisconnection() {
                connectionInfoAvailable = false;
            }

            @Override
            public void onSelfDeviceAvailable(WifiP2pDevice wifiP2pDevice) {
            }

            @Override
            public void onPeersAvailable(Collection<WifiP2pDevice> wifiP2pDeviceList) {
                for (WifiP2pDevice wifiP2pDevice : wifiP2pDeviceList) {
                    String deviceName = wifiP2pDevice.deviceName;
                    int status = wifiP2pDevice.status;
                    if (status == 0) {
                        send.setEnabled(true);
                        curClientDeviceName = wifiP2pDevice.deviceName;
                    } else if (curClientDeviceName.equals(deviceName) && wifiP2pDevice.status == 3) {
                        startConnectTask();
                        send.setEnabled(false);
                    }
                }
            }

            @Override
            public void onChannelDisconnected() {
                showToast("断开连接");
            }
        });

        wifiP2pHelper.deleteGroup(null);
    }

    private void startConnectTask() {
        serviceTask.start();
        curClientDeviceName = "";
    }

    private void addLog(String stringBuilder) {
        String s = new Date().toString() + "\n" + stringBuilder + "\n******************************************\n" + log.getText().toString();
        log.setText(s);
    }

    protected <T extends Service> void startService(Class<T> tClass) {
        startService(new Intent(this, tClass));
    }

    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        serviceTask.clean();
        wifiP2pHelper.onDestroy();
    }
}
