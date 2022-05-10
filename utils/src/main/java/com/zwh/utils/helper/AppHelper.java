package com.zwh.utils.helper;

import android.app.Activity;
import android.content.pm.PackageManager;

public class AppHelper {

    public static String getAppName() {
        Activity context = AppActivityManager.getInstance().currentActivity();
        if (context == null) {
            return null;
        }
        try {
            PackageManager packageManager = context.getPackageManager();
            return String.valueOf(packageManager.getApplicationLabel(context.getApplicationInfo()));
        } catch (Throwable e) {
            return null;
        }
    }

}
