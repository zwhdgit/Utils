package com.zwh.wifip2putil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Looper;

import androidx.core.app.ActivityCompat;

import com.zwh.wifip2putil.broadcast.DirectBroadcastReceiver;
import com.zwh.wifip2putil.callback.DirectActionListener;

public class WifiP2pHelper {

    private Application context;
    private String TAG;

    public static WifiP2pHelper getInstance() {
        return new WifiP2pHelper();
    }

    private WifiP2pManager wifiP2pManager;
    private WifiP2pManager.Channel channel;
    private DirectBroadcastReceiver broadcastReceiver;

    public boolean init(Application context, DirectActionListener directActionListener) {
        this.context = context;
        wifiP2pManager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
        if (wifiP2pManager == null) {
//            finish();
            return false;
        }
        channel = wifiP2pManager.initialize(context, Looper.getMainLooper(), directActionListener);
        broadcastReceiver = new DirectBroadcastReceiver(wifiP2pManager, channel, directActionListener);
        context.registerReceiver(broadcastReceiver, DirectBroadcastReceiver.getIntentFilter());
        return true;
    }


    /**
     *
     */
    public void search(WifiP2pManager.ActionListener actionListener) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        wifiP2pManager.discoverPeers(channel, actionListener);
    }

    /**
     * 连接设备，需要权限
     * isOpenConnect 仅代表成功开启连接，不等于连接成功，连接成功回调于广播
     *
     * @return
     */
    public void connect(WifiP2pDevice mWifiP2pDevice, WifiP2pManager.ActionListener actionListener) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            showToast("请先授予位置权限");
            return;
        }
        WifiP2pConfig config = new WifiP2pConfig();
        if (config.deviceAddress != null && mWifiP2pDevice != null) {
            config.deviceAddress = mWifiP2pDevice.deviceAddress;
            config.wps.setup = WpsInfo.PBC;
//            showLoadingDialog("正在连接 " + mWifiP2pDevice.deviceName);
            wifiP2pManager.connect(channel, config, actionListener);
        }
    }

    public void disconnect(WifiP2pManager.ActionListener actionListener) {
        wifiP2pManager.removeGroup(channel, actionListener);

    }

    @SuppressLint("MissingPermission")
    public void createGroup(WifiP2pManager.ActionListener actionListener) {
        if (checkPer()) {
            return;
        }
        //  建议不管成功失败先尝试关闭群组
//        deleteGroup(null);
        wifiP2pManager.createGroup(channel, actionListener);
    }

    @SuppressLint("MissingPermission")
    public void deleteGroup(WifiP2pManager.ActionListener actionListener) {
        if (checkPer()) {
            return;
        }
        wifiP2pManager.removeGroup(channel, actionListener);
    }

    /**
     * 检查必要权限 ACCESS_FINE_LOCATION
     */
    public boolean checkPer() {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED;
    }

    public void onDestroy() {
        disconnect(null);
        context.unregisterReceiver(broadcastReceiver);
        context = null;
    }
}
