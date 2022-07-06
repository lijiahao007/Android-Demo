package com.example.myapplication.rxjava;

import org.junit.Before;
import org.junit.Test;
import org.reactivestreams.Subscription;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableEmitter;
import io.reactivex.rxjava3.core.FlowableOnSubscribe;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ObservableDemo {

    private Observer<String> defaultObserver;

    @Before
    public void setUp() throws Exception {
        defaultObserver = new Observer<String>() {
            Disposable disposable;
            ;

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                System.out.println("onSubscribe");
                this.disposable = d;
            }

            @Override
            public void onNext(@NonNull String s) {
                System.out.println("onNext: " + s);
//                if (s.equals("Hello")) {
//                    disposable.dispose(); // 主动取消订阅
//                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                System.out.println("onError" + e);
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        };
    }

    @Test
    public void ObservableTest() {
        // 1. 使用create()方法创建Observable对象
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                emitter.onNext("Hello");
                emitter.onNext("World");
                emitter.onComplete();
            }
        }).subscribe(defaultObserver);

        System.out.println();

        // 2. 使用just()方法创建Observable对象  (最后会自动调用emitter.onComplete())
        Observable.just("Hello1", "World1").subscribe(defaultObserver);

        // 3. 使用fromArray()方法创建Observable对象
        String[] strs = {"Hello2", "World2"};
        Observable<String> stringObservable = Observable.fromArray(strs);
        stringObservable.subscribe(defaultObserver);

    }

    @Test
    public void FlowableTest() {
        Flowable.create(new FlowableOnSubscribe<String>() {
            int count = 0;

            @Override
            public void subscribe(@NonNull FlowableEmitter<String> emitter) throws Throwable {
                while(true) {
//                    System.out.println("\t\t\t" + count);
                    emitter.onNext(count++ + "");
                    try {
                        Thread.sleep(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, BackpressureStrategy.DROP)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new FlowableSubscriber<String>() {
            @Override
            public void onSubscribe(@NonNull Subscription s) {
                System.out.println("onSubscribe");
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(String s) {
                System.out.println("onNext: " + s);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("onError:" + t);
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });

        while(true);
    }
}
