package com.zwh.asm_test;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Button bt0 = findViewById(R.id.bt0);
        bt0.setText("匿名内部类消息");
        bt0.setOnClickListener(new View.OnClickListener() {
            @Override
            @NoCheckClick
            public void onClick(View view) {
//                Log.e(TAG, "onClick: ", );
                Log.e("TAG", "匿名内部类消息: ");
            }
        });

        Button bt1 = findViewById(R.id.bt1);
        bt1.setText("lambda消息");
        bt1.setOnClickListener(view -> Log.e("TAG", "lambda消息: "));
//        run();
//        initListener();
    }

//    private void initListener() {
//        findViewById(R.id.requestPermissions).setOnClickListener(v -> Log.e("TAG", "initListener: "));
////        run1();
//    }

    public void run() {
//        if (ClickCheck.isFastClick()) return;
        Log.e("TAG", "run: ");
    }
//
//    public void run1() {
//        Log.e("TAG", "run: ");
//    }
}
