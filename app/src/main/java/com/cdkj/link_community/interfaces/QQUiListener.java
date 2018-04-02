package com.cdkj.link_community.interfaces;

import android.app.Activity;
import android.content.Context;

import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.link_community.BaseApplication;
import com.cdkj.link_community.R;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import java.lang.ref.SoftReference;

/**
 * Created by cdkj on 2018/3/27.
 */

public class QQUiListener implements IUiListener {

    private Activity mActivity;
    private final SoftReference<Activity> mS;

    public QQUiListener(Activity mActivity) {
        mS = new SoftReference<>(mActivity);
        this.mActivity = mS.get();
    }

    @Override
    public void onComplete(Object o) {
        if (mActivity == null) return;
        UITipDialog.showSuccess(mActivity, mActivity.getString(R.string.share_succ));
    }

    @Override
    public void onError(UiError e) {
        if (mActivity == null) return;
        UITipDialog.showFall(mActivity, mActivity.getString(R.string.share_fail));
    }

    @Override
    public void onCancel() {
        if (mActivity == null) return;
        UITipDialog.showFall(mActivity,  mActivity.getString(R.string.share_cancel));
    }

    public void onDestroy() {
        mS.clear();
        mActivity = null;
    }

}
