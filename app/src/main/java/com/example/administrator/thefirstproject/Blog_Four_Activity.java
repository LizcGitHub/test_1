package com.example.administrator.thefirstproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import rx.Observable;

public class Blog_Four_Activity extends AppCompatActivity {
    private TextView tvShowFour;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog__four_);
        initView();
        init();
    }
    private void initView() {
        tvShowFour = (TextView) findViewById(R.id.id_tv_show_four);
    }
    private void init() {
        test_1();
    }
    /***
     * 在0.24.0，rx.android.observables.AndroidObservable 被改为了 rx.android.app.AppObservable，可以使用AppObservable.bindActivity()、AppObservable.bindFragment()、ContentObservable.fromBroadcast()等方法
     */
    private void test_1() {
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
