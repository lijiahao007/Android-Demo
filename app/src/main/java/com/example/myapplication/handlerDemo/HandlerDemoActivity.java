package com.example.myapplication.handlerDemo;

import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.BaseActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityHandlerDemoBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.LinkedList;

public class HandlerDemoActivity extends BaseActivity<ActivityHandlerDemoBinding> {

    private AppBarConfiguration appBarConfiguration;

    LinkedList<MessageQueue.IdleHandler> idleHandlerList = new LinkedList<>();
    LinkedList<MessageQueue.IdleHandler> longTimeIdleHandler = new LinkedList<>();
    private MessageQueue.IdleHandler idleHandlerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_handler_demo);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        idleHandlerManager = new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                Log.i(TAG, "idleHandlerManager " + idleHandlerList.size());
                if (idleHandlerList.size() > 0) {
                    MessageQueue.IdleHandler idleHandler = idleHandlerList.get(0);
                    idleHandlerList.remove(0);
                    Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
                        @Override
                        public boolean queueIdle() {
                            boolean res = idleHandler.queueIdle();
                            if (res) {
                                longTimeIdleHandler.add(this);
                            }
                            mBaseActivityHandler.post(() -> {});
                            return res;
                        }
                    });
                }
                return true;
            }
        };
        Looper.myQueue().addIdleHandler(idleHandlerManager);


        for (int i = 0; i < 5; i++) {
            int finalI = i;
            mBaseActivityHandler.post(() -> {
                try {
                    for (int j = 0; j < 3; j++) {
                        Thread.sleep(100);
                        Log.i(TAG, "message:" + finalI + "  count 【" + j + "】");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }


        for (int i = 0; i < 10; i++) {
            int finalI = i;
            MessageQueue.IdleHandler idleHandler = new MessageQueue.IdleHandler() {
                @Override
                public boolean queueIdle() {
                    if (finalI == 0) {
                        Log.i(TAG, "idleHandler【0】 --> queueIdle");
                        return true;
                    } else {
                        Log.i(TAG, "idleHandler --> queueIdle");
                        try {
                            for (int j = 0; j < 5; j++) {
                                Thread.sleep(100);
                                Log.i(TAG, "idleHandler:" + finalI + "  count 【" + j + "】");
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        mBaseActivityHandler.post(() -> {
                            try {
                                for (int j = 0; j < 5; j++) {
                                    Thread.sleep(100);
                                    Log.i(TAG, "idleHandle-" + finalI + "中发出 message:" + finalI + "  count 【" + j + "】");
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        });
                        return false;
                    }
                }
            };
            idleHandlerList.add(idleHandler);
        }


        for (int i = 5; i < 10; i++) {
            int finalI = i;
            mBaseActivityHandler.post(() -> {
                try {
                    for (int j = 0; j < 3; j++) {
                        Thread.sleep(100);
                        Log.i(TAG, "message:" + finalI + "  count 【" + j + "】");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBaseActivityHandler.removeCallbacksAndMessages(null);
        Looper.myQueue().removeIdleHandler(idleHandlerManager);
        for (MessageQueue.IdleHandler handler : longTimeIdleHandler) {
            Looper.myQueue().removeIdleHandler(handler);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_handler_demo);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}