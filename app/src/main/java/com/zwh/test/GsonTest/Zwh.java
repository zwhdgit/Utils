package com.zwh.test.GsonTest;

import android.util.Log;

public class Zwh extends Person {
    public String name;
    public int age;

//    public Zwh() {
//        Log.e("zwh", "无参构造" );
//    }

    public Zwh(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
