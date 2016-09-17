package com.app.smartbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by 14501_000 on 2016/9/12.
 */

public class NoScrollViewPager extends ViewPager {

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;// 重写此方法, 触摸时什么都不做, 从而实现对滑动事件的禁用
    }
    //事件拦截
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;//不拦截子控件事件
    }
}
