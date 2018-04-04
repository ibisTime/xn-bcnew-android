package com.cdkj.link_community.module.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.link_community.R;
import com.cdkj.link_community.databinding.ActivityShareBinding;
import com.cdkj.link_community.interfaces.QQUiListener;
import com.cdkj.link_community.utils.QqShareUtil;
import com.cdkj.link_community.utils.WxUtil;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.uuzuche.lib_zxing.decoding.Intents;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;


/**
 * 分享界面
 * Created by cdkj on 2017/8/1.
 */

public class ShareActivity extends Activity {

    private ActivityShareBinding mbinding;

    private String mShareUrl;//需要分享的URL
    private String mPhotoUrl;//需要分享的URL

    private String mTitle;//title
    private String mContent;//content

    private IUiListener QQUiListener;
    private UITipDialog tipDialog;


    /**
     * 打开当前页面
     *
     * @param context
     */
    public static void open(Context context, String shareUrl, String title, String content, String photoUrl) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, ShareActivity.class);
        intent.putExtra("shareUrl", shareUrl);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        intent.putExtra("photoUrl", photoUrl);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_share);

        if (getIntent() != null) {
            mShareUrl = getIntent().getStringExtra("shareUrl");
            mTitle = getIntent().getStringExtra("title");
            mContent = getIntent().getStringExtra("content");
            mPhotoUrl = getIntent().getStringExtra("photoUrl");
        }

        QQUiListener = new IUiListener() {
            @Override
            public void onComplete(Object o) {
                showDialog(0, getString(R.string.share_succ));
            }

            @Override
            public void onError(UiError uiError) {
                LogUtil.E("qq分享错误" + uiError.errorCode + uiError.errorMessage + uiError.errorDetail);
                showDialog(1, getString(R.string.share_fail));
            }

            @Override
            public void onCancel() {
                showDialog(2, getString(R.string.share_cancel));
            }
        };

        initListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        Tencent.onActivityResultData(requestCode, resultCode, data, QQUiListener);

        if (requestCode == Constants.REQUEST_API) {
            if (resultCode == Constants.REQUEST_QQ_SHARE ||
                    resultCode == Constants.REQUEST_QZONE_SHARE ||
                    resultCode == Constants.REQUEST_OLD_SHARE) {
                Tencent.handleResultData(data, QQUiListener);
            }
        }

    }

    /**
     * 初始化事件
     */
    private void initListener() {

        mbinding.txtCancel.setOnClickListener(v -> {
            finish();
        });

        mbinding.linShareToPyq.setOnClickListener(v -> {
            WxUtil.shareToPYQ(ShareActivity.this, mShareUrl,
                    mTitle, mContent);
            finish();
        });

        mbinding.linShareToWx.setOnClickListener(v -> {
            WxUtil.shareToWX(ShareActivity.this, mShareUrl,
                    mTitle, mContent);
            finish();
        });

        mbinding.linShareToQq.setOnClickListener(view -> {
            if (ImgUtils.isHaveHttp(mPhotoUrl)) {
                QqShareUtil.shareMsg(this, mTitle, mContent, mShareUrl, mPhotoUrl, QQUiListener);
            } else {
                QqShareUtil.shareMsg(this, mTitle, mContent, mShareUrl, MyCdConfig.QINIUURL + mPhotoUrl, QQUiListener);
            }

            mbinding.layoutShare.setVisibility(View.GONE);
        });

    }

    public void showDialog(int type, String info) {

        if (type == 0) {
            tipDialog = new UITipDialog.Builder(this)
                    .setIconType(UITipDialog.Builder.ICON_TYPE_SUCCESS)
                    .setTipWord(info)
                    .create();
            tipDialog.show();
        } else if (type == 1) {
            tipDialog = new UITipDialog.Builder(this)
                    .setIconType(UITipDialog.Builder.ICON_TYPE_FAIL)
                    .setTipWord(info)
                    .create();

        } else {
            tipDialog = new UITipDialog.Builder(this)
                    .setIconType(UITipDialog.Builder.ICON_TYPE_INFO)
                    .setTipWord(info)
                    .create();
        }

        tipDialog.show();

        Observable.timer(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        tipDialog.dismiss();
                        finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        tipDialog.dismiss();
                        finish();
                    }
                });
    }

}
