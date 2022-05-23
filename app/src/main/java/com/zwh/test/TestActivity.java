package com.zwh.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.bluetooth.BluetoothAdapter;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Choreographer;
import android.view.View;

import com.zwh.test.databinding.ActivityTestBinding;
import com.zwh.utils.observable.BusMutableLiveData;
import com.zwh.utils.observable.SingleLiveEvent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;

public class TestActivity extends AppCompatActivity {

    private SingleLiveEvent<String> singleLiveEvent = new SingleLiveEvent<>();
    private ActivityTestBinding binding;
    private String TAG = getClass().getSimpleName();
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

//        testObservableField();
//        testBusMutableLiveData();
//        testSingleLiveEvent();
//        IntentFilter statusFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
//        registerReceiver(BluBroadcast.mStatusReceive,statusFilter);
    }

    private void initView() {
        View view = getLayoutInflater().inflate(R.layout.activity_test, null);
        setContentView(view);
        DataBindingUtil.bind(view);
        binding = DataBindingUtil.getBinding(view);
        AtomicBoolean isBluOff = new AtomicBoolean(true);
        binding.btTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean b = isBluOff.get();
                isBluOff.compareAndSet(b,!b);
                Log.e(TAG, "onClick: "+b );
            }
        });
    }

    private void testSingleLiveEvent() {
        singleLiveEvent.setValue("ss");
        binding.btTest.setOnClickListener(new View.OnClickListener() {
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
        binding.btTest.setOnClickListener(new View.OnClickListener() {
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