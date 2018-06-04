package com.cdkj.link_community;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.cdkj.baselibrary.CdApplication;
import com.cdkj.baselibrary.utils.LogUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

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

        initUM();
        initJPush();

        CdApplication.initialize(this, BuildConfig.LOG_DEBUG);
    }

    /**
     * 初始化极光推送
     */
    private void initUM() {

        UMConfigure.init(this, "5acd7605f29d985d370000d3", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, null);

        UMConfigure.setLogEnabled(LogUtil.isDeBug);
        UMConfigure.setEncryptEnabled(true);

        // 场景类型设置接口
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
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

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
