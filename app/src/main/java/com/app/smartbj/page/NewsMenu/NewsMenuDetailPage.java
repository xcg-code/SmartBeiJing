package com.app.smartbj.page.NewsMenu;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.app.smartbj.MainActivity;
import com.app.smartbj.R;
import com.app.smartbj.domain.NewsMenu;
import com.app.smartbj.page.BasePageMenuDetail;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

/**
 * 新闻菜单-新闻
 * Created by 14501_000 on 2016/9/16.
 */

public class NewsMenuDetailPage extends BasePageMenuDetail {
    private ViewPager mViewPager;
    private ArrayList<NewsMenu.NewsTabData> mTabData;
    private ArrayList<TabDetailPage> pageList;
    private TabPageIndicator indicator;
    private ImageButton mImageButton;

    public NewsMenuDetailPage(Activity activity, ArrayList<NewsMenu.NewsTabData> children) {
        super(activity);
        mTabData=children;
    }

    @Override
    public View initView() {
        View view=View.inflate(mActivity, R.layout.news_detail_page,null);
        mViewPager = (ViewPager) view.findViewById(R.id.vp_news);
        mImageButton = (ImageButton) view.findViewById(R.id.bt_next);
        indicator= (TabPageIndicator) view.findViewById(R.id.tpi_indicator);
        return view;
    }

    @Override
    public void initData() {
        pageList=new ArrayList<TabDetailPage>();
        for (int i = 0; i <mTabData.size() ; i++) {
            TabDetailPage page=new TabDetailPage(mActivity,mTabData.get(i));
            pageList.add(page);
        }
        mViewPager.setAdapter(new NewsMenuDetailAdapter());
        indicator.setViewPager(mViewPager);//将ViewPager与指示器绑定

        // 此处必须给指示器设置页面监听,不能设置给viewpager
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    setSlidingMenuEnable(true);
                }else{
                    setSlidingMenuEnable(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳到下个页面
                int currentItem = mViewPager.getCurrentItem();
                currentItem++;
                mViewPager.setCurrentItem(currentItem);
            }
        });
    }
    class NewsMenuDetailAdapter extends PagerAdapter {
        //指定指示器的标题
        @Override
        public CharSequence getPageTitle(int position) {
            return mTabData.get(position).title;
        }

        @Override
        public int getCount() {
            return pageList.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            TabDetailPage pager=pageList.get(position);
            View view=pager.mRootView;
            container.addView(view);
            pager.initData();
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
    }
    //开启或禁用侧边栏
    public void setSlidingMenuEnable(boolean enable){
        MainActivity activity= (MainActivity) mActivity;
        SlidingMenu slidingMenu=activity.getSlidingMenu();
        if(enable){
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else{
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

}
