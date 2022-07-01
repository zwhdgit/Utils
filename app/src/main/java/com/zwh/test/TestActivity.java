package com.zwh.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Choreographer;
import android.view.View;

import com.zwh.test.databinding.ActivityTestBinding;
import com.zwh.test.test_wifip2p.ClientActivity;
import com.zwh.test.test_wifip2p.ClientActivity_1;
import com.zwh.test.test_wifip2p.ServiceActivity;
import com.zwh.test.test_wifip2p.ServiceActivity_1;
import com.zwh.utils.observable.BusMutableLiveData;
import com.zwh.utils.observable.SingleLiveEvent;

public class TestActivity extends AppCompatActivity {

    private SingleLiveEvent<String> singleLiveEvent = new SingleLiveEvent<>();
    private ActivityTestBinding binding;
    private String TAG = getClass().getSimpleName();
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        initThreadTra();
//        initTestWifiP2p();
//        testObservableField();
//        testBusMutableLiveData();
//        testSingleLiveEvent();
//        IntentFilter statusFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
//        registerReceiver(BluBroadcast.mStatusReceive,statusFilter);
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
        try {
            Thread thread = new Thread(new Task());
            thread.start();
        } catch (Exception e) {
            System.out.println("==Exception: " + e.getMessage());
        }
    }

    static class Task implements Runnable {
        @Override
        public void run() {
            try {
                System.out.println(3 / 2);
                System.out.println(3 / 0);
                System.out.println(3 / 1);
            }catch (Exception e){
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