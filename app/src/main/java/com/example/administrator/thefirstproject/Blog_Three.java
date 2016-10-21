package com.example.administrator.thefirstproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.internal.operators.OnSubscribeRedo;
import rx.schedulers.Schedulers;

/**
 * Blog 3  响应式的好处
 */
public class Blog_Three {
    public Blog_Three() {
//        test_1();
        test_2();
    }
    /***
     * 测试1:
     *  onComplete Observable对象在终结的时候调用
     *  onError    操作符调用链中一旦抛出异常 就会直接执行此方法（跳过后面操作符）
     *      特点：所有错误处理交给订阅者（Subscriber）来做
     */
    private void test_1() {
        Observable.just("Hello World")
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return potentialException(s);    //这玩意会抛出异常
                    }
                })
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String str) {
                        return anotherPotentialException(str);
                    }
                })
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("...onCompleted!!!");
                    }
                    @Override
                    public void onError(Throwable e) {   //因为上面会抛出异常 所以这里会执行
                        System.out.println("...onError.."); //这极大的简化了错误处理。只需要在一个地方处理错误即可以。
                    }
                    @Override
                    public void onNext(String s) {
                    }
                });
    }
    private String potentialException(String str) {
        return 10 / 0 + "";            //抛出一个异常玩下
    }
    private String anotherPotentialException(String s) {
        String nullStr = null;
        return nullStr.length() + "";  //再来个异常
    }
    /***
     *  异步
     *     操作符 observerOn 和 subscribeOn
     */
    private void test_2() {
        Observable.just("图片的url")
                .map(new Func1<String, Bitmap>() {
                    @Override
                    public Bitmap call(String url) {
                        System.out.println("在这里进行加载图片操作");
                        Bitmap bm = null;
                        return bm;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        System.out.println("在这里控制UI");
                    }
                });
    }
    private Observable<String> retrieveImage(String url) {
        return Observable.just(url);
    }
    public static void main(String[] args) {
        new Blog_Three();
    }
}
