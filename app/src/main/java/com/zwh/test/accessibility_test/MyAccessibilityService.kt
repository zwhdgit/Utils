package com.zwh.test.accessibility_test

import android.accessibilityservice.AccessibilityService
import android.hardware.display.DisplayManager
import android.media.projection.MediaProjectionManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import kotlin.concurrent.thread

class MyAccessibilityService : AccessibilityService() {

    val handler = Handler(Looper.myLooper()!!) {
        sendMessage()
        true
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d("~~~", "onServiceConnected")
//        thread {
//            Thread.sleep(5000)
//            ClickUtils.click(this, 254f, 577f)
//        }
        sendMessage()

    }

    fun sendMessage() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                Log.d("~~~", "ClickUtils.x = " + ClickUtils.x + ",ClickUtils.y=" + ClickUtils.y)
                if (ClickUtils.x != 0.0f) {
                    ClickUtils.click(this@MyAccessibilityService, ClickUtils.x, ClickUtils.y)
                    ClickUtils.x = 0.0f
                    ClickUtils.y = 0.0f
                }
            }

        }, 1000)
    }

    //  当界面发生改变时，这个方法就会被调用，界面改变的具体信息就会包含在这个参数中
    override fun onAccessibilityEvent(accessibilityEvent: AccessibilityEvent?) {
//        Log.e("TAG", "onAccessibilityEvent: "+accessibilityEvent.toString() )
    }

    //  方法辅助服务被中断了。
    override fun onInterrupt() {

    }

}


