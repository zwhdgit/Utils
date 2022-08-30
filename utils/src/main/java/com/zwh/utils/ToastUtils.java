package com.zwh.utils;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

public class ToastUtils {
    private static Toast mToast;
    private static Toast mLongToast;
    private static Context mContext;
    private static Handler handler = new Handler(Looper.getMainLooper());

    /**
     * Application 调用
     * @param context
     */
    public static void init(Context context) {
        mContext = context;
    }

    public static void showToast(String msg) {
        if (checkThread()) {
            executeToast(msg);
        } else {
            handler.post(() -> executeToast(msg));
        }
    }

    private static void executeToast(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
        }
        mToast.setText(msg);
        mToast.show();
    }

    public static void showLongToast(String msg) {
        if (checkThread()) {
            executeLongToast(msg);
        } else {
            handler.post(() -> executeLongToast(msg));
        }
    }

    private static void executeLongToast(String msg) {
//        Context context = CommonUtils.getContext();
        if (mLongToast == null) {
            mLongToast = Toast.makeText(mContext, msg, Toast.LENGTH_LONG);
        }
        mLongToast.setText(msg);
        mLongToast.show();
    }

//    public static void showCustomToast(View view, String text) {
////        Context context = CommonUtils.getContext();
//        Toast mToast = new Toast(mContext);
//        mToast.setGravity(17, 0, 0);
//        mToast.setDuration(Toast.LENGTH_SHORT);
//        mToast.setView(view);
//        mToast.setText(text);
//        mToast.show();
//    }

    /**
     * 非主线程也可通过创建looper的形式成功进行Toast,但是可能会引发冲突
     * android.view.ViewRootImpl$CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views.
     * 参考 ：https://juejin.cn/post/7130824794060587016#heading-0
     */
    private static boolean checkThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

}
