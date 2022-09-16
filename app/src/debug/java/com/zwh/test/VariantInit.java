package com.zwh.test;

import android.app.Application;

import com.didichuxing.doraemonkit.DoKit;

public class VariantInit {

    public static Application app;

    public static void init(Application app) {
        VariantInit.app = app;
        initDokit();
    }

    private static void initDokit() {
        new DoKit.Builder(app)
                .productId("749a0600b5e48dd77cf8ee680be7b1b7")
                .build();

    }
}
