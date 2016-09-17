package com.app.smartbj.utils;

import android.content.Context;

/**
 * 网络缓存工具
 * Created by 14501_000 on 2016/9/14.
 */

public class CacheUtils {
    /**
     * 以url为key, 以json为value,保存在本地
     * @param url
     * @param jsonData
     * @param context
     */
    public static void setCache(Context context,String url, String jsonData){
        //以文件存储缓存： 以MD5(url)为文件名, 以json为文件内容
        PrefUtils.setString(context,url,jsonData);
    }

    /**
     * 获取缓存
     * @param context
     * @param url
     * @return
     */
    public static String getCache(Context context,String url){
        //文件缓存: 查找有没有一个文件叫做MD5(url)的, 有的话,说明有缓存
        return PrefUtils.getString(context,url,null);
    }
}
