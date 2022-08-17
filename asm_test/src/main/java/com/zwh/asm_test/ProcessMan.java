package com.zwh.asm_test;

import android.util.Log;

class ProcessMan {

    private String name;

    public void printName() {
        System.out.println(name);
    }

    public void run() {
        new Thread(() -> {
            Log.e("TAG", "run: ");
        });
    }
}