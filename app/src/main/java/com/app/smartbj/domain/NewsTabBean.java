package com.app.smartbj.domain;

import java.util.ArrayList;

/**
 * 页签详情数据对象
 * Created by 14501_000 on 2016/9/18.
 */

public class NewsTabBean {
    public TabData data;
    public class TabData{
        public String more;
        public ArrayList<NewsData> news;
        public ArrayList<TopNewsData> topnews;
        public int retcode;
    }

    public class NewsData{
        public int id;
        public String listimage;
        public String pubdate;
        public String title;
        public String type;
        public String url;
    }
    public class TopNewsData{
        public int id;
        public String pubdate;
        public String title;
        public String topimage;
        public String type;
        public String url;

    }
}
