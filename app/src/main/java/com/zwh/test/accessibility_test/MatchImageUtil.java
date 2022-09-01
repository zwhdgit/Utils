package com.zwh.test.accessibility_test;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import com.zwh.test.R;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MatchImageUtil {

    public static Mat mat;
    public static String TAG = "tag";

    public static void main(String[] args) {
        String s0 = "D:\\Code\\Utils\\app\\src\\main\\res\\drawable\\ic0.jpg";
        String s1 = "D:\\Code\\Utils\\app\\src\\main\\res\\drawable\\ic1.jpg";
//        run(s0, s1);
    }

    /**
     * 运行时长 1800
     */
    public static void run(Resources r, String filePath, String filePath1) {
//        Mat mat = Imgcodecs.imread(System.getProperty("user.dir") + "\image\big.png");
//        Mat template = Imgcodecs.imread(System.getProperty("user.dir") + "\image\small.png");
//        boolean loaded = OpenCVLoader.initDebug();
//        System.out.println("loaded=" + loaded);
//        filePath = "src/main/res/drawable/ic0.jpg";
//        filePath1 = "D:\\Code\\Utils\\app\\src\\main\\res\\drawable\\ic1.png";
        Bitmap bitmap = drawableToBitamp(r.getDrawable(R.drawable.ic0));
        Bitmap bitmap1 = drawableToBitamp(r.getDrawable(R.drawable.ic1));

        if (mat == null) {
            Bitmap bit = bitmap.copy(Bitmap.Config.ARGB_8888, false);
            mat = new Mat(bit.getHeight(), bit.getWidth(), CvType.CV_8UC(3));
            Utils.bitmapToMat(bit, mat);
        }


        Bitmap bit1 = bitmap1.copy(Bitmap.Config.ARGB_8888, false);
        Mat template = new Mat(bit1.getHeight(), bit1.getWidth(), CvType.CV_8UC(3));
        Utils.bitmapToMat(bit1, template);


//        Mat mat = Imgcodecs.imread(uri.toString());
//        Mat template = Imgcodecs.imread(uri1.toString());

        int method = Imgproc.TM_CCORR_NORMED;
        int width = mat.cols() - template.cols() + 1;
        int height = mat.rows() - template.rows() + 1;
        Mat result = new Mat(width, height, CvType.CV_32FC1);
        Log.e(TAG, "matchTemplate: start");
        Imgproc.matchTemplate(mat, template, result, method);
        Log.e(TAG, "matchTemplate: end");
        Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());
        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
        double x, y;
        if (method == Imgproc.TM_SQDIFF_NORMED || method == Imgproc.TM_SQDIFF) {
            x = mmr.minLoc.x;
            y = mmr.minLoc.y;
        } else {
            x = mmr.maxLoc.x;
            y = mmr.maxLoc.y;
        }
        System.out.println("x=" + x + ",y=" + y);
    }

    private static Bitmap drawableToBitamp(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        System.out.println("Drawable转Bitmap");
        Bitmap.Config config =
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        //注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }
}
