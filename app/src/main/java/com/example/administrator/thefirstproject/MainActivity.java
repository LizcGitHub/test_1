package com.example.administrator.thefirstproject;

import android.bluetooth.BluetoothA2dp;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private TextView mTv;
    Observable<String> mOb;   //被观察者 触发事件 （事件源）
    Subscriber<String> mSu;   //订阅者 监听者
    Subscription mSup;        //用于取消订阅的
    private String time;
    String strA = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        init();
    }
    private void init() {
        /**
         *  1.Hello World 嗯。。hellworld
         */
//        mOb = rx.Observable.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//                //发生的事件   参数是个订阅者
//                //TODO:...
//                subscriber.onNext("哈哈哈哈喽 沃德");
//                subscriber.onCompleted();
//            }
//        });
        //TODO:好像不需要下面这玩意
//        mSu = new Subscriber<String>() {
//            @Override
//            public void onCompleted() {}
//            @Override
//            public void onError(Throwable e) {}
//            @Override
//            public void onNext(String str) {
//                mTv.setText(str);
//            }
//        };
//        /**将事件源 和 订阅者绑定*/
//        mOb.subscribe(mSu);     //subscribe方法有一个重载版本，接受三个Action1类型的参数，分别对应OnNext，OnComplete， OnError函数。
        /**Observable可以直接绑定 Aciton 而不需要 subscribe*/
//        Action1 onNextAction = new Action1<String>() {
//            @Override
//            public void call(String s) {
//                Log.i("action1里", s);
//            }
//        };
//        mOb.subscribe(onNextAction);    //默认是next方法
        /**Lambda表达式*/
//        mOb.just("Hello Kitty").subscribe(s->System.out.println(s));
        /**
         * 2.直接just一个对象
         *      just直接产生一个只发生一个事件就结束的Observale对象
         */
//        rx.Observable.just("hello 沃德").subscribe(new Action1<String>() {
//            @Override
//            public void call(String str) {   //形参对应 just里面的类型
//                mTv.setText(str);
//            }
//        });
        /**
         * 3.计时器
         *      异步
         */
        mSup = Observable.interval(1000, TimeUnit.MILLISECONDS, Schedulers.newThread())
                .map(new Func1<Long, String>() {   //参数为 T 类型和 R 类型
                    @Override
                    public String call(Long aLong) {        //这个aLong是时间
                        Log.i("aLong ~~~", aLong + "");     //这里是在子线程里处理
                        //在这里处理事件
                        time = aLong + "";
                        return time;       //map返回的是一个R类型（）
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())  //observeOn指定的是订阅者(subscriber)执行的线程
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String str) {              //反之相反
                        mTv.setText("当前时间: " + time);
                        if (mSup == null) {
                            Toast.makeText(getBaseContext(), "嗯。。为空", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getBaseContext(), "嗯。。不为空" + mSup.isUnsubscribed(), Toast.LENGTH_SHORT).show();
                        }
                        if (str.equals("5")) {
                            mSup.unsubscribe();
                            Toast.makeText(getBaseContext(), "取消绑定后" + mSup.isUnsubscribed(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    private void initView() {
        mTv = (TextView) findViewById(R.id.id_tv);
    }
}
