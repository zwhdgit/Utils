package com.zwh.test.accessibility_test;

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.zwh.test.R
import com.zwh.test.databinding.ActivityTestBinding
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.opencv.android.OpenCVLoader
import org.reactivestreams.Subscriber


const val REQUEST_MEDIA_PROJECTION = 1

class AccessibilityTestActivity : AppCompatActivity() {


    private val binding by lazy { ActivityTestBinding.inflate(layoutInflater) }
    private val mediaProjectionManager: MediaProjectionManager by lazy {
        getSystemService(
            MEDIA_PROJECTION_SERVICE
        ) as MediaProjectionManager
    }
    private var mediaProjection: MediaProjection? = null
    private var virtualDisplay: VirtualDisplay? = null
    private val bitmapList: ArrayList<Bitmap> = arrayListOf()

    @Volatile
    lateinit var bitmap: Bitmap

    lateinit var observable: Disposable

    private val handler = Handler(Looper.getMainLooper(), object : Handler.Callback {
        override fun handleMessage(msg: Message): Boolean {
//                bitmapList.
//            }
            return false
        }
    })

    private val imageReader by lazy {
        ImageReader.newInstance(
            ScreenUtils.getScreenWidth(),
            ScreenUtils.getScreenHeight(),
            PixelFormat.RGBA_8888,
            1
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initListener()
        val loaded = OpenCVLoader.initDebug()
        Log.d("~~~", "loaded: $loaded")
//        if (loaded) {
//            val bitmap1 = BitmapFactory.decodeResource(resources, R.drawable.step0)
//            val bitmap2 = Bitmap.createBitmap(bitmap1, 0, 0, bitmap1.width / 2, bitmap1.height / 2)
//            Log.d("~~~", "similarity: ${SIFTUtils.similarity(bitmap1, bitmap2)}")
//        }
        initData()
        requestIgnoreBatteryOptimizations()
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun requestIgnoreBatteryOptimizations() {
        try {
            val intent: Intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
            intent.data = Uri.parse("package:$packageName")
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun initListener() {
        binding.requestPermissions.setText("开启辅助功能")
        binding.requestPermissions.setOnClickListener {
            AccessibilitySettingUtils.jumpToAccessibilitySetting(AccessibilityTestActivity@ this)
        }

        binding.bt0.setText("去截屏")
        binding.bt0.setOnClickListener {
//            binding.page.cs.visibility = View.VISIBLE
            startScreenCapture()
        }

        binding.bt1.setText("开启任务")
        binding.bt1.setOnClickListener {

        }

        binding.page.btnStart.setOnClickListener {
            Log.d("~~~", "Requesting confirmation")
            startScreenCapture()
        }
        binding.page.btnStop.setOnClickListener {
            Log.d("~~~", "Stop screen capture")
            stopScreenCapture()
        }
    }


    fun initData() {
        val bitmap0 = BitmapFactory.decodeResource(resources, R.drawable.step0)
        val bitmap1 = BitmapFactory.decodeResource(resources, R.drawable.step1)
        val bitmap2 = BitmapFactory.decodeResource(resources, R.drawable.step2)
        bitmapList.add(bitmap0)
        bitmapList.add(bitmap1)
        bitmapList.add(bitmap2)
    }

    fun handleBitmap() {
//        Thread {
        observable = Observable.create(object : ObservableOnSubscribe<String> {
            override fun subscribe(e: ObservableEmitter<String>) {
                for (i in 0..bitmapList.size - 1) {
                    val b = bitmapList[i]
                    val similarity = SIFTUtils.similarity(b, bitmap)
                    val similarity1 = SimilarityUtils.similarity(bitmap, b)
                    Log.d("~~~", "similarity==" + similarity)
                    Log.d("~~~", "similarity1==" + similarity1)
                    if (similarity > 0.4) {
                        when (i) {
                            0 -> {
                                ClickUtils.x = 803F
                                ClickUtils.y = 592F
                            }
                            1 -> {
                                ClickUtils.x = 709F
                                ClickUtils.y = 809F
                            }
                            2 -> {
                                ClickUtils.x = 572F
                                ClickUtils.y = 286F
                            }
                        }
                    }
                }
                e.onNext("")
            }
        })
            .subscribeOn(Schedulers.computation()) //异步任务在IO线程执行
            .observeOn(AndroidSchedulers.mainThread()) //执行结果在主线程运行
            .subscribe(object : Consumer<String> {
                override fun accept(t: String) {
                    startScreenCapture()
                }
            })
//        }.start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_MEDIA_PROJECTION) {
            if (resultCode != RESULT_OK) {
                Log.d("~~~", "User cancelled")
                return
            }
            Log.d("~~~", "Starting screen capture")
            mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data!!)
            setUpVirtualDisplay()
        }
    }


    private fun startScreenCapture() {
        if (mediaProjection == null) {
            Log.d("~~~", "Requesting confirmation")
            // This initiates a prompt dialog for the user to confirm screen projection.
            startActivityForResult(
                mediaProjectionManager.createScreenCaptureIntent(),
                REQUEST_MEDIA_PROJECTION
            )
        } else {
            Log.d("~~~", "mediaProjection != null")
            setUpVirtualDisplay()
        }
    }

    private fun setUpVirtualDisplay() {
        Log.d("~~~", "setUpVirtualDisplay")
        virtualDisplay = mediaProjection!!.createVirtualDisplay(
            "ScreenCapture",
            ScreenUtils.getScreenWidth(),
            ScreenUtils.getScreenHeight(),
            ScreenUtils.getScreenDensityDpi(),
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            imageReader.surface,
            null,
            null
        )
        handler.postDelayed({
            val image = imageReader.acquireLatestImage()
            if
                    (image != null) {
                Log.d("~~~", "get image: $image")
                bitmap = ImageUtils.imageToBitmap(image)
//                binding.page.iv.setImageBitmap(bitmap)
                binding.iv.setImageBitmap(bitmap)
            } else {
                Log.d("~~~", "image == null")
            }
            stopScreenCapture()
            handleBitmap()
        }, 3000)
    }


    private fun stopScreenCapture() {
        Log.d("~~~", "stopScreenCapture, virtualDisplay = $virtualDisplay")
        virtualDisplay?.release()
        virtualDisplay = null
    }
}
//其中，用到的 ScreenUtils 的作用是获取屏幕的宽高和密度。代码如下：

object ScreenUtils {

    fun getScreenWidth(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }

    fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    fun getScreenDensityDpi(): Int {
        return Resources.getSystem().displayMetrics.densityDpi
    }
}
