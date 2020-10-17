package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.bumptech.glide.load.engine.cache.DiskCacheAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
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
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class TestActivity extends AppCompatActivity {

    private String TAG = "TestActivity";

    private int deferStr = 10;

    Button bt1, bt2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        bt1 = findViewById(R.id.button);
        bt2 = findViewById(R.id.button2);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.start();
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.onFinish();
            }
        });
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


//        createOperator();
//        transformOperator();
//        mergeTransform();
//        functionalOperator();
        my();
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

        //直到有观察者订阅时  当前被观察对象才会被创建
        Observable<Integer> observableDefer = Observable.defer(new Callable<ObservableSource<? extends Integer>>() {
            @Override
            public ObservableSource<? extends Integer> call() throws Exception {
                return Observable.just(deferStr);
            }
        });
        deferStr = 15;
        //注：当observableDefer进行订阅时，才会调用defer（）创建被观察者对象（Observable）
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
                Log.d(TAG, "------intervalRange--value:" + aLong);
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

    //组合 合并操作符
    private void mergeTransform() {
        //concat、concatArray   组合多个观察者发送数据 按发送顺序串行执行  观察者数量<=4  concatArray()中的被观察者数量可以>4
        Observable.concat(Observable.just(1, 2),
                Observable.just(4, 5)).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "------concat--value:" + integer);
//                ------concat--value:1
//                ------concat--value:2
//                ------concat--value:4
//                ------concat--value:5
            }
        });


        //merge、mergeArray   组合多个被观察者一起发送数据 合并后按时间线并行执行  被观察者数量<=4  mergeArray()中的被观察者数量可以>4
        Observable.merge(Observable.intervalRange(0, 3, 1, 1, TimeUnit.SECONDS),
                Observable.intervalRange(2, 5, 0, 1, TimeUnit.SECONDS)).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                Log.d(TAG, "------merge--value:" + aLong);
//                ------merge--value:2
//                ------merge--value:0
//                ------merge--value:3
//                ------merge--value:1
//                ------merge--value:4
//                ------merge--value:2
//                ------merge--value:5
//                ------merge--value:6
            }
        });

        //concatDelayError  mergeDelayError  将出现error的事件推迟到其他观察者发送事件结束后才触发
        Observable.concatArrayDelayError(Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onError(new NullPointerException()); // 发送Error事件，因为使用了concatDelayError，所以第2个Observable将会发送事件，等发送完毕后，再发送错误事件
                emitter.onNext(4);
            }
        }), Observable.just(5, 6)).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "------concatArrayDelayError--value:" + integer);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "------concatArrayDelayError--onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "------concatArrayDelayError--onComplete");
            }
        });
        /**结果
         * ------concatArrayDelayError--value:1
         * ------concatArrayDelayError--value:2
         * ------concatArrayDelayError--value:3
         * ------concatArrayDelayError--value:5
         * ------concatArrayDelayError--value:6
         * ------concatArrayDelayError--onError
         */


        //合并 多个被观察者（Observable）发送的事件，生成一个新的事件序列（即组合过后的事件序列），并最终发送
        //<-- 创建第1个被观察者 -->
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "被观察者1发送了事件1");
                emitter.onNext(1);
                // 为了方便展示效果，所以在发送事件后加入2s的延迟
                Thread.sleep(1000);

                Log.d(TAG, "被观察者1发送了事件2");
                emitter.onNext(2);
                Thread.sleep(1000);

                Log.d(TAG, "被观察者1发送了事件3");
                emitter.onNext(3);
                Thread.sleep(1000);
            }
        }).subscribeOn(Schedulers.io()); // 设置被观察者1在工作线程1中工作

        //<-- 创建第2个被观察者 -->
        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Log.d(TAG, "被观察者2发送了事件A");
                emitter.onNext("A");
                Thread.sleep(1000);

                Log.d(TAG, "被观察者2发送了事件B");
                emitter.onNext("B");
                Thread.sleep(1000);

                Log.d(TAG, "被观察者2发送了事件C");
                emitter.onNext("C");
                Thread.sleep(1000);

                Log.d(TAG, "被观察者2发送了事件D");
                emitter.onNext("D");
                Thread.sleep(1000);
            }
        }).subscribeOn(Schedulers.newThread());// 设置被观察者2在工作线程2中工作
        // 假设不作线程控制，则该两个被观察者会在同一个线程中工作，即发送事件存在先后顺序，而不是同时发送

        //<-- 使用zip变换操作符进行事件合并 -->
        // 注：创建BiFunction对象传入的第3个参数 = 合并后数据的数据类型
        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String string) throws Exception {
                return integer + string;
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String value) {
                Log.d(TAG, "------zip--value:" + value);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.d(TAG, "------zip--onComplete");
            }
        });

        //当两个Observables中的任何一个发送了数据后，将先发送了数据的Observables 的最新（最后）一个数据 与 另外一个Observable发送的每个数据结合，最终基于该函数的结果发送数据
        //与zip的区别  zip是数量上的合并  后者是按时间合并  即在同一时间点上合并
        Observable.combineLatest(Observable.just(1l, 2l, 3l), Observable.intervalRange(0, 3, 1, 1, TimeUnit.SECONDS), new BiFunction<Long, Long, Long>() {
            @Override
            public Long apply(Long aLong, Long aLong2) throws Exception {
                //along=第一个obeservable发送的最后一个数据
                //along2=第二个observable发送的每一个数据
                Log.d(TAG, "------combineLatest--合并的数据：" + aLong + "  " + aLong2);
                return aLong + aLong2;
            }
        }).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                Log.d(TAG, "------combineLatest--最终的结果：" + aLong);
            }
        });


        //把被观察者需要发送的事件聚合成1个事件 & 发送
        Observable.just(1, 2, 3, 4).reduce(new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) throws Exception {
                Log.d(TAG, "------reduce本次计算的数据是： " + integer + " 乘 " + integer2);

                return integer * integer2;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "------reduce--最终结果：" + integer);
                //------reduce--最终结果：24
            }
        });


        //将被观察者Observable发送的数据事件收集到一个数据结构里
        Observable.just(1, 2, 3, 4, 5, 6).collect(new Callable<ArrayList<Integer>>() {
            @Override
            public ArrayList<Integer> call() throws Exception {
                return new ArrayList<>();
            }
        }, new BiConsumer<ArrayList<Integer>, Integer>() {
            @Override
            public void accept(ArrayList<Integer> list, Integer integer) throws Exception {
                list.add(integer);
            }
        }).subscribe(new BiConsumer<ArrayList<Integer>, Throwable>() {
            @Override
            public void accept(ArrayList<Integer> integers, Throwable throwable) throws Exception {
                for (Integer integer : integers) {
                    Log.d(TAG, "------collect--最终结果：" + integer);
                }
            }
        });


        //在一个被观察者发送事件前，追加发送一些数据 / 一个新的被观察者
        Observable.just(4, 5, 6)
                .startWith(0)  // 追加单个数据 = startWith()
                .startWithArray(1, 2, 3) // 追加多个数据 = startWithArray()
                .startWith(Observable.just(7, 8))
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.e(TAG, "------startWith--最终结果：" + integer);
                        //7,8,1,2,3,0,4,5,6

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


        //统计被观察者发送事件的数量
        Observable.just(1, 2, 3, 4).count().subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                Log.d(TAG, "------count--发送事件数量：" + aLong);
                //------count--发送事件数量：4
            }
        });

    }


    private int i = 0;

    //功能性操作符
    private void functionalOperator() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onError(new Exception("发生错误了"));
                e.onNext(3);
            }
        })
                .retry(2) // 遇到错误时，让被观察者重新发射数据（若一直错误，则一直重新发送
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer value) {
                        Log.d(TAG, "---------接收到了事件" + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "--------对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "--------对Complete事件作出响应");
                    }
                });


        Observable.just(1, 2, 4).repeatWhen(new Function<Observable<Object>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Observable<Object> objectObservable) throws Exception {

                return objectObservable.flatMap(new Function<Object, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Object o) throws Exception {
                        Log.d(TAG, "-----repeatWhen--进来了-value：" + i);
                        if (i > 3) {
                            return Observable.error(new Throwable("轮询结束"));
                        }
                        return Observable.just(1).delay(2000, TimeUnit.MILLISECONDS);

//                         return Observable.just(1);

                    }
                });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "-----repeatWhen---value：" + integer);
                        i++;
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "-----repeatWhen---error：" + e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    ObservableEmitter<Long> myEmitter;
    int x = 60;
    CountDownTimer countDownTimer;

    private void my() {

        int progress = 0;
        int myProgress = 0;

        Observable<Long> observable = Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> emitter) throws Exception {
                myEmitter = emitter;
            }
        }).debounce(5, TimeUnit.SECONDS);

        observable.subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                Log.i(TAG, "--------just：" + aLong);
            }
        });


        countDownTimer = new CountDownTimer(60000, 6000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.i(TAG, "--------onTick：" + millisUntilFinished);
                myEmitter.onNext(millisUntilFinished);

            }

            @Override
            public void onFinish() {

            }
        };


    }


}



