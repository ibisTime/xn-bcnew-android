package com.cdkj.link_community.interfaces;

import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.link_community.BaseApplication;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

/**
 * Created by cdkj on 2018/3/27.
 */

public class QQUiListener implements IUiListener {

    @Override
    public void onComplete(Object o) {
        ToastUtil.show(BaseApplication.getInstance(), "分享成功");
    }

    @Override
    public void onError(UiError e) {
    }

    @Override
    public void onCancel() {
    }

}
