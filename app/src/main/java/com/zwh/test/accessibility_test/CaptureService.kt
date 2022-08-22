package com.zwh.test.accessibility_test

import android.app.Service
import android.content.Intent
import android.content.ServiceConnection
import android.hardware.display.DisplayManager
import android.media.projection.MediaProjectionManager
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.zwh.test.MyApplication.SCREEN_CAPTURE_CHANNEL_ID

class CaptureService : Service() {

    private val binder: MyBind = MyBind()

    val handler = Handler(Looper.myLooper()!!) {
        true
    }
    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun bindService(service: Intent?, conn: ServiceConnection, flags: Int): Boolean {

        startForeground(1, NotificationCompat.Builder(this, SCREEN_CAPTURE_CHANNEL_ID).build())
        return super.bindService(service, conn, flags)
    }


    private val mediaProjectionManager: MediaProjectionManager by lazy {
        getSystemService(
            MEDIA_PROJECTION_SERVICE
        ) as MediaProjectionManager
    }

    class MyBind : Binder() {

    }

}