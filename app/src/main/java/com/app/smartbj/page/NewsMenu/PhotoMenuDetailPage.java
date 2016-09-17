package com.app.smartbj.page.NewsMenu;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.app.smartbj.page.BasePageMenuDetail;

/**
 * 新闻菜单-组图
 * Created by 14501_000 on 2016/9/16.
 */

public class PhotoMenuDetailPage extends BasePageMenuDetail {

    public PhotoMenuDetailPage(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        TextView view = new TextView(mActivity);
        view.setText("菜单详情页-组图");
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);
        return view;
    }
}
