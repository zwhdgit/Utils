package com.zwh.test.viewmodel;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class BluBroadcast {
    public static BroadcastReceiver mStatusReceive = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()) {

                case BluetoothAdapter.ACTION_STATE_CHANGED:

                    int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);

                    switch (blueState) {

                        case BluetoothAdapter.STATE_TURNING_ON:

                            break;

                        case BluetoothAdapter.STATE_ON:

//开始扫描

//                            scanLeDevice(true);

                             break;

                        case BluetoothAdapter.STATE_TURNING_OFF:

                            break;

                        case BluetoothAdapter.STATE_OFF:

                            break;

                    }

                    break;

            }

        }

    };
}
