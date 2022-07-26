package com.zwh.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Choreographer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.zwh.test.GsonTest.Lxy;
import com.zwh.test.GsonTest.Zwh;
import com.zwh.test.databinding.ActivityTestBinding;
import com.zwh.test.test_wifip2p.ClientActivity_1;
import com.zwh.test.test_wifip2p.ServiceActivity_1;
import com.zwh.test.viewmodel.TestViewModel;
import com.zwh.utils.observable.BusMutableLiveData;
import com.zwh.utils.observable.SingleLiveEvent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private SingleLiveEvent<String> singleLiveEvent = new SingleLiveEvent<>();
    private ActivityTestBinding binding;
    private String TAG = getClass().getSimpleName();
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        testLea();
//        testGson();
//        initThreadTra();
//        initTestWifiP2p();
//        testObservableField();
//        testBusMutableLiveData();
//        testSingleLiveEvent();
//        IntentFilter statusFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);

//        registerReceiver(BluBroadcast.mStatusReceive,statusFilter);
    }

    private void testLea() {
        binding.bt0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.myView.setVisibility(View.VISIBLE);
//                binding.myView.invalidate();
            }
        });
        binding.bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.myView.invalidate();
            }
        });
    }

    private void testGson() {
        binding.bt0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Zwh zwh = new Gson().fromJson("{\"age\":\"12\"}", Zwh.class);
                Log.e("zwh", "onClick: " + zwh.name);
            }
        });
        binding.bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Lxy lxy = new Gson().fromJson("{\"age\":\"12\"}", Lxy.class);
                Log.e("lxy", "onClick: " + lxy.getName());
            }
        });

    }

    private void initThreadTra() {
        binding.bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(3 / 0);
                System.out.println(3 / 1);
            }
        });
    }

    public static void main(String[] args) {
//        try {
//            Thread thread = new Thread(new Task());
//            thread.start();
//        } catch (Exception e) {
//            System.out.println("==Exception: " + e.getMessage());
//        }
//        BigDecimal b = new BigDecimal(0.1);
////        double d1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//        System.out.println(b.doubleValue() + "");
//        System.out.println(b.floatValue() + "");
//        System.out.println(b + "");

        ObservableField<Integer> tipIcon = new ObservableField<>(0);
        if (tipIcon.get() != null && tipIcon.get() == 0) {
            System.out.println("没崩");
        }else if (tipIcon.get()==null){
            System.out.println("检测为null");
        }
        System.out.println("可以");

        List<? super TextView> textViews = new ArrayList<>();
//        textViews.add(new Bview(null));
        TextView textView = (TextView) textViews.get(0); // 👈 get 可以

        textViews.add(textView);

    }

    static class Task implements Runnable {
        @Override
        public void run() {
            try {
                System.out.println(3 / 2);
                System.out.println(3 / 0);
                System.out.println(3 / 1);
            } catch (Exception e) {
                System.out.println("==Exception: " + e.getMessage());
            }
        }
    }

    private void initView() {
        View view = getLayoutInflater().inflate(R.layout.activity_test, null);
        setContentView(view);
        DataBindingUtil.bind(view);
        binding = DataBindingUtil.getBinding(view);
    }

    private void initTestWifiP2p() {
        binding.requestPermissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(TestActivity.this,
                        new String[]{Manifest.permission.CHANGE_NETWORK_STATE,
                                Manifest.permission.ACCESS_NETWORK_STATE,
//                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.ACCESS_WIFI_STATE,
                                Manifest.permission.CHANGE_WIFI_STATE,
                                Manifest.permission.ACCESS_FINE_LOCATION}, 2333);
            }
        });
        binding.bt0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TestActivity.this, ClientActivity_1.class));
            }
        });
        binding.bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestActivity.this, ServiceActivity_1.class));
            }
        });
    }

    private void testSingleLiveEvent() {
        singleLiveEvent.setValue("ss");
        binding.bt0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singleLiveEvent.observe(TestActivity.this, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        Log.e(TAG, "onChanged: " + s);
                    }
                });
            }
        });
    }

    private void testBusMutableLiveData() {
        BusMutableLiveData<String> data = new BusMutableLiveData<>();
        data.postValue("ss");
        data.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.e("TAG", "onChanged: " + s);
            }
        });
    }

    /**
     * ObservableField 延迟测试
     */
    private void testObservableField() {
        TestViewModel viewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new TestViewModel();
            }
        }).get(TestViewModel.class);
        binding.setViewModel(viewModel);
        binding.bt0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.setText("天气真好");
                Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
                    @Override
                    public void doFrame(long l) {
                        Log.e("TEST", "text: " + binding.tvTest.getText());
                    }
                });
            }
        });
    }

}