package com.app.smartbj.page.NewsMenu;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.smartbj.Configs;
import com.app.smartbj.R;
import com.app.smartbj.domain.PhotoBean;
import com.app.smartbj.page.BasePageMenuDetail;
import com.app.smartbj.utils.CacheUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

/**
 * 新闻菜单-组图
 * Created by 14501_000 on 2016/9/16.
 */

public class PhotoMenuDetailPage extends BasePageMenuDetail {

    private ListView listView;
    private GridView gridView;
    private ArrayList<PhotoBean.PhotoNews> photoNewsList;
    private ImageButton btn_photo;
    private boolean isListView = true;// 标记当前是否是listview展示
    public PhotoMenuDetailPage(Activity activity, ImageButton imageButton) {
        super(activity);
        btn_photo=imageButton;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_photos_menu_detail,null);
        listView = (ListView) view.findViewById(R.id.lv_photo);
        gridView = (GridView) view.findViewById(R.id.gv_photo);
        return view;
    }

    @Override
    public void initData() {
        String cache = CacheUtils.getCache(mActivity,Configs.PHOTOS_URL);
        if (!TextUtils.isEmpty(cache)) {
            processData(cache);
        }
        getDataFromServer();
    }
    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, Configs.PHOTOS_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                processData(result);//解析JSON数据
                CacheUtils.setCache(mActivity,Configs.PHOTOS_URL,result);
            }
            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
            }
        });
        btn_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isListView){
                    listView.setVisibility(View.VISIBLE);
                    gridView.setVisibility(View.INVISIBLE);
                    isListView=!isListView;
                }else{
                    gridView.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.INVISIBLE);
                    isListView=!isListView;
                }
            }
        });
    }
    protected void processData(String result) {
        Gson gson = new Gson();
        PhotoBean photosBean = gson.fromJson(result, PhotoBean.class);
        photoNewsList = photosBean.data.news;

        listView.setAdapter(new PhotoAdapter());
        gridView.setAdapter(new PhotoAdapter());// gridview的布局结构和listview完全一致,
        // 所以可以共用一个adapter
    }

    class PhotoAdapter extends BaseAdapter{

        private BitmapUtils mBitmapUtils;

        public PhotoAdapter() {
            mBitmapUtils = new BitmapUtils(mActivity);
            mBitmapUtils.configDefaultLoadingImage(R.drawable.topnews_item_default);
        }
        @Override
        public int getCount() {
            return photoNewsList.size();
        }

        @Override
        public Object getItem(int position) {
            return photoNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView==null){
                holder=new ViewHolder();
                convertView = View.inflate(mActivity,R.layout.list_item_photos, null);
                holder.iv_Pic= (ImageView) convertView.findViewById(R.id.iv_pic);
                holder.tv_Title= (TextView) convertView.findViewById(R.id.tv_title);
                convertView.setTag(holder);
            }else{
                holder= (ViewHolder) convertView.getTag();
            }
            PhotoBean.PhotoNews item = (PhotoBean.PhotoNews) getItem(position);
            holder.tv_Title.setText(item.title);
            mBitmapUtils.display(holder.iv_Pic,item.listimage);
            return convertView;
        }
    }
    static class ViewHolder {
        public ImageView iv_Pic;
        public TextView tv_Title;
    }
}
