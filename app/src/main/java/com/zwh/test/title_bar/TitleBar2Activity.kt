package com.zwh.test.title_bar

import android.annotation.TargetApi
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.palette.graphics.Palette
import com.jaeger.library.StatusBarUtil
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
        setTransparent(this)
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
}