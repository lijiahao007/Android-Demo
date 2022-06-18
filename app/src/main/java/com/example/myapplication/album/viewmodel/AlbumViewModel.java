package com.example.myapplication.album.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AlbumViewModel extends ViewModel {

    public MutableLiveData<Boolean> isEditMode = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> isSelectAll = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> shareBehavior = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> deleteBehavior = new MutableLiveData<>(false);
}
