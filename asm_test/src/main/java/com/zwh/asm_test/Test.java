package com.zwh.asm_test;

import android.util.Log;

public class Test {
    public static void main(String[] args) {
//        ProcessTest.testReader();
//        ProcessTest.testClassVisitor();
//        ProcessTest.createClass();
//        new TextView(null).setOnClickListener(v -> {
//
//        });
    }
    public void run(){
        new Thread(()->{
            Log.e("TAG", "Test run: " );
        });
    }
}
