package com.zwh.utils.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.Locale;

/**
 * @author : Angle
 * 创建时间 : 2019/1/17 10:36
 * 描述 : app的信息工具类
 */
public class AppInfoUtils {

    /**
     * 获取版本信息
     *
     * @param context 上下文
     * @return 版本信息的类
     */
    private static PackageInfo getPackageInfo(Context context) {
        try {
            return context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取版本信息
     *
     * @param context 上下文
     * @return long型的版本信息
     */
    public static Long getVersionCode(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        long versionCode;
        if (packageInfo == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            versionCode = packageInfo.versionCode;
        } else {
            versionCode = packageInfo.getLongVersionCode();
        }
        return versionCode;
    }

    /**
     * 获取版本名称
     *
     * @param context 上下文
     * @return 版本名称
     */
    public static String getVersionName(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo == null) {
            return null;
        }

        return packageInfo.versionName;
    }


    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        return Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return 语言列表
     */
    public static Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }
}