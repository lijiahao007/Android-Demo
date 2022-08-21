package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class BaseActivity<T extends ViewBinding> extends AppCompatActivity {

    protected T binding;
    protected final String TAG = getClass().getCanonicalName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Type genericSuperclass = getClass().getGenericSuperclass();
        ParameterizedType parameterizedSuperclass = (ParameterizedType) genericSuperclass;
        assert parameterizedSuperclass != null;
        Type[] actualTypeArguments = parameterizedSuperclass.getActualTypeArguments();
        Type viewBindingType = actualTypeArguments[0];
        Class viewBindingClass = (Class) viewBindingType;
        try {
            Method inflate = viewBindingClass.getMethod("inflate", LayoutInflater.class);
            binding = (T) inflate.invoke(null, getLayoutInflater());
            assert binding != null;
            setContentView(binding.getRoot());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
