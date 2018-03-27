package com.cdkj.link_community.utils;

import android.app.Activity;
import android.os.Bundle;

import com.cdkj.link_community.interfaces.QQUiListener;
import com.cdkj.link_community.module.user.ShareActivity;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.Tencent;

/**
 * QQ分享工具类
 * Created by cdkj on 2018/3/27.
 */

public class QqShareUtil {

    public static final String QQAPPID = "1106791506";

    /**
     * 分享本地图片
     *
     * @param act
     * @param photoPath
     */
    public static void shareLocaPhoto(Activity act, String photoPath) {
        Bundle params = new Bundle();
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, photoPath);
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        Tencent.createInstance(QQAPPID, act.getApplicationContext()).shareToQQ(act, params, new QQUiListener());
    }


    /**
     * 分享消息
     * @param act
     * @param title
     * @param content
     * @param openUrl
     * @param photoUrl
     */
    public static void shareMsg(Activity act, String title, String content, String openUrl, String photoUrl) {

        if (act == null) return;

        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, content);

        /*点击URL*/
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, openUrl);

        /*图片路径*/
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, photoUrl);

        Tencent.createInstance(QQAPPID, act.getApplicationContext()).shareToQQ(act, params, new QQUiListener());
    }


}
