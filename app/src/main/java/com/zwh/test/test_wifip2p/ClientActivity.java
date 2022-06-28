package com.zwh.test.test_wifip2p;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zwh.test.R;
import com.zwh.test.adapter.DeviceAdapter;
import com.zwh.wifip2putil.WifiP2pHelper;
import com.zwh.wifip2putil.callback.DirectActionListener;
import com.zwh.wifip2putil.callback.MsgListener;
import com.zwh.wifip2putil.common.Constants;
import com.zwh.wifip2putil.task.WifiClientTask;
import com.zwh.wifip2putil.util.WifiP2pUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class ClientActivity extends AppCompatActivity {

    private EditText text;
    private Button disconnect;
    private Button send;
    private TextView log;
    private String TAG = getClass().getSimpleName();
    private WifiP2pInfo wifiP2pInfo;
    private List<WifiP2pDevice> mWifiP2pDeviceList = new ArrayList<>();
    private DeviceAdapter deviceAdapter;
    private RecyclerView rv;
    private WifiP2pDevice curConnectDevice;
    private WifiP2pHelper wifiP2pHelper;
    private WifiClientTask wifiClientTask;
    private Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        initView();
        initData();
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
             * 连接成功开始socket通信
             * @param wifiP2pInfo
             */
            @Override
            public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
                try {
                    mWifiP2pDeviceList.clear();
                    deviceAdapter.notifyDataSetChanged();
                    disconnect.setEnabled(true);
                    send.setEnabled(true);
                    Log.e(TAG, "onConnectionInfoAvailable");
                    Log.e(TAG, "onConnectionInfoAvailable groupFormed: " + wifiP2pInfo.groupFormed);
                    Log.e(TAG, "onConnectionInfoAvailable isGroupOwner: " + wifiP2pInfo.isGroupOwner);
                    Log.e(TAG, "onConnectionInfoAvailable getHostAddress: " + wifiP2pInfo.groupOwnerAddress.getHostAddress());
                    StringBuilder stringBuilder = new StringBuilder();
                    if (curConnectDevice != null) {
                        stringBuilder.append("连接的设备名：");
                        stringBuilder.append(curConnectDevice.deviceName);
                        stringBuilder.append("\n");
                        stringBuilder.append("连接的设备的地址：");
                        stringBuilder.append(curConnectDevice.deviceAddress);
                    }
                    stringBuilder.append("\n");
                    stringBuilder.append("是否群主：");
                    stringBuilder.append(wifiP2pInfo.isGroupOwner ? "是群主" : "非群主");
                    stringBuilder.append("\n");
                    stringBuilder.append("群主IP地址：");
                    stringBuilder.append(wifiP2pInfo.groupOwnerAddress.getHostAddress());
                    addLog(stringBuilder.toString());
//                if (wifiP2pInfo.groupFormed && !wifiP2pInfo.isGroupOwner) {
                    ClientActivity.this.wifiP2pInfo = wifiP2pInfo;
//                }

                    wifiClientTask = new WifiClientTask(wifiP2pInfo.groupOwnerAddress.getHostAddress());
                    wifiClientTask.setMsgListener(new MsgListener() {
                        @Override
                        public void onReceiveMsg(String msg) {
                            ClientActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    addLog("服务端：" + msg);
                                }
                            });
                        }
                    });
                    wifiClientTask.start();
                } catch (Exception e) {
                    Log.e(TAG, "onConnectionInfoAvailable: " + e.getMessage());
                }
            }

            @Override
            public void onDisconnection() {
                Log.e(TAG, "onDisconnection");
                disconnect.setEnabled(false);
                send.setEnabled(false);
                showToast("处于非连接状态");
                mWifiP2pDeviceList.clear();
                deviceAdapter.notifyDataSetChanged();
//                tv_status.setText(null);
                ClientActivity.this.wifiP2pInfo = null;
            }

            @Override
            public void onSelfDeviceAvailable(WifiP2pDevice wifiP2pDevice) {
                Log.e(TAG, "onSelfDeviceAvailable");
                Log.e(TAG, "DeviceName: " + wifiP2pDevice.deviceName);
                Log.e(TAG, "DeviceAddress: " + wifiP2pDevice.deviceAddress);
                Log.e(TAG, "Status: " + wifiP2pDevice.status);
                StringBuilder sb = new StringBuilder();
                sb.append("本设备信息：\n");
                sb.append("设备名称：" + wifiP2pDevice.deviceName + "\n");
                sb.append("设备地址：" + wifiP2pDevice.deviceAddress + "\n");
                sb.append("设备状态：" + WifiP2pUtils.getDeviceStatus(wifiP2pDevice.status));
                addLog(sb.toString());
            }

            @Override
            public void onPeersAvailable(Collection<WifiP2pDevice> wifiP2pDeviceList) {
                // 搜索到设备列表
                mWifiP2pDeviceList.clear();
                mWifiP2pDeviceList.addAll(wifiP2pDeviceList);
                deviceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChannelDisconnected() {
                // 断开连接
                Log.e(TAG, "onChannelDisconnected");
            }
        });
    }


    private void addLog(String stringBuilder) {
        String s = new Date().toString() + "\n" + stringBuilder + "\n******************************************\n" + log.getText().toString();
        log.setText(s);
    }

    private void initView() {
        View search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wifiP2pHelper.search(new WifiP2pManager.ActionListener() {
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

        disconnect = findViewById(R.id.disconnect);
        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wifiP2pHelper.disconnect(null);
            }
        });

        text = findViewById(R.id.text);
        send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wifiClientTask.sendMessage(text.getText().toString());
//                sendMessage(text.getText().toString());
            }
        });

        log = findViewById(R.id.log);

        rv = findViewById(R.id.rv_deviceList);
        rv.setLayoutManager(new LinearLayoutManager(this));
        deviceAdapter = new DeviceAdapter(mWifiP2pDeviceList);
        deviceAdapter.setClickListener(new DeviceAdapter.OnClickListener() {
            @Override
            public void onItemClick(int position) {
                curConnectDevice = mWifiP2pDeviceList.get(position);
                wifiP2pHelper.connect(curConnectDevice, null);
            }
        });
        rv.setAdapter(deviceAdapter);
    }

    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wifiP2pHelper.onDestroy();
        wifiClientTask.clean();
    }
}