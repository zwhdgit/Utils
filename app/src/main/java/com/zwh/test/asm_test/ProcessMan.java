package com.zwh.test.asm_test;

import android.util.Log;

import java.security.PublicKey;

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