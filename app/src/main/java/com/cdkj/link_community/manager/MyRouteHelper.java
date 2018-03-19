package com.cdkj.link_community.manager;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * 路由管理
 * Created by cdkj on 2018/3/12.
 */

public class MyRouteHelper {

    //跳转到登录页面
    public static final String APPMAIN = "/app/main";

    /**
     * 打开主页
     */
    public static void openMain() {
        ARouter.getInstance().build(APPMAIN)
                .greenChannel()
                .navigation();
    }

}
