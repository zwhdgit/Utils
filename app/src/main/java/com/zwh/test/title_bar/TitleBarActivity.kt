package com.zwh.test.title_bar

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.palette.graphics.Palette
import com.zwh.test.R

/**
 * https://guolin.blog.csdn.net/article/details/123023395?spm=1001.2014.3001.5502
 */
class TitleBarActivity : AppCompatActivity() {
    lateinit var bgView: ImageView
    val TAG: String = javaClass.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title_bar)
        initView()
        setBg(R.drawable.dark_image)
    }

    fun initView() {
        bgView = findViewById(R.id.bg)
        val onClickListener = View.OnClickListener {
            when (it.id) {
                R.id.bt0 -> setBg(R.drawable.dark_image)
                R.id.bt1 -> setBg(R.drawable.light_image)
                R.id.bt2 -> setBg(R.drawable.split_image)
            }
        }
        findViewById<Button>(R.id.bt0).setOnClickListener(onClickListener)
        findViewById<Button>(R.id.bt1).setOnClickListener(onClickListener)
        findViewById<Button>(R.id.bt2).setOnClickListener(onClickListener)
//        setBg(R.drawable.dark_image)
    }

    fun setBg(bgId: Int) {
        val bitmap = BitmapFactory.decodeResource(resources, bgId)
        bgView.setImageBitmap(bitmap)
        setBarStyle(bitmap)
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
//                        val luminance = ColorUtils.calculateLuminance(swatch.rgb)
                        val luminance = getBright(bitmap)
                        // 当luminance小于0.5时，我们认为这是一个深色值.
//                        if (luminance < 0.5) {
                        if (luminance < 128) {
                            setDarkStatusBar()
                        } else {
                            setLightStatusBar()
                        }
                    }
                }
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