package com.app.smartbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by 14501_000 on 2016/9/18.
 */

public class TopNewsViewPager extends ViewPager {
    int startX,startY;
    public TopNewsViewPager(Context context) {
        super(context);
    }

    public TopNewsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    /**拦截要求：
     * 1. 上下滑动需要拦截
     * 2. 向右滑动并且当前是第一个页面,需要拦截
     * 3. 向左滑动并且当前是最后一个页面,需要拦截
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX= (int) ev.getX();
                startY= (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int endX= (int) ev.getX();
                int endY= (int) ev.getY();

                int dx=endX-startX;
                int dy=endY-startY;
                if(Math.abs(dx)<Math.abs(dy)){
                    //上下滑动
                    getParent().requestDisallowInterceptTouchEvent(false);
                }else{
                    int currentPage=getCurrentItem();
                    //左右滑动
                    if(dx>0){
                        //向右划
                        if(currentPage==0){
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }else{
                        int count=getAdapter().getCount();
                        //向左划
                        if(currentPage==(count-1)){
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:break;
        }

        return super.dispatchTouchEvent(ev);
    }
}
