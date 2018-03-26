package com.cdkj.link_community.module.message;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.model.IntroductionInfoModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.databinding.ActivityFastMessageShareBinding;
import com.cdkj.link_community.model.FastMessage;
import com.cdkj.link_community.utils.WxUtil;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;

/**
 * 快讯分享
 * Created by cdkj on 2018/3/19.
 */

public class FastMessageToShareActivity extends AbsBaseLoadActivity {

    private ActivityFastMessageShareBinding mBinding;

    /**
     * @param context
     * @param message
     */
    public static void open(Context context, FastMessage message) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, FastMessageToShareActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, message);
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

        getShareUrl();

        if (getIntent() != null) {
            FastMessage fastMessage = getIntent().getParcelableExtra(CdRouteHelper.DATASIGN);

            if (fastMessage == null) return;

            SpannableStringBuilder span = new SpannableStringBuilder("缩" + "【" + getString(R.string.fast_msg) + "】 " + fastMessage.getContent());
            span.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 0, 1,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            mBinding.tvMsg.setText(span);

            if (!TextUtils.isEmpty(fastMessage.getShowDatetime())) {
                mBinding.tvTime.setText(DateUtil.getWeekOfDate(new Date(fastMessage.getShowDatetime())) + " " + DateUtil.formatStringData(fastMessage.getShowDatetime(), DateUtil.DATE_YYMMddHHmm));
            }

        }

        ininListener();

    }

    private void ininListener() {

        //结束当前界面
        mBinding.fraToFinish.setOnClickListener(view -> finish());


        mBinding.imgWx.setOnClickListener(view -> {
            mBinding.scrollView.post(() -> {
                WxUtil.shareBitmapToWX(FastMessageToShareActivity.this, getBitmapByView(mBinding.scrollView));
            });
        });

        mBinding.imgPyq.setOnClickListener(view -> {
            mBinding.scrollView.post(() -> {
                WxUtil.shareBitmapToWXPYQ(FastMessageToShareActivity.this, getBitmapByView(mBinding.scrollView));
            });
        });
    }

    public void getShareUrl() {

        Map<String, String> map = new HashMap<>();
        map.put("ckey", "h5DownUrl");
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("companyCode", MyCdConfig.COMPANYCODE);

        Call call = RetrofitUtils.getBaseAPiService().getKeySystemInfo("628917", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IntroductionInfoModel>(this) {
            @Override
            protected void onSuccess(IntroductionInfoModel data, String SucMessage) {

                if (TextUtils.isEmpty(data.getCvalue())) {
                    return;
                }
                createCodePhoto(data.getCvalue());
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    /**
     * 生成二维码图片
     *
     * @param url
     */
    public void createCodePhoto(String url) {
        mSubscription.add(Observable.just(url).map(s -> CodeUtils.createImage(s, 300, 300, null))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .filter(bitmap -> bitmap != null)
                .map(bitmap -> {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] bytes = baos.toByteArray();
                    return bytes;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bytes -> {
                    Glide.with(this)
                            .load(bytes)
                            .error(R.drawable.default_pic)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(mBinding.imgUrl);

                }, Throwable::printStackTrace));

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
                Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }

}
