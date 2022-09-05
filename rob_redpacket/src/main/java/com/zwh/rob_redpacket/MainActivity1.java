//package com.zwh.rob_redpacket;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.accessibilityservice.AccessibilityServiceInfo;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.view.accessibility.AccessibilityManager;
//
//import java.util.List;
//
//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                isWeChetServiceOpen();
//            }
//        });
//    }
//
//    public static boolean isStartAccessibilityService(Context context, String name) {
//        AccessibilityManager am = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
//        List<AccessibilityServiceInfo> serviceInfos = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
//        for (AccessibilityServiceInfo info : serviceInfos) {
//            String id = info.getId();
//            if (id.contains(name)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private void isWeChetServiceOpen() {
//        String weChetServiceName = ".WeChetService";
//        boolean isWeChetServiceOpen = isStartAccessibilityService(this, weChetServiceName);
//        if (isWeChetServiceOpen) {
//
//        } else {
//            jumpToSettingPage();
//        }
//    }
//
//    private void jumpToSettingPage() {
//        Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
//        startActivity(intent);
//    }
//}