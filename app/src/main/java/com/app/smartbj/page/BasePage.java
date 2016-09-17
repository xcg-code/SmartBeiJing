package com.app.smartbj.page;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.smartbj.MainActivity;
import com.app.smartbj.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * 五个标签页的基类
 * Created by 14501_000 on 2016/9/12.
 */

public class BasePage {
    public Activity mActivity;
    public TextView tv_title;
    public ImageButton bt_menu;
    public FrameLayout fl_content;
    public View mRootView;

    public BasePage(Activity activity) {
        mActivity = activity;
        mRootView = initView();
    }

    //初始化布局
    public View initView() {
        View view = View.inflate(mActivity, R.layout.base_page, null);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        bt_menu = (ImageButton) view.findViewById(R.id.btn_menu);
        fl_content = (FrameLayout) view.findViewById(R.id.fl_content);
        bt_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
        return view;
    }

    //初始化数据
    public void initData() {

    }

    private void toggle() {
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        slidingMenu.toggle();// 如果当前状态是开, 调用后就关; 反之亦然
    }
}
