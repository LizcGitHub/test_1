package com.example.administrator.thefirstproject;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

class ItemBean {
    String url;
    String title;
    String content;
    public ItemBean(String url, String title, String content) {
        this.url = url;
        this.title = title;
        this.content = content;
    }
}
/**
 *  博客2 操作符   flatMap  filter
 */
public class Blog_Two {
    private List<ItemBean> data = new ArrayList<>();
    //初始化一些测试数据
    private void initData() {
        data.add(new ItemBean("url_1", "tile_1", "content_1"));
        data.add(new ItemBean("url_2", "tile_2", "content_2"));
        data.add(new ItemBean("url_3", "tile_3", "content_3"));
    }
    public Blog_Two() {
        initData();
        /***
         *  RxJava
         */
//        test_0();
//        test_1();
//        test_2();
//        test_3();
        test_4();
    }
    /**
     * 测试0：最原始版：通过普通for循环实现遍历
     */
    private void test_0() {
        /***/
                query("Hello World")
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> urls) {
                        for (String url : urls) {
                            System.out.println(url);
                        }
                    }
                });
    }
    /**
     * 测试1：原始版 Observalbe.from().subscribe() 实现迭代
     */
    private void test_1() {
        query("传入要查询的字符串")
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> urls) {
                        Observable.from(urls).subscribe(new Action1<String>() {
                            @Override
                            public void call(String url) { //Observable.from() 传入一个迭代器  .subscribe方法自动迭代
                                System.out.println(url);
                            }
                        });
                    }
                });
    }
    /***
     *  测试2：通过flatMap 和 Observable.from实现遍历
     */
    private void test_2() {
        query("还是传入要查询的字符串")
                //flatMap 可以返回任何 它想返回的Observerable对象
                .flatMap(new Func1<List<String>, Observable<String>>() {  //参数泛型为T, R
                    @Override
                    public Observable<String> call(List<String> urls) {   //call参数泛型为T 返回值泛型为R
                        return Observable.from(urls);   //flatMap返回的是一个Observable对象//该对象即是.subscribe需要的 Subscriber不再收到List<String>，而是收到一些列单个的字符串，就像Observable.from()的输出一样
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String url) {
                        System.out.println(url);
                    }
                });
    }
    /**
     * 测试3：现在我不想打印URL了，而是要打印收到的每个网站的标题。
     *           问题来了，我的方法每次只能传入一个URL，并且返回值不是一个String，而是一个输出String的Observabl对象。
     *        重点：flatMap返回的是一个Observable对象
     */
    private void test_3() {
        query("又是一个需要查询的字符串")
                .flatMap(new Func1<List<String>, Observable<String>>() {    //遍历所有url
                    @Override
                    public Observable<String> call(List<String> urls) {
                        return Observable.from(urls);//Observable.from返回的就是 urls中单个元素的Observable(语死早)
                    }
                })
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String url) {     //查询单个url对应的title  //flatMap返回的是一个Observable对象
                        return getTitle(url);    //一个url（需要查询的title的url）
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String title) {
                        System.out.println("输出查询到的标题：" + title);
                    }});
    }
    /**
     * 测试4：其他操作符
     *        filter: 过滤掉那些不满足检查条件的(事件), 好像是只要boolean为false就不发出(emit)事件
     *        take :  好像是 设置 Observable最多发出(emit)事件的数量
     *        doOnNext: doOnNext用于在处理事件(监听者) 之前执行一些操作
     */
    private void test_4() {
        query("又是一个需要查询的字符串")
                .flatMap(new Func1<List<String>, Observable<String>>() {    //遍历所有url
                    @Override
                    public Observable<String> call(List<String> urls) {
                        return Observable.from(urls);//Observable.from返回的就是 urls中单个元素的Observable(语死早)
                    }
                })
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String url) {     //查询单个url对应的title  //flatMap返回的是一个Observable对象
                        return getTitle(url);    //一个url（需要查询的title的url）
                    }
                })
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String title) {//用于判断title是否为null //好像是只要boolean为false就不发出(emit)事件
                        return title != null;  //call返回的是一个boolean型
                    }
                })
                .take(2)           //好像是 设置 Observable最多发出(emit)事件的数量
                .doOnNext(new Action1<String>() {    //doOnNext用于在处理事件(监听者) 之前执行一些操作
                    @Override
                    public void call(String title) {
                        doSave2Disk(title);
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String title) {
                        System.out.println("输出查询到的标题：" + title);
                    }});
    }
    private void doSave2Disk(String title) {
        //TODO:将title保存到外存中
        System.out.println("嗯。。保存到外存~~" + title);
    }
    private Observable<String> getTitle(String url) {
        //测试数据：遍历找到 url 所对应item的 title
        String title = null;
        for (ItemBean item : data) {
            if (item.url.equals(url)) {
                title = item.title;
            }
        }
//        return null;
        return Observable.just(title);
    }
    private Observable<List<String>> query(String text) {
        List<String> list = new ArrayList<>();   //测试数据根据text返回网站的上的所有url
        list.add("url_1");
        list.add("url_2");
        list.add("url_3");
        return Observable.just(list);
    }
    public static void main(String[] args) {
        new Blog_Two();
    }
}
