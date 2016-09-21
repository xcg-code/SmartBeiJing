package com.app.smartbj.page.NewsMenu;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.smartbj.Configs;
import com.app.smartbj.R;
import com.app.smartbj.domain.NewsMenu;
import com.app.smartbj.domain.NewsTabBean;
import com.app.smartbj.page.BasePageMenuDetail;
import com.app.smartbj.utils.CacheUtils;
import com.app.smartbj.view.PullToRefreshListView;
import com.app.smartbj.view.TopNewsViewPager;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

/**
 * Created by 14501_000 on 2016/9/17.
 */

public class TabDetailPage extends BasePageMenuDetail {
    private View view;
    private PullToRefreshListView mListView;
    private TopNewsViewPager mViewPager;
    private NewsTabBean newsTabBean;
    private ArrayList<NewsTabBean.TopNewsData> mTopNewsDatas;
    private ArrayList<NewsTabBean.NewsData> mNewsDatas;
    private String mUrl;
    private NewsMenu.NewsTabData mTabData;// 单个页签的网络数据
    private TextView mTextView;
    private CirclePageIndicator mIndicator;
    private String mMoreUrl;//下一页数据连接
    private NewsAdapter mNewsAdapter;

    public TabDetailPage(Activity activity, NewsMenu.NewsTabData newsTabData) {
        super(activity);
        mTabData = newsTabData;
        mUrl = Configs.SERVER_URL + mTabData.url;
    }

    @Override
    public View initView() {
        view = View.inflate(mActivity, R.layout.pager_tab_detail, null);
        mListView = (PullToRefreshListView) view.findViewById(R.id.lv_list);

        View mHeaderView = View.inflate(mActivity, R.layout.list_item_header, null);
        mViewPager = (TopNewsViewPager) mHeaderView.findViewById(R.id.vp_pic);
        mTextView = (TextView) mHeaderView.findViewById(R.id.tv_title);
        mIndicator = (CirclePageIndicator) mHeaderView.findViewById(R.id.indictaor);
        mListView.addHeaderView(mHeaderView);
        mListView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                getDataFromServer();
            }
            @Override
            public void onLoadMore() {
                if (mMoreUrl!=null) {
                    getMoreDataFromServer();
                }else{
                    Toast.makeText(mActivity, "没有更多数据", Toast.LENGTH_SHORT).show();
                    // 没有数据时也要收起控件
                    mListView.onRefreshComplete(true);
                }
            }
        });
        return view;
    }

    @Override
    public void initData() {
        String cache = CacheUtils.getCache(mActivity, mUrl);
        if (!TextUtils.isEmpty(cache)) {
            processData(cache, false);
        }
        getDataFromServer();//请求数据

    }

    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, mUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                processData(result, false);//解析JSON数据
                CacheUtils.setCache(mActivity, mUrl, result);//加入缓存
                // 收起下拉刷新控件
                mListView.onRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                // 收起下拉刷新控件
                mListView.onRefreshComplete(false);
                Toast.makeText(mActivity, "请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //加载更多数据
    private void getMoreDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                processData(result, true);//解析JSON数据
                // 收起下拉刷新控件
                mListView.onRefreshComplete(true);
            }
            @Override
            public void onFailure(HttpException error, String msg) {

                Toast.makeText(mActivity, "请求失败", Toast.LENGTH_SHORT).show();
                // 收起下拉刷新控件
                mListView.onRefreshComplete(false);
            }
        });

    }

    private void processData(String result, boolean isMore) {
        Gson gson = new Gson();
        newsTabBean = gson.fromJson(result, NewsTabBean.class);
        String moreUrl = newsTabBean.data.more;
        if (!TextUtils.isEmpty(moreUrl)) {
            mMoreUrl = Configs.SERVER_URL + moreUrl;
        } else {
            mMoreUrl = null;
        }
        if (!isMore) {//刷新数据
            mTopNewsDatas = newsTabBean.data.topnews;// 头条新闻填充数据
            if (mTopNewsDatas != null) {
                mViewPager.setAdapter(new TopNewsAdapter());
                mIndicator.setViewPager(mViewPager);
                mIndicator.setSnap(true);
                mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        NewsTabBean.TopNewsData newsData = mTopNewsDatas.get(position);
                        mTextView.setText(newsData.title);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                mTextView.setText(mTopNewsDatas.get(0).title);
                mIndicator.onPageSelected(0);

                // 新闻列表填充数据
                mNewsDatas = newsTabBean.data.news;
                if (mNewsDatas != null) {
                    mNewsAdapter = new NewsAdapter();
                    mListView.setAdapter(mNewsAdapter);
                }
            }
        } else {//加载更多
            ArrayList<NewsTabBean.NewsData> moreNews = newsTabBean.data.news;
            mNewsDatas.addAll(moreNews);// 将数据追加在原来的集合中
            //刷新ListView
            mNewsAdapter.notifyDataSetChanged();
        }


    }

    class TopNewsAdapter extends PagerAdapter {
        private BitmapUtils mBitmapUtils;

        public TopNewsAdapter() {
            mBitmapUtils = new BitmapUtils(mActivity);
            // 设置加载中的默认图片
            mBitmapUtils.configDefaultLoadingImage(R.drawable.topnews_item_default);
        }

        @Override
        public int getCount() {
            return mTopNewsDatas.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final ImageView view = new ImageView(mActivity);
            view.setScaleType(ImageView.ScaleType.FIT_XY);//填充父窗体
            String imageUrl = mTopNewsDatas.get(position).topimage;// 图片下载链接

            // 下载图片-将图片设置给imageview-避免内存溢出-缓存,BitmapUtils-XUtils
            mBitmapUtils.display(view, imageUrl);
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

    class NewsAdapter extends BaseAdapter {
        private BitmapUtils mBitmapUtils;

        public NewsAdapter() {
            mBitmapUtils = new BitmapUtils(mActivity);
            mBitmapUtils.configDefaultLoadingImage(R.drawable.news_pic_default);
        }

        @Override
        public int getCount() {
            return mNewsDatas.size();
        }

        @Override
        public NewsTabBean.NewsData getItem(int position) {
            return mNewsDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mActivity, R.layout.list_item_news, null);
                holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            NewsTabBean.NewsData newsData = getItem(position);
            holder.tvTitle.setText(newsData.title);
            holder.tvDate.setText(newsData.pubdate);
            mBitmapUtils.display(holder.ivIcon, newsData.listimage);
            return convertView;
        }
    }

    class ViewHolder {
        public ImageView ivIcon;
        public TextView tvTitle;
        public TextView tvDate;
    }

}
