package com.app.smartbj.domain;

import java.util.ArrayList;

/**
 * Created by 14501_000 on 2016/9/26.
 */

public class PhotoBean {
    public PhotosData data;

    public class PhotosData {
        public ArrayList<PhotoNews> news;
    }
    public class PhotoNews{
        public int id;
        public String listimage;
        public String title;
    }
}
