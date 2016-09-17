package com.app.smartbj.page;

import android.app.Activity;
import android.view.View;

/**
 * 菜单详情页面基类
 * Created by 14501_000 on 2016/9/16.
 */

public abstract class BasePageMenuDetail {
    public Activity mActivity;
    public View mRootView;// 菜单详情页根布局
    public BasePageMenuDetail(Activity activity){
        mActivity=activity;
        mRootView=initView();
    }
    //抽象方法，初始化布局，必须子类实现
    public abstract View initView();

    //初始化数据，不必须实现
    public  void initData(){

    }
}
