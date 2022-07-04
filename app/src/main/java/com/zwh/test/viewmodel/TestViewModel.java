package com.zwh.test.viewmodel;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

public class TestViewModel extends ViewModel {

    public ObservableField<String> text = new ObservableField<>();

    public void setText(String s) {
        text.set(s);
    }

}
