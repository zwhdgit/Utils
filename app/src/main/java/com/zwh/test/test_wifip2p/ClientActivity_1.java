package com.zwh.test.test_wifip2p;

import android.app.Activity;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zwh.test.R;
import com.zwh.test.adapter.DeviceAdapter;
import com.zwh.wifip2putil.WifiP2pHelper;
import com.zwh.wifip2putil.callback.DirectActionListener;
import com.zwh.wifip2putil.callback.MsgListener;
import com.zwh.wifip2putil.task.WifiClientTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class ClientActivity_1 extends Activity {

    private EditText text;
    private Button disconnect;
    private Button send;
    private TextView log;
    private String TAG = getClass().getSimpleName();
    private List<WifiP2pDevice> mWifiP2pDeviceList = new ArrayList<>();
    private DeviceAdapter deviceAdapter;
    private RecyclerView rv;
    private WifiP2pDevice curConnectDevice;
    private WifiP2pHelper wifiP2pHelper;
    private WifiClientTask wifiClientTask;
    private WifiP2pInfo wifiP2pInfo;

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
            }

            /**
             * 连接成功开始socket通信
             * @param wifiP2pInfo
             */
            @Override
            public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
                mWifiP2pDeviceList.clear();
                deviceAdapter.notifyDataSetChanged();
                disconnect.setEnabled(true);
                send.setEnabled(true);

                ClientActivity_1.this.wifiP2pInfo = wifiP2pInfo;
                startSocket();
            }

            @Override
            public void onDisconnection() {
                disconnect.setEnabled(false);
                send.setEnabled(false);
                showToast("处于非连接状态");
                mWifiP2pDeviceList.clear();
                deviceAdapter.notifyDataSetChanged();
                ClientActivity_1.this.wifiP2pInfo = null;
            }

            @Override
            public void onSelfDeviceAvailable(WifiP2pDevice wifiP2pDevice) {

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

            }
        });
    }

    private void startSocket() {
        wifiClientTask = new WifiClientTask(wifiP2pInfo.groupOwnerAddress.getHostAddress());
        wifiClientTask.setMsgListener(new MsgListener() {
            @Override
            public void onReceiveMsg(String msg) {
                ClientActivity_1.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addLog("服务端：" + msg);
                    }
                });
            }
        });
        wifiClientTask.start();
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
