package com.cdkj.link_community.module.market;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.base.BaseWebViewFragment;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.IntroductionInfoModel;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.model.eventmodels.FullScreenModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.UIStatusBarHelper;
import com.cdkj.link_community.R;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.databinding.ActivityMarketBinding;
import com.cdkj.link_community.model.CoinListModel;
import com.cdkj.link_community.utils.AccountUtil;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.link_community.utils.AccountUtil.MONEY_SIGN;
import static com.cdkj.link_community.utils.AccountUtil.MONEY_SIGN_USD;
import static com.cdkj.link_community.utils.AccountUtil.formatPercent;
import static com.cdkj.link_community.utils.AccountUtil.scale;

/**
 * Created by cdkj on 2018/5/7.
 */

public class MarketActivity extends AbsBaseLoadActivity {

    private ActivityMarketBinding mBinding;

    private String id;
    private CoinListModel model;

    private String url = "";

    private String mSharePhotoUrl;//要分分享的图片url
    private String mShareContent;//要分享的内容

    /**
     * @param context
     */
    public static void open(Context context, String id) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, MarketActivity.class);
        intent.putExtra(CdRouteHelper.DATA_SIGN, id);
        context.startActivity(intent);
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_market, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        if (getIntent() == null)
            return;

        id = getIntent().getStringExtra(CdRouteHelper.DATA_SIGN);

        getMarket(true);

        initListener();
    }

    private void initListener() {
        mBinding.flBack.setOnClickListener(view -> {

            finish();
        });

        mBinding.llRefresh.setOnClickListener(view -> {

            getMarket(true);
        });

        mBinding.llChoice.setOnClickListener(view -> {

            if (!SPUtilHelper.isLogin(this, false)) {
                return;
            }

            toCollectRequest();
        });

        mBinding.llWarn.setOnClickListener(view -> {

            if (!SPUtilHelper.isLogin(this, false)) {
                return;
            }

            MarketWarnActivity.open(this, model);
        });

        mBinding.llCapital.setOnClickListener(view -> {

            MarketProjectActivity.open(this, model);
        });

        mBinding.llShare.setOnClickListener(view -> {

//            getUrlToShare();
        });

        mBinding.llMore.setOnClickListener(view -> {

            mBinding.llMoreDialog.setVisibility(mBinding.llMoreDialog.getVisibility() == View.GONE ?  View.VISIBLE : View.GONE);
        });

        mBinding.tvUsd.setOnClickListener(view -> {

            mBinding.llMoreDialog.setVisibility(View.GONE);
            if(model.getLastUsdPrice() != null)
                mBinding.tvPrice.setText(MONEY_SIGN_USD+ scale(model.getLastUsdPrice(),2));
        });

        mBinding.tvCny.setOnClickListener(view -> {

            mBinding.llMoreDialog.setVisibility(View.GONE);
            if(model.getLastCnyPrice() != null)
                mBinding.tvPrice.setText(MONEY_SIGN+ scale(model.getLastCnyPrice(),2));
        });

        mBinding.tvOriginal.setOnClickListener(view -> {

            mBinding.llMoreDialog.setVisibility(View.GONE);
            if(model.getLastPrice() != null)
                mBinding.tvPrice.setText(model.getLastPrice());
        });
    }

    private void getMarket(boolean isInitChart) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("userId", SPUtilHelper.getUserId());
        showLoadingDialog();

        Call call = RetrofitUtils.createApi(MyApiServer.class).getMarket("628352", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<CoinListModel>(this) {

            @Override
            protected void onSuccess(CoinListModel data, String SucMessage) {

                if (data == null )
                    return;

                model = data;

                setTitle();
                setView();

                if (isInitChart)
                    getUrlToShare();
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    private void setTitle(){
        mBinding.tvExchange.setText(model.getExchangeCname());
        mBinding.tvSymbolPair.setText(model.getSymbolPair().toUpperCase());
    }

    private void setView() {
//        mSharePhotoUrl = StringUtils.getAsPicListIndexOne(model.getAdvPic());
//        mShareContent = subShareContent(StringUtils.delHTMLTag(model.getContent()));

        setPageBgColor(model.getPercentChange());

        if(model.getLastCnyPrice() != null) {
            mBinding.tvPrice.setText(MONEY_SIGN+ scale(model.getLastCnyPrice(),2));
        }

        double rate = Double.parseDouble(model.getPercentChange());
        mBinding.tvRangePercent.setText(formatPercent(rate*100) + "%");
        mBinding.tvRange.setText(AccountUtil.scale(model.getPriceChange(), 6));

        mBinding.tvHigh.setText("高: " + AccountUtil.scale(model.getHigh(), 6));
        mBinding.tvLow.setText("低: " + AccountUtil.scale(model.getLow(), 6));
        mBinding.tvOpen.setText("开: " + AccountUtil.scale(model.getOpen(), 6));
        mBinding.tvClose.setText("收: " + AccountUtil.scale(model.getClose(), 6));

        if (TextUtils.isEmpty(model.getBidPrice())){
            mBinding.tvBuy.setText("买: --");
        }else {
            mBinding.tvBuy.setText("买: " + AccountUtil.scale(model.getBidPrice(), 6));
        }

        if (TextUtils.isEmpty(model.getAskPrice())){
            mBinding.tvSale.setText("卖: --");
        }else {
            mBinding.tvSale.setText("卖: " + AccountUtil.scale(model.getAskPrice(), 6));
        }

        mBinding.tvQuantity.setText("量: " + AccountUtil.scale(model.getVolume(), 6));

        if (TextUtils.equals(model.getIsChoice(), "1")) {
            mBinding.ivChoice.setImageResource(R.drawable.market_cancel_choice);
        } else {
            mBinding.ivChoice.setImageResource(R.drawable.market_add_choice);
        }

    }

    private void setPageBgColor(String percent){
        double rate = Double.parseDouble(percent);
        if (rate == 0) {
            UIStatusBarHelper.translucent(this, ContextCompat.getColor(this, R.color.colorPrimary));
            mBinding.llRoot.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        } else if (rate > 0) {
            UIStatusBarHelper.translucent(this, ContextCompat.getColor(this, R.color.market_green));
            mBinding.llRoot.setBackgroundColor(ContextCompat.getColor(this, R.color.market_green));
        } else {
            UIStatusBarHelper.translucent(this, ContextCompat.getColor(this, R.color.market_red));
            mBinding.llRoot.setBackgroundColor(ContextCompat.getColor(this, R.color.market_red));
        }
    }

    private void initChartWebView() {
        FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();

        mFragmentTransaction.add(R.id.fl_chart, BaseWebViewFragment.getInstance(url + "/charts/index.html?symbol="
                + model.getSymbol()
                + "/"
                + model.getToSymbol()
                + "&exchange="
                + model.getExchangeEname()
                + "&isfull=0",true));
        mFragmentTransaction.commit();

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

                url = data.getCvalue();

                initChartWebView();

//                String shareTitle = mBinding.contentLayout.tvTitle.getText().toString();
//                ShareActivity.open(MarketActivity.this, data.getCvalue() + "?code=" + mCode, shareTitle, mShareContent, mSharePhotoUrl);

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    private void toCollectRequest() {

        Map<String, String> map = new HashMap<>();
        map.put("userId", SPUtilHelper.getUserId());
        map.put("exchangeEname", model.getExchangeEname());
        map.put("toSymbol", model.getToSymbol());
        map.put("symbol", model.getSymbol());

        Call call = RetrofitUtils.getBaseAPiService().successRequest("628330", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {

            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {
                    UITipDialog.showSuccess(MarketActivity.this, getString(R.string.do_succ), dialogInterface -> getMarket(false));
                }else {
                    UITipDialog.showFail(MarketActivity.this, getString(R.string.do_fall));
                }
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


    @Subscribe
    public void toFullScreen(FullScreenModel fullScreenModel){
        if (fullScreenModel == null)
            return;

        if (fullScreenModel.getIndex().equals("1"))
            MarketKLineActivity.open(this, model, url);
    }
}
