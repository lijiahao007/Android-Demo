package com.example.myapplication.dialog;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class DialogFragmentUtlis {
    //修复进入后台显示DialogFragment导致闪退闪退
    static void show(Fragment fragment, FragmentManager manager, String tag){
        try {
            //Class c=Class.forName("android.support.v4.app.DialogFragment");
            Class c=Class.forName("androidx.fragment.app.DialogFragment");// 迁移Androidx后需要修改 modify by lyq on 20200111
            Constructor con = c.getConstructor();
            Object obj = con.newInstance();
            Field dismissed = c.getDeclaredField("mDismissed");
            dismissed.setAccessible(true);
            dismissed.set(obj,false);
            Field shownByMe = c.getDeclaredField("mShownByMe");
            shownByMe.setAccessible(true);
            shownByMe.set(obj,false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(fragment, tag);
        ft.commitAllowingStateLoss();
    }
}
