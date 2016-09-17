package com.app.smartbj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.smartbj.utils.PrefUtils;

import java.util.ArrayList;

/**
 * Created by 14501_000 on 2016/9/12.
 */
public class GuideActivity extends Activity {
    private ViewPager mViewPager;
    private Button bt_start;
    private LinearLayout ll_point;

    private ArrayList<ImageView> mImageViewList;
    // 引导页图片id数组
    private int[] mImageIds = new int[]{R.drawable.guide_1,
            R.drawable.guide_2, R.drawable.guide_3};
    private int previousSelectedPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initUI();
        initData();//初始化数据
        mViewPager.setAdapter(new GuideAdapter());//设置适配器
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //当页面被选中
                if (position == (mImageIds.length - 1)) {
                    bt_start.setVisibility(View.VISIBLE);
                } else {
                    bt_start.setVisibility(View.INVISIBLE);
                }
                // 把之前的禁用, 把最新的启用, 更新指示器
                ll_point.getChildAt(previousSelectedPosition).setEnabled(false);
                ll_point.getChildAt(position).setEnabled(true);
                previousSelectedPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更新sp, 已经不是第一次进入了
                PrefUtils.setBoolean(getApplicationContext(),"is_first_enter",false);
                //跳转到主页面
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });
    }

    private void initUI() {
        mViewPager = (ViewPager) findViewById(R.id.vp_guide);
        bt_start = (Button) findViewById(R.id.bt_start);
        ll_point = (LinearLayout) findViewById(R.id.ll_container);
    }

    private void initData() {
        mImageViewList = new ArrayList<ImageView>();
        for (int i = 0; i < mImageIds.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(mImageIds[i]);
            mImageViewList.add(imageView);

            //初始化小圆点
            View pointView = new View(this);
            pointView.setBackgroundResource(R.drawable.sbg_point_selector);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20,20);
            if (i != 0) params.leftMargin = 15;
            //设置默认所有都不可用
            pointView.setEnabled(false);
            ll_point.addView(pointView,params);
        }
        ll_point.getChildAt(0).setEnabled(true);

    }


    private class GuideAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mImageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        //初始化item的布局
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = mImageViewList.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
