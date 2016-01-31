package com.oceancx.androidlib.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 缓存帮助类
 * 1. 缓存什么要知道
 * 2. 读取什么要知道
 * Created by oceancx on 15/11/9.
 */
public class CacheHelper {
    private SharedPreferences preferences;
    private static CacheHelper CACHE;
    public static final String FIRSTE_RECTVIEW="firstRecView";



    public CacheHelper(Context context) {
        preferences = context.getSharedPreferences("Cache", Context.MODE_PRIVATE);
    }

    public static CacheHelper getInstance(Context context) {
        if (CACHE == null)
            CACHE = new CacheHelper(context);
        return CACHE;
    }

    public boolean writeCache(String key, String text) {
        return preferences.edit().putString(key, text).commit();

    }

    public String readCache(String key) {

        return preferences.getString(key, null);
    }
}
