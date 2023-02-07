package com.example.myapplication.dialog;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.viewbinding.ViewBinding;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

abstract class BaseDialogFragment<T extends ViewBinding> extends DialogFragment implements View.OnClickListener {
    //    protected abstract T getViewBinding(ViewGroup parent);
    protected abstract void initView(View view);
    protected abstract int[] bindClickId();
    protected abstract void onClicked(View view);
    protected T binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Type type = this.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            try {
                Class<T> clazz = (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[0];
                Method method = clazz.getMethod("inflate", LayoutInflater.class);
                binding = (T) method.invoke(null, getLayoutInflater());
            } catch (NoSuchMethodException e) {
                Log.e(this.getClass().getSimpleName(), "NoSuchMethodException = " + e.toString());
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                Log.e(this.getClass().getSimpleName(), "IllegalAccessException = " + e.toString());
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                Log.e(this.getClass().getSimpleName(), "InvocationTargetException = " + e.toString());
                e.printStackTrace();
            }
        }
        initView(binding.getRoot());
        return bingClick(binding.getRoot(),bindClickId());
    }
    private View bingClick(View view, int[] ids) {
        if (ids != null) {
            for (int i = 0; i < ids.length; i++) {
                view.findViewById(ids[i]).setOnClickListener(this);
            }
        }
        return view;
    }

    @Override
    public void onClick(View view) {
//        if (!CanClickUtil.isCanClick()) return;
        onClicked(view);
    }
}
