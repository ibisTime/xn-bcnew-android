package com.cdkj.link_community.module.message;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.model.IntroductionInfoModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.BitmapUtils;
import com.cdkj.baselibrary.utils.CameraHelper;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.PermissionHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.databinding.ActivityFastMessageShareBinding;
import com.cdkj.link_community.interfaces.QQUiListener;
import com.cdkj.link_community.model.FastMessage;
import com.cdkj.link_community.utils.QqShareUtil;
import com.cdkj.link_community.utils.WxUtil;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;

import static com.cdkj.baselibrary.utils.BitmapUtils.getBitmapByView;

/**
 * 快讯分享
 * Created by cdkj on 2018/3/19.
 */

public class FastMessageToShareActivity extends AbsBaseLoadActivity {

    private ActivityFastMessageShareBinding mBinding;

    private PermissionHelper mPreHelper;//权限请求

    private String mQQSharePhotoPath;//qq图片分享路径

    //需要的权限
    private String[] needLocationPermissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    private QQUiListener QQUiListener;

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

        QQUiListener = new QQUiListener(this);

        if (getIntent() != null) {
            FastMessage fastMessage = getIntent().getParcelableExtra(CdRouteHelper.DATASIGN);

            if (fastMessage == null) return;

            SpannableStringBuilder span = new SpannableStringBuilder("缩" + "【" + getString(R.string.fast_msg) + "】 " + Html.fromHtml(fastMessage.getContent()));
            span.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 0, 1,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            mBinding.tvMsg.setText(span);

            if (!TextUtils.isEmpty(fastMessage.getShowDatetime())) {
                mBinding.tvTime.setText(DateUtil.getWeekOfDate(new Date(fastMessage.getShowDatetime())) + " " + DateUtil.formatStringData(fastMessage.getShowDatetime(), DateUtil.DEFAULT_DATE_FMT));
            }

        }

        ininListener();

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

    private void ininListener() {

        //结束当前界面
        mBinding.fraToFinish.setOnClickListener(view -> finish());

        mBinding.scrollView.post(() -> {

            mBinding.imgWx.setOnClickListener(view -> {
                WxUtil.shareBitmapToWX(FastMessageToShareActivity.this, getBitmapByView(mBinding.scrollView));
            });

            mBinding.imgPyq.setOnClickListener(view -> {
                WxUtil.shareBitmapToWXPYQ(FastMessageToShareActivity.this, getBitmapByView(mBinding.scrollView));
            });


            mBinding.imgQq.setOnClickListener(view -> {

                if (CameraHelper.isNeedRequestPremission()) {
                    requestPermissions();
                    return;
                }

                saveBitmapAndShare();
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


    public void saveBitmapAndShare() {

        if (mQQSharePhotoPath != null) {
            QqShareUtil.shareLocaPhoto(FastMessageToShareActivity.this, mQQSharePhotoPath, QQUiListener);
            return;
        }

        mSubscription.add(Observable.just(getBitmapByView(mBinding.scrollView), TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .observeOn(Schedulers.newThread())  //创建
                .map(o -> BitmapUtils.saveBitmapFile((Bitmap) o, "share_qq"))
                .observeOn(AndroidSchedulers.mainThread())
                .filter(s -> !TextUtils.isEmpty(s))
                .subscribe(path -> {
                    mQQSharePhotoPath = path;
                    QqShareUtil.shareLocaPhoto(FastMessageToShareActivity.this, path, QQUiListener);
                }, throwable -> {
                    LogUtil.E(throwable.toString());
                }));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (mPreHelper != null) {
            mPreHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * 请求权限
     */
    private void requestPermissions() {

        if (mPreHelper == null) {
            mPreHelper = new PermissionHelper(this);
        }

        mPreHelper.requestPermissions(new PermissionHelper.PermissionListener() {
            @Override
            public void doAfterGrand(String... permission) {
                saveBitmapAndShare();
            }

            @Override
            public void doAfterDenied(String... permission) {
                showSureDialog("没有文件权限，无法分享快讯。", view -> {

                });
            }

        }, needLocationPermissions);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (QQUiListener != null) {
            QQUiListener.onDestroy();
        }
    }
}
