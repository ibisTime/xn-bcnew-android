package com.cdkj.link_community.module.message;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.link_community.R;
import com.cdkj.link_community.databinding.ActivityFastMessageShareBinding;

/**
 * 快讯分享
 * Created by cdkj on 2018/3/19.
 */

public class FastMessageToShareActivity extends AbsBaseLoadActivity {

    private ActivityFastMessageShareBinding mBinding;
    private boolean is;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, FastMessageToShareActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_fast_message_share, null, false);
        return mBinding.getRoot();
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        ininListener();

    }

    private void ininListener() {

        //结束当前界面
        mBinding.fraToFinish.setOnClickListener(view -> finish());

        is = true;
        mBinding.scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!is) {
                    return;
                }
                is = !is;
                mBinding.img.setImageBitmap(getBitmapByView(mBinding.scrollView));
            }
        });

    }


    /**
     * 截取scrollview的生产bitmap
     *
     * @param scrollView
     * @return
     */
    public Bitmap getBitmapByView(ScrollView scrollView) {
        int h = 0;
        Bitmap bitmap = null;
        // 获取scrollview实际高度
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
            scrollView.getChildAt(i).setBackgroundColor(
                    Color.parseColor("#ffffff"));
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
                Bitmap.Config.ARGB_4444);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }

}
