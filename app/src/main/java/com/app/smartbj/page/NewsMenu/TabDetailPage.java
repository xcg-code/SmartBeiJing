package com.app.smartbj.page.NewsMenu;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.app.smartbj.Configs;
import com.app.smartbj.R;
import com.app.smartbj.domain.NewsMenu;
import com.app.smartbj.domain.NewsTabBean;
import com.app.smartbj.page.BasePageMenuDetail;
import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by 14501_000 on 2016/9/17.
 */

public class TabDetailPage extends BasePageMenuDetail {
    private View view;
    private ListView mListView;
    private ViewPager mViewPager;
    private NewsTabBean newsTabBean;
    private ArrayList<NewsTabBean.TopNewsData> mTopNewsDatas;

    public TabDetailPage(Activity activity, NewsMenu.NewsTabData newsTabData) {
        super(activity);
    }

    @Override
    public View initView() {
        view = View.inflate(mActivity, R.layout.pager_tab_detail, null);
        mListView = (ListView) view.findViewById(R.id.lv_list);
        mViewPager = (ViewPager) view.findViewById(R.id.vp_pic);
        return view;
    }

    @Override
    public void initData() {
        getDataFromServer();//请求数据

    }

    private void getDataFromServer() {
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //创建一个Request
        final Request request = new Request.Builder()
                .url(Configs.News_URL_phone)
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
                System.out.println("请求成功" + result);
                processData(result);//解析JSON数据
                //CacheUtils.setCache(mActivity, Configs.CATEGORY_URL, result);//写入缓存
            }

        });
    }

    private void processData(String result) {
        Gson gson = new Gson();
        newsTabBean = gson.fromJson(result, NewsTabBean.class);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTopNewsDatas = newsTabBean.data.topnews;// 头条新闻填充数据
                if (mTopNewsDatas != null) {
                    mViewPager.setAdapter(new TopNewsAdapter());
                }
            }
        });

    }

    class TopNewsAdapter extends PagerAdapter {

        public TopNewsAdapter() {

        }
        @Override
        public int getCount() {
            return mTopNewsDatas.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = new ImageView(mActivity);
            view.setImageResource(R.drawable.topnews_item_default);
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
}
