package com.zwh.utils.utils;

import android.app.Activity;

import java.util.Stack;

public class ActivityController {

    /**
     * Activity栈
     */
    private static Stack<Activity> activityStack;
    /**
     * 单例模式
     */
    private static ActivityController instance;

    private ActivityController() {
    }

    /**
     * 单一实例
     */
    public static ActivityController getInstance() {
        if (instance == null) {
            synchronized (ActivityController.class) {
                if (instance == null) {
                    instance = new ActivityController();
                }
            }
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0; i < activityStack.size(); i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 获取栈中Activity的数量
     *
     * @return Activity的数量
     */
    public int getStackActivitySize() {
        return activityStack == null ? 0 : activityStack.size();
    }
}