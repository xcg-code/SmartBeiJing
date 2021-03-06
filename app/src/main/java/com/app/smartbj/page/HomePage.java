package com.app.smartbj.page;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by 14501_000 on 2016/9/12.
 */

public class HomePage extends BasePage {
    public HomePage(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        //给帧布局填充数据
        TextView view = new TextView(mActivity);
        view.setText("首页");
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);
        fl_content.addView(view);
        // 修改页面标题
        tv_title.setText("智慧北京");
        // 隐藏菜单按钮
        bt_menu.setVisibility(View.GONE);
    }
}
