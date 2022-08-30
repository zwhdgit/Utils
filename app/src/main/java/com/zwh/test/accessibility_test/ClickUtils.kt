package com.zwh.test.accessibility_test

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.util.Log

object ClickUtils {
    @Volatile
    var x: Float = 0.0F
    @Volatile
    var y: Float = 0.0F
    fun click(accessibilityService: AccessibilityService, x: Float, y: Float) {
        Log.d("~~~", "click: ($x, $y)")
        val builder = GestureDescription.Builder()
        val path = Path()
        path.moveTo(x, y)
        path.lineTo(x, y)
        builder.addStroke(GestureDescription.StrokeDescription(path, 0, 1))
        val gesture = builder.build()
        accessibilityService.dispatchGesture(
            gesture,
            object : AccessibilityService.GestureResultCallback() {
                override fun onCancelled(gestureDescription: GestureDescription) {
                    super.onCancelled(gestureDescription)
                }

                override fun onCompleted(gestureDescription: GestureDescription) {
                    super.onCompleted(gestureDescription)
                }
            },
            null
        )
    }
}