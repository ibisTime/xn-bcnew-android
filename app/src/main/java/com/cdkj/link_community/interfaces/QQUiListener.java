package com.cdkj.link_community.interfaces;

import android.app.Activity;

import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.link_community.R;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

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
        LogUtil.E("qq分享" + e.errorMessage);
        UITipDialog.showFail(mActivity, mActivity.getString(R.string.share_fail) + e.errorMessage);
    }

    @Override
    public void onCancel() {
        if (mActivity == null) return;
        UITipDialog.showFail(mActivity, mActivity.getString(R.string.share_cancel));
    }

    public void onDestroy() {
        mS.clear();
        mActivity = null;
    }

}
