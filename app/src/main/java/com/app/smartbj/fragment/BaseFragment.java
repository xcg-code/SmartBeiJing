package com.app.smartbj.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 14501_000 on 2016/9/12.
 */

public abstract class BaseFragment extends Fragment {

    public Activity mActivity;//这个activity就是MainActivity
    @Override
    // Fragment创建
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取fragment所在的Activity
        mActivity = getActivity();

    }

    @Nullable
    @Override
    // 初始化fragment的布局
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=initView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 初始化数据
        initData();
    }

    // 初始化布局, 必须由子类实现
    public abstract View initView() ;
    // 初始化数据, 必须由子类实现
    public abstract void initData() ;
}
