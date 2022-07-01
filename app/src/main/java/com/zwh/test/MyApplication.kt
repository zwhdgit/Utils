package com.zwh.test

import android.app.Application
import com.zwh.utils.log.OwnUncaughtExceptionHandler

class MyApplication :Application() {

    override fun onCreate() {
        super.onCreate()

        Thread.setDefaultUncaughtExceptionHandler(OwnUncaughtExceptionHandler())
    }
}