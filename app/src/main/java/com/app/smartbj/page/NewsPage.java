package com.app.smartbj.page;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;

import com.app.smartbj.Configs;
import com.app.smartbj.MainActivity;
import com.app.smartbj.domain.NewsMenu;
import com.app.smartbj.fragment.LeftMenuFragment;
import com.app.smartbj.page.NewsMenu.InteracMenuDetailPage;
import com.app.smartbj.page.NewsMenu.NewsMenuDetailPage;
import com.app.smartbj.page.NewsMenu.PhotoMenuDetailPage;
import com.app.smartbj.page.NewsMenu.TopicMenuDetailPage;
import com.app.smartbj.utils.CacheUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

/**
 * Created by 14501_000 on 2016/9/12.
 */

public class NewsPage extends BasePage {

    private NewsMenu mNewsData;
    private ArrayList<BasePageMenuDetail> mMenuDetailPagers;// 菜单详情页集合

    public NewsPage(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        tv_title.setText("新闻");
        // 显示菜单按钮
        bt_menu.setVisibility(View.VISIBLE);
        String cache = CacheUtils.getCache(mActivity, Configs.CATEGORY_URL);
        if (!TextUtils.isEmpty(cache)) {//判断是否有缓存
            processData(cache);//加载缓存
        }
        //请求服务器，获取数据,使用开源框架okHttp
        getDataFromServer();
    }

    /**
     * 从服务器获取数据 需要权限:<uses-permission android:name="android.permission.INTERNET"
     */
    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, Configs.CATEGORY_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                System.out.println("请求总体数据成功,result=" + result);
                processData(result);//解析JSON数据
                CacheUtils.setCache(mActivity, Configs.CATEGORY_URL, result);//写入缓存
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                System.out.println("请求失败");

            }
        });
    }

    /**
     * 解析JSON数据
     *
     * @param result
     */
    private void processData(String result) {
        Gson gson = new Gson();
        mNewsData = gson.fromJson(result, NewsMenu.class);

        //初始化新闻菜单详情页
        mMenuDetailPagers = new ArrayList<BasePageMenuDetail>();
        mMenuDetailPagers.add(new NewsMenuDetailPage(mActivity, mNewsData.data.get(0).children));
        mMenuDetailPagers.add(new TopicMenuDetailPage(mActivity));
        mMenuDetailPagers.add(new PhotoMenuDetailPage(mActivity));
        mMenuDetailPagers.add(new InteracMenuDetailPage(mActivity));

        //获取侧边栏对象
        MainActivity mainUI = (MainActivity) mActivity;
        LeftMenuFragment leftMenuFragment = mainUI.getLeftMenuFragment();
        leftMenuFragment.setMenuData(mNewsData.data);

        //设置初始布局
        setCurrentDetailPage(0);

    }


    public void setCurrentDetailPage(int position) {
        BasePageMenuDetail page = mMenuDetailPagers.get(position);
        View view = page.mRootView;// 当前页面的布局

        fl_content.removeAllViews();  //清除所有旧布局
        fl_content.addView(view);// 给帧布局添加布局
        page.initData();
        tv_title.setText(mNewsData.data.get(position).title);// 更新标题
    }
}
