package com.cdkj.link_community;

import android.app.Application;
import android.content.Context;

import com.cdkj.baselibrary.CdApplication;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by cdkj on 2018/1/31.
 */

public class BaseApplication extends Application {

    public static Context instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initJPush();
        CdApplication.initialize(this, BuildConfig.LOG_DEBUG);
    }

    /**
     * 初始化极光推送
     */
    private void initJPush() {
        JPushInterface.setDebugMode(BuildConfig.LOG_DEBUG);
        JPushInterface.init(this);
    }

    public static Context getInstance() {
        return instance;
    }
}
