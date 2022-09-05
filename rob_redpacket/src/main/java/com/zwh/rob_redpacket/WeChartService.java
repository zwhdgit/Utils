package com.zwh.rob_redpacket;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * 1、收到通知 -》2、点击通知进入聊天页面 -》3、关键字检索红包并打开 -》4、返回
 *  不完全版
 *  1、保活没弄
 *  2、通知没处理，结合系统自带直接跳转聊天页，偷懒只处理 3、4
 */
public class WeChartService extends AccessibilityService {

    static final String TAG = "Jackie";

    /**
     * 微信的包名
     */
    static final String WECHAT_PACKAGENAME = "com.tencent.mm";

    /**
     * 聊天页面
     */
//    private static final String WECHET_LAUCHER = "com.tencent.mm.ui.LauncherUI";
    private static final String WECHET_LAUCHER = "com.tencent.mm.ui.chatting.ChattingUI";
    /**
     * 拆包页面
     */
//    private static final String LUCKEY_MONEY_RECEIVER = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI";
    private static final String OPEN_PACK = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyNotHookReceiveUI";
    /**
     * 红包详情
     */
//    private static final String PACK_DETAILS = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI";
    private static final String PACK_DETAILS = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyBeforeDetailUI";

    /**
     * 红包消息的关键字
     */
    static final String ENVELOPE_TEXT_KEY = "[微信红包]";
    static final String ENVELOPE_TEXT = "微信红包";
//    static final String ENVELOPE_TEXT_STATUS = "已领取";

    final List<String> ENVELOPE_STATUS = new ArrayList<String>() {{
        add("已领取");
        add("已被领完");
    }};

    Handler handler = new Handler();

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        final int eventType = event.getEventType();

        Log.d(TAG, "事件---->" + event);

        //通知栏事件
        if (eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            List<CharSequence> texts = event.getText();
            if (!texts.isEmpty()) {
                for (CharSequence t : texts) {
                    String text = String.valueOf(t);
                    if (text.contains(ENVELOPE_TEXT_KEY)) {
                        openNotification(event);
                        break;
                    }
                }
            }
        } else if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            openEnvelope(event);
        }
    }

    /*@Override
    protected boolean onKeyEvent(KeyEvent event) {
        //return super.onKeyEvent(event);
        return true;
    }*/

    @Override
    public void onInterrupt() {
        Toast.makeText(this, "中断抢红包服务", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Toast.makeText(this, "连接抢红包服务", Toast.LENGTH_SHORT).show();
    }

    private void sendNotificationEvent() {
        AccessibilityManager manager = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
        if (!manager.isEnabled()) {
            return;
        }
        AccessibilityEvent event = AccessibilityEvent.obtain(AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED);
        event.setPackageName(WECHAT_PACKAGENAME);
        event.setClassName(Notification.class.getName());
        CharSequence tickerText = ENVELOPE_TEXT_KEY;
        event.getText().add(tickerText);
        manager.sendAccessibilityEvent(event);
    }

    /**
     * 打开通知栏消息
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void openNotification(AccessibilityEvent event) {
        if (event.getParcelableData() == null || !(event.getParcelableData() instanceof Notification)) {
            return;
        }
        //以下是精华，将微信的通知栏消息打开
        Notification notification = (Notification) event.getParcelableData();
        PendingIntent pendingIntent = notification.contentIntent;
        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void openEnvelope(AccessibilityEvent event) {
        CharSequence className = event.getClassName();
        Log.e(TAG, "AccessibilityEvent: " + event);
        if (event.getPackageName().equals("com.tencent.mm")) {
            Log.e("className", "className: = " + className);
            if (OPEN_PACK.contentEquals(className)) {
                //点中了红包，下一步就是去拆红包
                checkKey1();
            } else if (PACK_DETAILS.contentEquals(className)) {
                //红包详情页
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        performGlobalAction(GLOBAL_ACTION_BACK);
                    }
                }, 300);
            } else if (WECHET_LAUCHER.contentEquals(className)) {
                //在聊天界面,去点中红包
                checkKey2();
            } else {
                checkKey2();
            }
        }

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void checkKey1() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo == null) {
            Log.w(TAG, "rootWindow为空");
            return;
        }

//        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("拆红包");
//        for (AccessibilityNodeInfo n : list) {
//            n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//        }
//        ClickUtils.INSTANCE.click(this, 600, 1160);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ClickUtils.INSTANCE.click(WeChartService.this, 526, 1472);
            }
        }, 100);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void checkKey2() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo == null) {
            Log.w(TAG, "rootWindow为空");
            return;
        }
//        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("领取红包");
        List<AccessibilityNodeInfo> packList = nodeInfo.findAccessibilityNodeInfosByText(ENVELOPE_TEXT);

        List<AccessibilityNodeInfo> statusList = new ArrayList<>();
        for (String envelope_status : ENVELOPE_STATUS) {
            statusList.addAll(nodeInfo.findAccessibilityNodeInfosByText(envelope_status));
        }
        ArrayList<Integer> statusCoords = new ArrayList<>();
        Rect rect0 = new Rect();
        if (!statusList.isEmpty()) {
            for (AccessibilityNodeInfo info : statusList) {
//                Log.e(TAG, "领取信息: " + info);
                info.getBoundsInScreen(rect0);
                statusCoords.add(rect0.top);
//                Log.e(TAG, info.getText() + ",位置 :" + rect0.top);
            }
        }

        if (packList.isEmpty()) {
            Log.e(TAG, "当前页面没有红包");
        } else {
            Log.e(TAG, "红包个数" + packList.size());
            for (AccessibilityNodeInfo n : packList) {
                clickRedPack(n, statusCoords);
            }
        }

//        else {
//            //最新的红包领起
//            for (int i = list.size() - 1; i >= 0; i--) {
//                AccessibilityNodeInfo parent = list.get(i).getParent();
//                Log.i(TAG, "-->领取红包:" + parent);
//                clickRedPack(parent);
//                break;
//            }
//        }
    }


    private boolean clickRedPack(AccessibilityNodeInfo n, List<Integer> statusList) {
        if (n == null) return false;
//        Log.e(TAG, "-->微信红包:" + n);
//        if (n.isClickable()) {
//            n.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//        }

        Rect rect = new Rect();
        n.getBoundsInScreen(rect);
//        Log.e(TAG, "clickRedPack: " );
        for (Integer integer : statusList) {
            if (Math.abs(integer - rect.top) < 100) {

                Log.e(TAG, "标记位置=" + integer + ",红包位置=" + rect.top);
                Log.e(TAG, "红包已领取" + integer);
                return false;
            }
        }
        Log.e(TAG, "点击红包");
        ClickUtils.INSTANCE.click(this, rect.left, rect.top);
        return true;
    }
}