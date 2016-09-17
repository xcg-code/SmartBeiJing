package com.app.smartbj.page.NewsMenu;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.app.smartbj.domain.NewsMenu;
import com.app.smartbj.page.BasePageMenuDetail;

/**
 * Created by 14501_000 on 2016/9/17.
 */

public class TabDetailPage extends BasePageMenuDetail{
    private NewsMenu.NewsTabData mTabData;
    private  TextView view;

    public TabDetailPage(Activity activity, NewsMenu.NewsTabData newsTabData) {
        super(activity);
        mTabData=newsTabData;
    }

    @Override
    public View initView() {
       view=new TextView(mActivity);
        //view.setText(mTabData.title);//此处初始化数据回报空指针异常
        view.setTextSize(25);
        view.setTextColor(Color.RED);
        view.setGravity(Gravity.CENTER);
        return view;
    }

    @Override
    public void initData() {
        view.setText(mTabData.title);
    }
}
