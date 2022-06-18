package com.example.myapplication.album.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AlbumViewModel extends ViewModel {

    public MutableLiveData<Boolean> isEditMode = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> isSelectAll = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> isDeselectAll = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> shareBehavior = new MutableLiveData<>(false); // 消耗后需要设置为false
    public MutableLiveData<Boolean> deleteBehavior = new MutableLiveData<>(false);
    public int curItem = -1;
}
