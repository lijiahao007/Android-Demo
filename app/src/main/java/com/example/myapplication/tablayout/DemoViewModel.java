package com.example.myapplication.tablayout;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DemoViewModel extends ViewModel {
    public MutableLiveData<Boolean> isChange = new MutableLiveData<>(true);
}
