package com.zwh.test.accessibility_test

import android.graphics.Bitmap
import android.media.Image
import java.nio.ByteBuffer

object ImageUtils {
//    fun imageToBitmap(image: Image): Bitmap {
//        val bitmap = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)
//        bitmap.copyPixelsFromBuffer(image.planes[0].buffer)
//        image.close()
//        return bitmap
//    }
    fun imageToBitmap(image: Image): Bitmap {

        val width = image.width
        val height = image.height
        val planes = image.planes
        val buffer: ByteBuffer = planes[0].buffer
//两个像素的距离
        val pixelStride = planes[0].pixelStride
//整行的距离
        val rowStride = planes[0].rowStride
        val rowPadding = rowStride - pixelStride * width
        var bitmap =
            Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888)
        bitmap.copyPixelsFromBuffer(buffer)
        image.close()
        return bitmap
    }
}


