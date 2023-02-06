package com.example.myapplication.glidedemo;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public class ActivityLifecycle implements MyLifecycle {

    Set<MyLifecycleListener> listenerSet = Collections.newSetFromMap(new WeakHashMap<MyLifecycleListener, Boolean>()); // 相当于 WeakHashSet
    private boolean isStarted;
    private boolean isDestroyed;

    @Override
    public void addListener(MyLifecycleListener lifecycleListener) {
        listenerSet.add(lifecycleListener);

        if (isDestroyed) {
            lifecycleListener.onDestroy();
        } else if (isStarted) {
            lifecycleListener.onStart();
        } else {
            lifecycleListener.onStop();
        }
    }

    @Override
    public void removeListener(MyLifecycleListener lifecycleListener) {
        listenerSet.remove(lifecycleListener);
    }

    void onStart() {
        isStarted = true;
        for (MyLifecycleListener lifecycleListener : listenerSet) {
            lifecycleListener.onStart();
        }
    }

    void onStop() {
        isStarted = false;
        for (MyLifecycleListener lifecycleListener : listenerSet) {
            lifecycleListener.onStop();
        }
    }

    void onDestroy() {
        isDestroyed = true;
        for (MyLifecycleListener lifecycleListener : listenerSet) {
            lifecycleListener.onDestroy();
        }
    }
}
