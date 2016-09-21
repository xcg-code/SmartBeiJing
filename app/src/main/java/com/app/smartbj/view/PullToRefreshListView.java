package com.app.smartbj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.smartbj.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 14501_000 on 2016/9/19.
 */

public class PullToRefreshListView extends ListView {
    private TextView tvTitle;
    private TextView tvTime;
    private ImageView ivArrow;
    private ProgressBar pb;

    private View mHeaderView;
    private int mHeaderViewHeight;
    private int startY;

    RotateAnimation animUp;

    private static final int STATE_PULL_TO_REFRESH = 1;
    private static final int STATE_RELEASE_TO_REFRESH = 2;
    private static final int STATE_REFRESHING = 3;

    private int mCurrentState = STATE_PULL_TO_REFRESH;// 当前刷新状态
    private RotateAnimation animDown;
    private View footView;
    private int footViewHight;
    private boolean isLoadMore=false;//是否在加载更多

    public PullToRefreshListView(Context context) {
        super(context);
        initHeaderView();
        initFooterView();
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
        initFooterView();
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
        initFooterView();
    }

    private void initHeaderView() {
        mHeaderView = View.inflate(getContext(), R.layout.pull_to_refresh, null);
        tvTitle = (TextView) mHeaderView.findViewById(R.id.tv_title);
        tvTime = (TextView) mHeaderView.findViewById(R.id.tv_time);
        ivArrow = (ImageView) mHeaderView.findViewById(R.id.iv_arrow);
        pb = (ProgressBar) mHeaderView.findViewById(R.id.pb_loading);
        this.addHeaderView(mHeaderView);//添加头布局

        //隐藏头布局
        mHeaderView.measure(0, 0);
        mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
        initAnim();
        setCurrentTime();
    }

    //设置尾布局
    private void initFooterView() {
        footView = View.inflate(getContext(), R.layout.list_item_foot, null);
        this.addFooterView(footView);

        //隐藏尾布局
        footView.measure(0, 0);
        footViewHight = footView.getMeasuredHeight();
        footView.setPadding(0,-footViewHight,0, 0);
        this.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {// 空闲状态
                    int lastVisiblePosition = getLastVisiblePosition();
                    if (lastVisiblePosition == (getCount() - 1)&& !isLoadMore) {// 当前显示的是最后一个item并且没有正在加载更多
                        // 到底了
                        isLoadMore = true;
                        footView.setPadding(0, 0, 0, 0);// 显示加载更多的布局
                        setSelection(getCount() - 1);// 将listview显示在最后一个item上,从而加载更多会直接展示出来, 无需手动滑动
                        //通知主界面加载下一页数据
                        if (mListener != null) {
                            mListener.onLoadMore();
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    // 设置刷新时间
    private void setCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(new Date());
        tvTime.setText(time);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                // 当用户按住头条新闻的viewpager进行下拉时,ACTION_DOWN会被viewpager消费掉,
                // 导致startY没有赋值,此处需要重新获取一下
                if (startY == -1) {
                    startY = (int) ev.getY();
                }
                if (mCurrentState == STATE_REFRESHING) break;//正处于刷新状态

                int endY = (int) ev.getY();
                int dy = endY - startY;
                if (dy > 0 && getFirstVisiblePosition() == 0) {//向下滑，下拉刷新
                    int padding = dy - mHeaderViewHeight;
                    mHeaderView.setPadding(0, padding, 0, 0);
                    if (padding > 0 && mCurrentState != STATE_RELEASE_TO_REFRESH) {
                        // 改为松开刷新
                        mCurrentState = STATE_RELEASE_TO_REFRESH;
                        refreshState();
                    } else if (padding < 0 && mCurrentState != STATE_PULL_TO_REFRESH) {
                        // 改为下拉刷新
                        mCurrentState = STATE_PULL_TO_REFRESH;
                        refreshState();
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                startY = -1;
                if (mCurrentState == STATE_RELEASE_TO_REFRESH) {
                    mCurrentState = STATE_REFRESHING;
                    refreshState();
                    // 完整展示头布局
                    mHeaderView.setPadding(0, 0, 0, 0);
                    // 4. 进行回调
                    if (mListener != null) {
                        mListener.onRefresh();
                    }
                } else if (mCurrentState == STATE_PULL_TO_REFRESH) {
                    // 隐藏头布局
                    mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 初始化箭头动画
     */
    private void initAnim() {
        animUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animUp.setDuration(200);
        animUp.setFillAfter(true);

        animDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animDown.setDuration(200);
        animDown.setFillAfter(true);
    }

    private void refreshState() {
        switch (mCurrentState) {
            case STATE_PULL_TO_REFRESH:
                tvTitle.setText("下拉刷新");
                pb.setVisibility(View.INVISIBLE);
                ivArrow.setVisibility(View.VISIBLE);
                ivArrow.startAnimation(animDown);
                break;
            case STATE_RELEASE_TO_REFRESH:
                tvTitle.setText("松开刷新");
                pb.setVisibility(View.INVISIBLE);
                ivArrow.setVisibility(View.VISIBLE);
                ivArrow.startAnimation(animUp);
                break;
            case STATE_REFRESHING:
                tvTitle.setText("正在刷新...");
                ivArrow.clearAnimation();// 清除箭头动画,否则无法隐藏
                pb.setVisibility(View.VISIBLE);
                ivArrow.setVisibility(View.INVISIBLE);
                break;

            default:
                break;
        }
    }

    /**
     * 刷新结束,收起控件
     */
    public void onRefreshComplete(boolean success) {
        if(!isLoadMore){
            mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
            mCurrentState = STATE_PULL_TO_REFRESH;
            tvTitle.setText("下拉刷新");
            pb.setVisibility(View.INVISIBLE);
            ivArrow.setVisibility(View.VISIBLE);
            if (success) {// 只有刷新成功之后才更新时间
                setCurrentTime();
            }
        }else{
            footView.setPadding(0,-footViewHight,0,0);
            isLoadMore=false;
        }


    }

    // 3. 定义成员变量,接收监听对象
    private OnRefreshListener mListener;

    /**
     * 2. 暴露接口,设置监听
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    /**
     * 1. 下拉刷新的回调接口
     *
     * @author Kevin
     * @date 2015-10-21
     */
    public interface OnRefreshListener {
        void onRefresh();
        void onLoadMore();
    }
}
