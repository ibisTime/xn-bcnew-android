package com.cdkj.link_community.module.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.cdkj.link_community.R;
import com.cdkj.link_community.databinding.ActivityShareBinding;
import com.cdkj.link_community.interfaces.QQUiListener;
import com.cdkj.link_community.utils.QqShareUtil;
import com.cdkj.link_community.utils.WxUtil;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.Tencent;
import com.uuzuche.lib_zxing.decoding.Intents;


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

        initListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, new QQUiListener());
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
            QqShareUtil.shareMsg(this, mTitle, mContent, mShareUrl, mPhotoUrl);
        });

    }


}
