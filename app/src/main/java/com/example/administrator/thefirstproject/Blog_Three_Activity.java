package com.example.administrator.thefirstproject;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class Blog_Three_Activity extends AppCompatActivity {
    private TextView tvShow;
    Observable<String> observable;
    Subscriber<String> subscriber;
    Subscription subscription = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog__three_);
        initView();
        init();
    }
    private void initView() {
        tvShow = (TextView) findViewById(R.id.id_tv_show);
    }
    private void init() {
//        test_2();
        test_3();
    }
    /***
     * 测试 2：
     *   异步
     *      操作符 observerOn 和 subscribeOn
     */
    private void test_2() {
            retrieveImage("传入图片的url")
                    .map(new Func1<String, Bitmap>() {
                        @Override
                        public Bitmap call(String url) {
                            Bitmap bm = null;
                            Log.i("当前线程(为子线程) : ", Thread.currentThread().getId() + "");
                            return bm;
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Bitmap>() {
                        @Override
                        public void call(Bitmap bitmap) {
                            tvShow.setText("嗯。。改变UI，很明显这玩意在主线程里");
                        }
                    });
    }
    private Observable<String> retrieveImage(String url) {
        return Observable.just(url);
    }
    /***
     *  测试3：
     *      Subscription 对象
     *          当调用Observable.subscribe()，会返回一个Subscription对象。这个对象代表了被观察者和订阅者之间的联系。
     */
    private void test_3() {
        //TODO:
        //这个对象代表了被观察者和订阅者之间的联系。
        observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("哈哈哈哈喽 沃德");
//                subscriber.onCompleted();
            }
        });     //一个Observable对象
        subscriber = new Subscriber<String>() {     //一个Subscriber对象
            @Override
            public void onCompleted() {}
            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Log.d("嗯。。存在错误", "存在错误");
            }
            @Override
            public void onNext(String str) {
                Log.i("onNext : ",  str);
                //解除绑定 (在内部类中可以更改属性值（属性可以不用定义为final），但局部变量必须定义为final)
            }
        };
        subscription = observable.subscribe(subscriber);


//        subscription = Observable.just("字符串")
//                .subscribe(new Action1<String>() {
//                    @Override
//                    public void call(String s) {
//                        Log.i("内容 ", s);
//                    }
//                });
//        Log.i("绑定状态。。", subscription.isUnsubscribed() + "");
    }
}
