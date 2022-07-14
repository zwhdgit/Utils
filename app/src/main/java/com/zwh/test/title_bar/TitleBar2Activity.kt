package com.zwh.test.title_bar

import android.annotation.TargetApi
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.core.graphics.ColorUtils
import androidx.palette.graphics.Palette
import com.baec.app_my.base.AutoImmersiveStatusBar
import com.zwh.test.R

/**
 * https://github.com/laobie/StatusBarUtil
 *
 * 布局里可以不用配置，有时间融合一下郭霖老师的自适应方案
 * todo 有个问题，activity根布局铺满后是如何处理的子布局没有重叠状态栏
 */
class TitleBar2Activity : AppCompatActivity() {
    val TAG: String = javaClass.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title_bar2)
//        StatusBarUtil.setColor(this, getColor(R.color.transp))
//        StatusBarUtil.setTransparent(this)
//        StatusBarUtil.setTranslucent(this)
//        setTransparent(this)
        val parent = findViewById<View>(android.R.id.content) as ViewGroup
        val rootView = parent.getChildAt(0) as ViewGroup
//        val background = rootView.background as BitmapDrawable
//        setBarStyle(background.bitmap)
//        setBarStyle(rootView)
        findViewById<Button>(R.id.bt0).setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                AutoImmersiveStatusBar(this@TitleBar2Activity).setBarStyle(rootView)
            }
        })
    }

    fun setTransparent(activity: Activity?) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return
        }
        transparentStatusBar(activity!!)
        setRootView(activity!!)
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun transparentStatusBar(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            activity.window.statusBarColor = Color.TRANSPARENT
        } else {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    private fun setRootView(activity: Activity) {
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

//    fun setBg(bgId: Int) {
//        val bitmap = BitmapFactory.decodeResource(resources, bgId)
//        bgView.setImageBitmap(bitmap)
//        setBarStyle(bitmap)
//    }

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

    private fun getBright(bm: Bitmap?): Int {
        Log.d(TAG, "getBright start")
        if (bm == null) return -1
        val width = bm.width
        val height = bm.height
        var r: Int
        var g: Int
        var b: Int
        var count = 0
        var bright = 0
        count = width * height
        val buffer = IntArray(width * height)
        bm.getPixels(buffer, 0, width, 0, 0, width, height)
        Log.d(TAG, "width:$width,height:$height")
        for (i in 0 until width) {
            for (j in 0 until height) {
                val localTemp = buffer[j * width + i] //bm.getPixel(i, j);
                r = localTemp shr 16 and 0xff
                g = localTemp shr 8 and 0xff
                b = localTemp and 0xff
                bright = (bright + 0.299 * r + 0.587 * g + 0.114 * b).toInt()
            }
        }
        Log.d(TAG, "getBright end")
        return bright / count
    }


    private fun setLightStatusBar() {
        val flags = window.decorView.systemUiVisibility
        window.decorView.systemUiVisibility = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    private fun setDarkStatusBar() {
        val flags = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.decorView.systemUiVisibility = flags xor View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    private fun getScreenWidth(): Int {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

    private fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
}