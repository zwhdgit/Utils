
package com.zwh.utils.helper;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.Process;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Stack;

public class AppActivityManager {
    private Stack<WeakReference<Activity>> mActivityStack;
    private static volatile AppActivityManager sInstance;
    private static final String TAG = "MyAppActivityManager";

    private AppActivityManager() {
    }

    public static AppActivityManager getInstance() {
        if (sInstance == null) {
            Class var0 = AppActivityManager.class;
            synchronized (AppActivityManager.class) {
                if (sInstance == null) {
                    sInstance = new AppActivityManager();
                }
            }
        }

        return sInstance;
    }

    public void addActivity(Activity activity) {
        if (this.mActivityStack == null) {
            this.mActivityStack = new Stack();
        }

        this.mActivityStack.add(new WeakReference(activity));
    }

    public void checkWeakReference() {
        if (this.mActivityStack != null) {
            Iterator it = this.mActivityStack.iterator();

            while (it.hasNext()) {
                WeakReference<Activity> activityReference = (WeakReference) it.next();
                Activity temp = (Activity) activityReference.get();
                if (temp == null) {
                    it.remove();
                }
            }
        }

    }

    public Activity currentActivity() {
        this.checkWeakReference();
        return this.mActivityStack != null && !this.mActivityStack.isEmpty() ? (Activity) ((WeakReference) this.mActivityStack.lastElement()).get() : null;
    }

    public void finishOtherActivity(Activity activtiy) {
        if (this.mActivityStack != null && activtiy != null) {
            Iterator it = this.mActivityStack.iterator();

            while (it.hasNext()) {
                WeakReference<Activity> activityReference = (WeakReference) it.next();
                Activity temp = (Activity) activityReference.get();
                if (temp == null) {
                    it.remove();
                } else if (temp != activtiy) {
                    it.remove();
                    temp.finish();
                }
            }
        }

    }

    public void finishOtherActivity(Class<?> cls) {
        if (this.mActivityStack != null) {
            Iterator it = this.mActivityStack.iterator();

            while (it.hasNext()) {
                WeakReference<Activity> activityReference = (WeakReference) it.next();
                Activity activity = (Activity) activityReference.get();
                if (activity == null) {
                    it.remove();
                } else if (!activity.getClass().equals(cls)) {
                    it.remove();
                    activity.finish();
                }
            }
        }

    }

    public void finishActivity() {
        Activity activity = this.currentActivity();
        if (activity != null) {
            this.finishActivity(activity);
        }

    }

    public void finishActivity(Activity activity) {
        if (activity != null) {
            Iterator it = this.mActivityStack.iterator();

            while (it.hasNext()) {
                WeakReference<Activity> activityReference = (WeakReference) it.next();
                Activity temp = (Activity) activityReference.get();
                if (temp == null) {
                    it.remove();
                } else if (temp == activity) {
                    it.remove();
                }
            }

            activity.finish();
        }

    }

    public void finishActivity(Class<?> cls) {
        if (this.mActivityStack != null) {
            Iterator it = this.mActivityStack.iterator();

            while (it.hasNext()) {
                WeakReference<Activity> activityReference = (WeakReference) it.next();
                Activity activity = (Activity) activityReference.get();
                if (activity == null) {
                    it.remove();
                } else if (activity.getClass().equals(cls)) {
                    it.remove();
                    activity.finish();
                }
            }
        }

    }

    public void finishAllActivity() {
        if (this.mActivityStack != null) {
            Iterator it = this.mActivityStack.iterator();

            while (it.hasNext()) {
                WeakReference<Activity> activityReference = (WeakReference) it.next();
                Activity activity = (Activity) activityReference.get();
                if (activity != null) {
                    activity.finish();
                }
            }

            this.mActivityStack.clear();
        }

    }

    public boolean isExistActivity(Class<?> cls) {
        if (this.mActivityStack != null) {
            Iterator it = this.mActivityStack.iterator();

            while (it.hasNext()) {
                WeakReference<Activity> activityReference = (WeakReference) it.next();
                Activity activity = (Activity) activityReference.get();
                if (activity.getClass().equals(cls)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void exitApp() {
        try {
            this.finishAllActivity();
            System.exit(0);
            Process.killProcess(Process.myPid());
        } catch (Exception var2) {
            Log.e("AppActivityManagerDeleg", "exitApp: ", var2);
        }

    }
}
