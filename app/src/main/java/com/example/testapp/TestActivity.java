package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class TestActivity extends AppCompatActivity {

    private String TAG = "TestActivity";

    private int deferStr = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Log.d(TAG, "-----被观察者----subscribe");
                emitter.onNext("axiba1");
                emitter.onNext("axiba2");
                emitter.onNext("axiba3");
                emitter.onComplete();

            }
        });


        observable.subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread());

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "----观察者-----开始采用subscribe连接");

            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "----观察者-----对Next事件作出响应" + s);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "----观察者-----对Error事件作出响应");

            }

            @Override
            public void onComplete() {
                Log.d(TAG, "----观察者-----对Complete事件作出响应");

            }
        };
        //普通订阅事件
        observable.subscribe(observer);


        transformOperator();

    }


    //创建操作符
    private void createOperator() {
        //快速创建事件
        Observable.just("hello").subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "------Consumer接口" + s);
            }
        });
        //快速创建事件
        Observable.fromArray(new Integer[]{1, 2, 3}).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "------fromArray--value:" + integer);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
        //快速创建事件
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        Observable.fromIterable(list).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "------fromIterable--value:" + s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
        //<-- empty()  -->
        // 该方法创建的被观察者对象发送事件的特点：仅发送Complete事件，直接通知完成
        Observable observable1 = Observable.empty();
        // 即观察者接收后会直接调用onCompleted（）

        //<-- error()  -->
        // 该方法创建的被观察者对象发送事件的特点：仅发送Error事件，直接通知异常
        // 可自定义异常
        Observable observable2 = Observable.error(new RuntimeException());
        // 即观察者接收后会直接调用onError（）

        // < --never()-- >
        // 该方法创建的被观察者对象发送事件的特点：不发送任何事件
        Observable observable3 = Observable.never();
        // 即观察者接收后什么都不调用


        //

        //直到有观察者订阅是  当前被观察对象才会被创建
        Observable<Integer> observableDefer = Observable.defer(new Callable<ObservableSource<? extends Integer>>() {
            @Override
            public ObservableSource<? extends Integer> call() throws Exception {
                return Observable.just(deferStr);
            }
        });
        deferStr = 15;
        //注：此时，才会调用defer（）创建被观察者对象（Observable）
        observableDefer.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "------defer--value:" + integer);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        //延迟指定时间后，发送1个数值0（Long类型）
        Observable.timer(2, TimeUnit.SECONDS).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Long aLong) {
                Log.d(TAG, "------defer--value:" + aLong);//打印结果 ------defer--value:0
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        //发送事件的特点：每隔指定时间 就发送 事件     发送的事件序列 = 从0开始、无限递增1的的整数序列
        Observable.interval(3, 1, TimeUnit.SECONDS).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Long aLong) {
                Log.d(TAG, "------interval--value:" + aLong);
                //打印结果 ------defer--value:0
                //打印结果 ------defer--value:1
                //打印结果 ------defer--value:2
                //打印结果 ------defer--value:...
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        //发送事件的特点：每隔指定时间 就发送 事件，可指定发送的数据的数量
        Observable.intervalRange(11, 3, 0, 3, TimeUnit.SECONDS).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Long aLong) {
                Log.d(TAG, "------interval--value:" + aLong);
                //打印结果 ------defer--value:11
                //打印结果 ------defer--value:12
                //打印结果 ------defer--value:13
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        //发送事件的特点：连续发送 1个事件序列，可指定范围  无延迟发送事件
        Observable.range(8, 3).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "------range--value:" + integer);
                //打印结果 ------defer--value:8
                //打印结果 ------defer--value:9
                //打印结果 ------defer--value:10
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }

    //变换操作符
    public void transformOperator() {


        //map变换操作符
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
        }).map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                return "哎西吧" + integer;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "------map--value:" + s);
            }


        });

        //flatMap操作符  无序（但日志打印出来是有序的  很奇怪）
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }

            // 采用flatMap（）变换操作符
        }).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                final List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("我是事件 " + integer + "拆分后的子事件" + i);
                    // 通过flatMap中将被观察者生产的事件序列先进行拆分，再将每个事件转换为一个新的发送三个String事件
                    // 最终合并，再发送给被观察者
                }
                return Observable.fromIterable(list);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "------flatMap--value:" + s);
            }
        });


        //flatMap操作符  有序（但日志打印出来是有序的  很奇怪）
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("1");
                emitter.onNext("2");
                emitter.onNext("3");
            }
        }).concatMap(new Function<String, ObservableSource<List>>() {
            @Override
            public ObservableSource<List> apply(String s) throws Exception {
                List list = new ArrayList();
                list.add("给我冲" + s);
                return Observable.just(list);
            }
        }).subscribe(new Consumer<List>() {
            @Override
            public void accept(List s) throws Exception {
                for (Object o : s) {
                    Log.d(TAG, "------concatMap--value:" + o.toString());
                }
            }
        });

        //带有缓存区的
        Observable.just(1, 2, 3, 4, 5)
                .buffer(3, 1)// params1每次从被观察者中获取事件的数量  params2每次获取新事件的数量，也就是游标要移动的长度
                .subscribe(new Observer<List<Integer>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Integer> integers) {
                        Log.d(TAG, "------buffer--value:" + integers.size());
                        for (Integer integer : integers) {
                            Log.d(TAG, "------buffer--value:" + integer);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

}
