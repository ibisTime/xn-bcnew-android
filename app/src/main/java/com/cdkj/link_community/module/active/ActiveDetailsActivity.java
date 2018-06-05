package com.cdkj.link_community.module.active;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.IntroductionInfoModel;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.databinding.ActivityActiveDetailsBinding;
import com.cdkj.link_community.model.ActiveModel;
import com.cdkj.link_community.module.user.ShareActivity;
import com.cdkj.link_community.utils.DeviceUtils;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.baselibrary.appmanager.CdRouteHelper.DATA_SIGN;

/**
 * Created by cdkj on 2018/4/29.
 */

public class ActiveDetailsActivity extends AbsBaseLoadActivity {

    private String code;
    private ActiveModel model;

    private ActivityActiveDetailsBinding mBinding;

    private String mSharePhotoUrl;//要分分享的图片url
    private String mShareContent;//要分享的内容

    /**
     * @param context
     */
    public static void open(Context context, String code) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, ActiveDetailsActivity.class);
        intent.putExtra(DATA_SIGN, code);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_active_details, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        if (getIntent() == null)
            return;

        code = getIntent().getStringExtra(DATA_SIGN);

        mBaseBinding.viewV.setVisibility(View.GONE);

    }

    @Override
    protected void onResume() {
        super.onResume();

        getActive();
    }

    /**
     * 资讯详情
     */
    private void getActive() {

        if (TextUtils.isEmpty(code)) {
            return;
        }

        Map<String, String> map = RetrofitUtils.getRequestMap();

        map.put("code", code);
        map.put("userId", SPUtilHelper.getUserId());

        showLoadingDialog();
        Call call = RetrofitUtils.createApi(MyApiServer.class).getActive("628508", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<ActiveModel>(this) {
            @Override
            protected void onSuccess(ActiveModel activeModel, String SucMessage) {
                if (activeModel != null){
                    model = activeModel;

                    setView();
                    initListener();
                    toReadActiveRequest();
                }

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    private void setView() {
        mBaseBinding.titleView.setMidTitle("活动详情");
        mBaseBinding.titleView.setRightImg(R.drawable.active_share);
        mBaseBinding.titleView.setRightFraClickListener(view -> {
            getUrlToShare();
        });

        mSharePhotoUrl = StringUtils.getAsPicListIndexOne(model.getAdvPic());
        mShareContent = subShareContent(StringUtils.delHTMLTag(model.getContent()));

        ImgUtils.loadImg(this, model.getAdvPic(), mBinding.ivAdv);

        mBinding.tvTitle.setText(model.getTitle());

//        if (model.getPrice() == 0){
//            mBinding.tvPrice.setText("免费");
//        }else {
//            mBinding.tvPrice.setText(MONEY_SIGN + AccountUtil.moneyFormat(model.getPrice()));
//        }
        mBinding.tvPrice.setText(model.getPrice());

        mBinding.tvDateTime.setText(DateUtil.formatStringData(model.getStartDatetime(), DateUtil.ACTIVE_DATE_FMT)
                + "-" + DateUtil.formatStringData(model.getEndDatetime(), DateUtil.ACTIVE_DATE_FMT));

        mBinding.tvLocation.setText(model.getAddress());
        mBinding.tvBrowse.setText(model.getReadCount()+"");
        mBinding.tvMobile.setText(model.getContactMobile());

        mBinding.tvApproveUser.setText("已报名用户("+model.getEnrollCount()+")/已通过("+model.getApproveCount()+")");
        mBinding.llApproveUser.setVisibility(View.VISIBLE);
        mBinding.lineApproveUser.setVisibility(View.VISIBLE);



        if (model.getApprovedList()!= null){

            mBinding.flApproveUser.removeAllViews();

            // 此处需使用LinearLayout的LayoutParams
            ImageView iv;
            FrameLayout.LayoutParams params;

            int limit = model.getApprovedList().size() > 10 ? 10 : model.getApprovedList().size();

            for (int i = 0; i < limit; i++){

                iv = new ImageView(ActiveDetailsActivity.this);
                params = new FrameLayout.LayoutParams(DeviceUtils.dip2px(this, 40f), FrameLayout.LayoutParams.MATCH_PARENT);

                if (i != 0){
                    params.setMargins(DeviceUtils.dip2px(this, i*25f),0,0,0);
                }
                mBinding.flApproveUser.addView(iv,params);

                ImgUtils.loadQiniuLogo(this, model.getApprovedList().get(i).getPhoto(), iv);
            }

        }

        mBinding.wvContent.loadData("<style>\n" +           //设置图片自适应
                "img{\n" +
                " max-width:100%;\n" +
                " height:auto;\n" +
                "}\n" +
                "</style>" + model.getContent(), "text/html; charset=UTF-8", "utf-8");

        // UN_ENROLL("0", "未报名"), ENROLL_APPLY("1", "申请报名中"), ENROLL_SUC("2", "报名成功");
        if (model.getIsEnroll().equals("0")){
            mBinding.tvBtn.setText("立即报名");
        }else if (model.getIsEnroll().equals("1")){
            mBinding.tvBtn.setText("报名申请中");
            mBinding.tvBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.bg_gray_e5e5e5));
        }else {
            mBinding.tvBtn.setText("已通过请注意查看短信");
        }

        /*评论数量*/
        if (model.getCommentCount() > 999) {
            mBinding.tvCommentNum.setText("999+");
        } else {
            mBinding.tvCommentNum.setText(model.getCommentCount() + "");
        }

        /*收藏按钮*/
        if (TextUtils.equals(model.getIsCollect(), "0")) {
            mBinding.imgCollection.setImageResource(R.drawable.callection_un);
        } else {
            mBinding.imgCollection.setImageResource(R.drawable.user_collection);
        }
    }

    private void initListener() {

        mBinding.tvBtn.setOnClickListener(view -> {
            if (!SPUtilHelper.isLogin(this, false)) {
                return;
            }

            if (model.getIsEnroll().equals("0"))
                ActiveApproveActivity.open(this, model.getCode());

        });

        mBinding.llMap.setOnClickListener(view -> {

            ActiveMapActivity.open(this, model.getLatitude() ,model.getLongitude());

        });

        mBinding.llApproveUser.setOnClickListener(view -> {
            ActiveApproveUserActivity.open(this, model.getCode());
        });

        mBinding.llComment.setOnClickListener(view -> {
            if (!SPUtilHelper.isLogin(this, false)) {
                return;
            }
            ActiveCommentActivity.open(this, model.getCode());

        });

        mBinding.llMobile.setOnClickListener(view -> {
            CallPhoneActivity.open(this, model.getContactMobile());
        });

        //收藏
        mBinding.llLike.setOnClickListener(view -> {
            if (!SPUtilHelper.isLogin(this, false)) {
                return;
            }
            toMsgCollectionRequest();

        });

    }


    /**
     * 资讯收藏
     */
    private void toMsgCollectionRequest() {

        if (TextUtils.isEmpty(model.getCode())) {
            return;
        }

        Map<String, String> map = RetrofitUtils.getRequestMap();

        map.put("code", model.getCode());

        showLoadingDialog();
        Call call = RetrofitUtils.getBaseAPiService().successRequest("628512", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {

                if (data.isSuccess()) {
                    if (model != null) {
                        if (TextUtils.equals(model.getIsCollect(), "0")) {
                            UITipDialog.showSuccess(ActiveDetailsActivity.this, getString(R.string.collect_succ));
                            model.setIsCollect("1");
                            mBinding.imgCollection.setImageResource(R.drawable.user_collection);
                        } else {
                            UITipDialog.showSuccess(ActiveDetailsActivity.this, getString(R.string.collect_cancel_succ));
                            model.setIsCollect("0");
                            mBinding.imgCollection.setImageResource(R.drawable.callection_un);
                        }
                    }
                }
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    /**
     * 阅读活动
     */
    private void toReadActiveRequest() {

        if (TextUtils.isEmpty(model.getCode())) {
            return;
        }

        Map<String, String> map = RetrofitUtils.getRequestMap();

        map.put("code", model.getCode());

        showLoadingDialog();
        Call call = RetrofitUtils.getBaseAPiService().successRequest("628510", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {


            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    /**
     * 获取链接并分享
     *
     */
    public void getUrlToShare() {

        Map<String, String> map = new HashMap<>();
        map.put("ckey", "h5Url");
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


                ShareActivity.open(ActiveDetailsActivity.this, data.getCvalue() + "/activity/activityDetail.html?code=" + model.getCode(), model.getTitle(), mShareContent, mSharePhotoUrl);

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    /**
     * 截取要分享的内容
     *
     * @param shareContent
     * @return
     */
    public String subShareContent(String shareContent) {

        if (TextUtils.isEmpty(shareContent) || shareContent.length() < 60) {
            return "";
        }
        return shareContent.substring(0, 60);
    }

    @Override
    protected void onDestroy() {
        mBinding.wvContent.clearHistory();
        ((ViewGroup) mBinding.wvContent.getParent()).removeView(mBinding.wvContent);
        mBinding.wvContent.loadUrl("about:blank");
        mBinding.wvContent.stopLoading();
        mBinding.wvContent.setWebChromeClient(null);
        mBinding.wvContent.setWebViewClient(null);
        mBinding.wvContent.destroy();
        super.onDestroy();
    }
}
