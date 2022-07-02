package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class BaseMenuActivity<T extends ViewBinding> extends AppCompatActivity {

    private T binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Type genericSuperclass = this.getClass().getGenericSuperclass(); // 某个继承BaseMenuActivity类执行这个语句时，获取的是BaseMenuActivity的Class类
            ParameterizedType parameterizedType = (ParameterizedType)genericSuperclass;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            Class<T> clazz = (Class<T>) actualTypeArguments[0];
            Method method = clazz.getMethod("inflate", LayoutInflater.class);
            binding = (T) method.invoke(null, getLayoutInflater());
            setContentView(binding.getRoot());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }




}
