package com.app.smartbj.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.app.smartbj.MainActivity;
import com.app.smartbj.R;
import com.app.smartbj.page.BasePage;
import com.app.smartbj.page.GovAffairsPage;
import com.app.smartbj.page.HomePage;
import com.app.smartbj.page.NewsPage;
import com.app.smartbj.page.SettingPage;
import com.app.smartbj.page.SmartServicePage;
import com.app.smartbj.view.NoScrollViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

/**
 * Created by 14501_000 on 2016/9/12.
 */

public class ContentFragment extends BaseFragment {
    private NoScrollViewPager mViewPager;
    private RadioGroup mRadioGroup;

    private ArrayList<BasePage> mPagerList;//五个标签页

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        mViewPager = (NoScrollViewPager) view.findViewById(R.id.vp_content);
        mRadioGroup = (RadioGroup) view.findViewById(R.id.rg_group);
        return view;
    }

    @Override
    public void initData() {
        mPagerList = new ArrayList<BasePage>();

        //添加5个标签页
        mPagerList.add(new HomePage(mActivity));
        mPagerList.add(new NewsPage(mActivity));
        mPagerList.add(new SmartServicePage(mActivity));
        mPagerList.add(new GovAffairsPage(mActivity));
        mPagerList.add(new SettingPage(mActivity));

        mViewPager.setAdapter(new ContentAdapter());
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        // 首页
                        mViewPager.setCurrentItem(0, false);// 参2:表示是否具有滑动动画
                        break;
                    case R.id.rb_news:
                        // 新闻中心
                        mViewPager.setCurrentItem(1, false);
                        break;
                    case R.id.rb_smart:
                        // 智慧服务
                        mViewPager.setCurrentItem(2, false);
                        break;
                    case R.id.rb_gov:
                        // 政务
                        mViewPager.setCurrentItem(3, false);
                        break;
                    case R.id.rb_setting:
                        // 设置
                        mViewPager.setCurrentItem(4, false);
                        break;
                    default:
                        break;
                }
            }
        });
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                BasePage page = mPagerList.get(position);
                page.initData();
                if (position == 0 || position == 4) {
                    setSlidingMenuEnable(false);
                } else {
                    setSlidingMenuEnable(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // 手动加载第一页数据
        mPagerList.get(0).initData();
        setSlidingMenuEnable(false);
    }

    private class ContentAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mPagerList.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            BasePage page = mPagerList.get(position);
            View view = page.mRootView;// 获取当前页面对象的布局
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    /**
     * 开启或禁用侧边栏
     *
     * @param enable
     */
    public void setSlidingMenuEnable(boolean enable) {
        //获取侧边栏对象
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        if (enable) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

    //获取新闻中心页面
    public NewsPage getNewsPage() {
        NewsPage page= (NewsPage) mPagerList.get(1);
        return page;
    }
}
