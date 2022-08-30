package com.zwh.test.accessibility_test

import android.graphics.Bitmap

object SimilarityUtils {
    fun similarity(bitmap1: Bitmap, bitmap2: Bitmap): Double {
        // 获取图片所有的像素
        val pixels1 = getPixels(bitmap1)
        val pixels2 = getPixels(bitmap2)
        // 总的像素点数以较大图片为准
        val totalCount = pixels1.size.coerceAtLeast(pixels2.size)
        if (totalCount == 0) return 0.00
        var matchCount = 0
        var i = 0
        while (i < pixels1.size && i < pixels2.size) {
            if (pixels1[i] == pixels2[i]) {
                // 统计相同的像素点数量
                matchCount++
            }
            i++
        }
        // 相同的像素点数量除以总的像素点数，得到相似比例。
        return String.format("%.2f", matchCount.toDouble() / totalCount).toDouble()
    }

    private fun getPixels(bitmap: Bitmap): IntArray {
        val pixels = IntArray(bitmap.width * bitmap.height)
        // 获取每个像素的 RGB 值
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        return pixels
    }
}