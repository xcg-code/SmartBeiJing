package com.app.smartbj.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.app.smartbj.MainActivity;
import com.app.smartbj.R;
import com.app.smartbj.domain.NewsMenu;
import com.app.smartbj.page.NewsPage;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

/**
 * Created by 14501_000 on 2016/9/12.
 */

public class LeftMenuFragment extends BaseFragment {
    private ListView mListView;
    private ArrayList<NewsMenu.NewsMenuData> mNewsMenuData;
    private int mCurrentPos;// 当前被选中的item的位置
    private MenuDataAdapter mAdapter;
    @Override
    public View initView() {
        View view=View.inflate(mActivity, R.layout.fragment_left_menu,null);
        mListView= (ListView) view.findViewById(R.id.lv_list);
        return view;
    }

    @Override
    public void initData() {

    }

    /**
     * 为侧边栏设置数据
     * @param data
     */
    public void setMenuData(ArrayList<NewsMenu.NewsMenuData> data) {
        mCurrentPos=0;//当前选中的位置归零
        mNewsMenuData=data;
        mAdapter=new MenuDataAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentPos=position;
                mAdapter.notifyDataSetChanged();// 刷新listview
                // 收起侧边栏
                toggle();

                // 侧边栏点击之后, 要修改新闻中心的FrameLayout中的内容
                setCurrentDetailPager(position);
            }
        });
    }

    private void setCurrentDetailPager(int position) {
        // 获取新闻中心的对象
        MainActivity mainUI = (MainActivity) mActivity;
        // 获取ContentFragment
        ContentFragment fragment = mainUI.getContentFragment();
        // 获取NewsPage
        NewsPage newsPage=fragment.getNewsPage();
        // 修改新闻中心的FrameLayout的布局
        newsPage.setCurrentDetailPage(position);
    }

    protected void toggle() {
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        slidingMenu.toggle();// 如果当前状态是开, 调用后就关; 反之亦然
    }

    class MenuDataAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mNewsMenuData.size();
        }

        @Override
        public NewsMenu.NewsMenuData getItem(int position) {
            return mNewsMenuData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=View.inflate(mActivity,R.layout.left_menu_item,null);
            TextView title= (TextView) view.findViewById(R.id.tv_menu);
            NewsMenu.NewsMenuData item=getItem(position);
            title.setText(item.title);

            if (position == mCurrentPos) {
                // 被选中
                title.setEnabled(true);// 文字变为红色
            } else {
                // 未选中
                title.setEnabled(false);// 文字变为白色
            }
            return view;
        }
    }
}
