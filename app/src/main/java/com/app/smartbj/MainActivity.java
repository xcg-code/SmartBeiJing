package com.app.smartbj;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.view.WindowManager;

import com.app.smartbj.fragment.ContentFragment;
import com.app.smartbj.fragment.LeftMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * Created by 14501_000 on 2016/9/12.
 */
public class MainActivity extends SlidingFragmentActivity{
    private static final String TAG_LEFT_MENU = "TAG_LEFT_MENU";
    private static final String TAG_CONTENT = "TAG_CONTENT";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setBehindContentView(R.layout.left_menu);
        SlidingMenu slidingMenu=getSlidingMenu();
        //全屏触摸
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //屏幕预留2/3宽度
        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        slidingMenu.setBehindOffset(width*2/3);
        
        initFragment();
    }

    /**
     * 初始化fragment
     */
    private void initFragment() {
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction transcation=fm.beginTransaction();//开启事务

        // 用fragment替换帧布局;参数1:帧布局容器的id;参数2:是要替换的fragment;参数3:标记
        transcation.replace(R.id.fl_left_menu,new LeftMenuFragment(),TAG_LEFT_MENU);
        transcation.replace(R.id.fl_main,new ContentFragment(),TAG_CONTENT);
        transcation.commit();//提交事务
    }

    //获取主页fragment对象
   public ContentFragment getContentFragment(){
        FragmentManager fm=getSupportFragmentManager();
        ContentFragment fragment= (ContentFragment) fm.findFragmentByTag(TAG_CONTENT);
        return fragment;
    }

    //获取侧边栏fragment对象
    public LeftMenuFragment getLeftMenuFragment(){
        FragmentManager fm=getSupportFragmentManager();
        LeftMenuFragment fragment= (LeftMenuFragment) fm.findFragmentByTag(TAG_LEFT_MENU);
        return fragment;
    }
}
