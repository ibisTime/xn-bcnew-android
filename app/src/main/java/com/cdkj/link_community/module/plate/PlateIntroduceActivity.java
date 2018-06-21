package com.cdkj.link_community.module.plate;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.databinding.ActivityWebviewBinding;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.IntroductionInfoModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.model.PlateDetailsModel;
import com.cdkj.link_community.module.user.ShareActivity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;


/**
 * 版块介绍
 */
public class PlateIntroduceActivity extends AbsBaseLoadActivity {

    private ActivityWebviewBinding mBinding;
    private WebView webView;
    private PlateDetailsModel plateDetailsModel;


    public static void open(Context context, PlateDetailsModel plateDetailsModel) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, PlateIntroduceActivity.class);
        intent.putExtra(CdRouteHelper.DATA_SIGN, plateDetailsModel);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_webview, null, false);
        return mBinding.getRoot();
    }


    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setRightTitle(getString(R.string.share));
        initLayout();
        initData();
    }

    @Override
    public void topTitleViewRightClick() {
        getUrlToShare();

    }


    private void initLayout() {
        //输入法
        if (getWindow() != null) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        webView = new WebView(getApplicationContext());
        webView.setLayoutParams(params);

        WebSettings webSettings = webView.getSettings();
        if (webSettings != null) {
            webSettings.setJavaScriptEnabled(true);//js
            webSettings.setDefaultTextEncodingName("UTF-8");
        }

        webView.setWebChromeClient(new MyWebViewClient1());
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                Log.e("lxj", error.toString() + "");
                handler.proceed(); // 接受网站证书
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mBinding.llAboutUs.addView(webView, 1);

    }

    private void initData() {
        if (getIntent() == null) {
            return;
        }

        plateDetailsModel = getIntent().getParcelableExtra(CdRouteHelper.DATA_SIGN);

        if (plateDetailsModel != null) {
            mBaseBinding.titleView.setMidTitle(plateDetailsModel.getName());

            webView.loadData(plateDetailsModel.getDescription(), "text/html;charset=UTF-8", "UTF-8");
        }
    }


    /**
     * 获取链接并分享
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
                if (plateDetailsModel != null) {
                    ShareActivity.open(PlateIntroduceActivity.this, data.getCvalue() + "/blockShare/blockShare.html?code=" + plateDetailsModel.getCode(),
                            plateDetailsModel.getName(), plateDetailsModel.getName(), "");
                }

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }


    public void getKeyUrl(String key) {

        if (TextUtils.isEmpty(key)) {
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("ckey", key);
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
                webView.loadData(data.getCvalue(), "text/html;charset=UTF-8", "UTF-8");
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFail(PlateIntroduceActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }


    private class MyWebViewClient1 extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            mBinding.pb.setProgress(newProgress);
            if (newProgress > 90) {
                mBinding.pb.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, newProgress);
        }
    }


    @Override
    public void onBackPressed() {
        goBack();
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.clearHistory();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.loadUrl("about:blank");
            webView.stopLoading();
            webView.setWebChromeClient(null);
            webView.setWebViewClient(null);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

    private void goBack() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }

}
