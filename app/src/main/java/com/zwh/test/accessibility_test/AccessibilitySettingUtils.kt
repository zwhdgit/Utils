package com.zwh.test.accessibility_test

import android.content.Context
import android.content.Intent
import android.provider.Settings

object AccessibilitySettingUtils {
    var name:String="";
    fun jumpToAccessibilitySetting(context: Context) {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        context.startActivity(intent)
    }
}