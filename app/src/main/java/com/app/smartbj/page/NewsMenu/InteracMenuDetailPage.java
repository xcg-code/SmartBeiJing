package com.app.smartbj.page.NewsMenu;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.app.smartbj.page.BasePageMenuDetail;

/**
 * 新闻菜单-互动
 * Created by 14501_000 on 2016/9/16.
 */

public class InteracMenuDetailPage extends BasePageMenuDetail {

    public InteracMenuDetailPage(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        TextView view = new TextView(mActivity);
        view.setText("菜单详情页-互动");
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);
        return view;
    }
}
