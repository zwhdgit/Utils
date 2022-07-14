package com.baec.app_my.base

import android.annotation.TargetApi
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.graphics.ColorUtils
import androidx.palette.graphics.Palette

class AutoImmersiveStatusBar(var activity: Activity) {

    init {
        setTransparentBar()
    }

    fun setTransparentBar() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return
        }
        transparentStatusBar()
        setRootView()
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun transparentStatusBar() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        activity.window.statusBarColor = Color.TRANSPARENT
//        } else {
//            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//        }
    }

    fun setRootView() {
        val parent = activity.findViewById<View>(android.R.id.content) as ViewGroup
        var i = 0
        val count = parent.childCount
        while (i < count) {
            val childView = parent.getChildAt(i)
            if (childView is ViewGroup) {
                childView.setFitsSystemWindows(true)
                childView.clipToPadding = true
            }
            i++
        }
    }

    fun setBarStyle(view: View) {
        val background = view.background
        if (background is BitmapDrawable) {
            setBarStyle(background.bitmap)
        } else if (background is ColorDrawable) {
            setStatusBar(background.color)
        }
    }

    fun setBarStyle(bitmap: Bitmap) {
        val colorCount = 5
        val left = 0
        val top = 0
        val right = getScreenWidth()
        val bottom = getStatusBarHeight()
        Palette
            .from(bitmap)
            .maximumColorCount(colorCount)
            .setRegion(left, top, right, bottom)
            .generate {
                it?.let { palette ->
                    var mostPopularSwatch: Palette.Swatch? = null
                    for (swatch in palette.swatches) {
                        if (mostPopularSwatch == null
                            || swatch.population > mostPopularSwatch.population
                        ) {
                            mostPopularSwatch = swatch
                        }
                    }
                    mostPopularSwatch?.let { swatch ->
                        setStatusBar(swatch.rgb)
                    }
                }
            }


    }

    fun setStatusBar(color: Int) {
        val luminance = ColorUtils.calculateLuminance(color)
//                        val luminance = getBright(bitmap)
        // 当luminance小于0.5时，我们认为这是一个深色值.
        if (luminance < 0.5) {
//                        if (luminance < 128) {
            setDarkStatusBar()
        } else {
            setLightStatusBar()
        }
    }

    fun setLightStatusBar() {
        val window = activity.window
        val flags = window.decorView.systemUiVisibility
        window.decorView.systemUiVisibility = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    fun setDarkStatusBar() {
        val window = activity.window
        val flags = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.decorView.systemUiVisibility = flags xor View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    fun getScreenWidth(): Int {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

    fun getStatusBarHeight(): Int {
        val resources = activity.resources
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

//    fun onDestroy() {
//        activity=null
//    }

}