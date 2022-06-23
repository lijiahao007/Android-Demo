package com.example.myapplication.rxjava;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.myapplication.R;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class RxJavaDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java_demo);

        // 观察者
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.i("RxJava", "onSubscribe：" + d);
            }

            @Override
            public void onNext(@NonNull String s) {
                Log.i("RxJava", "onNext: 处理值 " + s);

            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.i("RxJava", "onError： " + e);

            }

            @Override
            public void onComplete() {
                Log.i("RxJava", "onComplete");
            }
        };

        // 被观察者
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                for (int i = 0; i < 10; i++) {
                    emitter.onNext("我是事件" + i);
                    emitter.onNext("hello");
                    emitter.onNext("world");
                }
                emitter.onComplete();
            }
        });

        observable.subscribe(observer);


        PublishSubject<String> publishSubject =  PublishSubject.create();
        publishSubject.subscribe(observer);
        publishSubject.onNext("hello1");
        publishSubject.onNext("hello2");
        publishSubject.onNext("hello3");



    }
}