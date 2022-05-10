package com.zwh.utils.helper;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.core.content.res.ResourcesCompat;

import java.util.List;

/**
 * 耦合
 *
 * @see AppActivityManager
 * 要问为什么耦合 by lazy
 */
public class ResourceHelper {

    /**
     * 有效 String 不包含 纯空格 ：str.trim()
     */
    public static boolean isValidStr(String str) {
        return str != null && !TextUtils.isEmpty(str.trim());
    }

    /**
     * 有效 List 不包含 all elements are null ： list.get(0) != null
     */
    public static <T> boolean isValidList(List<T> list) {
        return list != null && !list.isEmpty() && list.get(0) != null;
    }

    public static String getString(@StringRes int id, String s) {
        Activity context = AppActivityManager.getInstance().currentActivity();
        return context.getString(id, s);
    }

    public static String getString(@StringRes int id, Object... s) {
        Activity context = AppActivityManager.getInstance().currentActivity();
        return context.getString(id, s);
    }

    public static Drawable getDrawable(@DrawableRes int id) {
        Activity context = AppActivityManager.getInstance().currentActivity();
        return ResourcesCompat.getDrawable(context.getResources(), id, null);
    }

    public static int getColor(@ColorRes int id) {
        Activity context = AppActivityManager.getInstance().currentActivity();
        return context.getResources().getColor(id);
    }
}
