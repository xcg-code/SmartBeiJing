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
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
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
        System.out.println("请求");
        //请求服务器，获取数据,使用开源框架okHttp
        getDataFromServer();
    }

    /**
     * 从服务器获取数据 需要权限:<uses-permission android:name="android.permission.INTERNET"
     */
    private void getDataFromServer() {
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //创建一个Request
        final Request request = new Request.Builder()
                .url(Configs.CATEGORY_URL)
                .build();
        //new call
        Call call = mOkHttpClient.newCall(request);
        //请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                System.out.println("请求错误");

            }

            @Override
            public void onResponse(final Response response) throws IOException {
                String result = response.body().string();
                System.out.println("请求成功"+result);
                processData(result);//解析JSON数据
                CacheUtils.setCache(mActivity, Configs.CATEGORY_URL, result);//写入缓存
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
        mMenuDetailPagers=new ArrayList<BasePageMenuDetail>();
        mMenuDetailPagers.add(new NewsMenuDetailPage(mActivity));
        mMenuDetailPagers.add(new TopicMenuDetailPage(mActivity));
        mMenuDetailPagers.add(new PhotoMenuDetailPage(mActivity));
        mMenuDetailPagers.add(new InteracMenuDetailPage(mActivity));

        //okHttp的onResponse在子线程中，不能直接修改该UI，需到主线程中修改
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //获取侧边栏对象
                MainActivity mainUI = (MainActivity) mActivity;
                LeftMenuFragment leftMenuFragment = mainUI.getLeftMenuFragment();
                leftMenuFragment.setMenuData(mNewsData.data);

                //设置初始布局
                setCurrentDetailPage(0);
            }
        });


    }


    public void setCurrentDetailPage(int position) {
        BasePageMenuDetail page=mMenuDetailPagers.get(position);
        View view=page.mRootView;// 当前页面的布局

        fl_content.removeAllViews();  //清除所有旧布局
        fl_content.addView(view);// 给帧布局添加布局
        page.initData();
        tv_title.setText(mNewsData.data.get(position).title);// 更新标题
    }
}
