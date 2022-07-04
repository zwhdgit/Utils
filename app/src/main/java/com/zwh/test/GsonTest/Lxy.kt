package com.zwh.test.GsonTest

import android.util.Log

data class Lxy(var name: String, var age: Int) : Person() {

    init {
        Log.e("Lxy", "lxy构造:" + name)
    }
}