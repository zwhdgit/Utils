package com.zwh.asm_test;

public class ClickCheck {

    public static long FROZEN_MILLIS = 1000L;

    public static long preClickTime = 0;

    public static boolean isValidClick() {
        final long now = System.currentTimeMillis();
        if (now - preClickTime > FROZEN_MILLIS) {
            preClickTime = now;
            return true;
        }
        return false;
    }
}