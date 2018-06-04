package com.cdkj.link_community.module.market;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.IntroductionInfoModel;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.link_community.R;
import com.cdkj.link_community.adapters.MarketCoinTabAdapter;
import com.cdkj.link_community.adapters.MarketCoinTabRecommendAdapter;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.databinding.ActivityMarketCoinTabSelectBinding;
import com.cdkj.link_community.model.MarketCoinRecommendTab;
import com.cdkj.link_community.model.MarketCoinTab;
import com.cdkj.link_community.model.event.EventCoinTabSelectedNotify;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by cdkj on 2018/5/16.
 */

public class CoinTabSelectActivity extends AbsBaseLoadActivity {

    private ActivityMarketCoinTabSelectBinding mBinding;

    private List<MarketCoinTab> mCoinTabArrayList = new ArrayList<>();

    /**
     * @param context
     */
    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, CoinTabSelectActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_market_coin_tab_select, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        getKey();
        initListener();

    }

    private void initListener() {
        mBinding.llBack.setOnClickListener(view -> {
            finish();
        });

        mBinding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(editable.toString())){
                    mBinding.llSearch.setVisibility(View.GONE);
                    mBinding.llRecommend.setVisibility(View.VISIBLE);
                }
            }
        });

        mBinding.rlSearch.setOnClickListener(view -> {
            if (TextUtils.isEmpty(mBinding.edtSearch.getText().toString())) {
                UITipDialog.showInfo(this, getString(R.string.please_input_search_info));
                return;
            }

            mBinding.llSearch.setVisibility(View.VISIBLE);
            mBinding.llRecommend.setVisibility(View.GONE);

            getSearchListRequest(mBinding.edtSearch.getText().toString());

        });
    }

    public void getKey() {

        Map<String, String> map = new HashMap<>();
        map.put("ckey", "coin_count");
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("companyCode", MyCdConfig.COMPANYCODE);

        Call call = RetrofitUtils.getBaseAPiService().getKeySystemInfo("628917", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IntroductionInfoModel>(this) {
            @Override
            protected void onSuccess(IntroductionInfoModel data, String SucMessage) {
                if (TextUtils.isEmpty(data.getCvalue())) {
                    Log.e("CoinTabSelectActivity","ata.getCvalue() is null");
                    return;
                }
                mBinding.tvMax.setText(data.getCvalue()+"");

                getListRequest();
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFail(CoinTabSelectActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
//                disMissLoading();
            }
        });

    }

    public void getListRequest() {

        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelper.getUserId());
        map.put("type", "C");


        Call call = RetrofitUtils.createApi(MyApiServer.class).getMarketCoinTab("628405", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseListCallBack<MarketCoinTab>(this) {
            @Override
            protected void onSuccess(List<MarketCoinTab> data, String SucMessage) {
                if (data == null)
                    return;

                mCoinTabArrayList.clear();

                // 初始化第一个默认条目全部
                MarketCoinTab marketCoinTab = new MarketCoinTab();
                marketCoinTab.setNavName("全部");
                mCoinTabArrayList.add(marketCoinTab);
                mCoinTabArrayList.addAll(data);

                mBinding.tvAlready.setText(data.size()+"");
                mBinding.tvSearch.setText((Integer.parseInt(mBinding.tvMax.getText().toString())- data.size())+"");
                mBinding.tvRecommend.setText((Integer.parseInt(mBinding.tvMax.getText().toString())- data.size())+"");

                mBinding.rvAlready.setAdapter(getRvAlreadyAdapter(mCoinTabArrayList));
                mBinding.rvAlready.setLayoutManager(getLinearLayoutManager());

                getRecommendListRequest();
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                super.onReqFailure(errorCode, errorMessage);
            }

            @Override
            protected void onFinish() {
//                disMissLoading();
            }
        });

    }

    private MarketCoinTabAdapter getRvAlreadyAdapter(List<MarketCoinTab> data){

        MarketCoinTabAdapter marketCoinTabAdapter = new MarketCoinTabAdapter(data, true);

        marketCoinTabAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            MarketCoinTab marketCoinTab = marketCoinTabAdapter.getItem(position);
            deleteRequest(marketCoinTab.getId());
        });

        return marketCoinTabAdapter;

    }

    public void getRecommendListRequest() {

        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelper.getUserId());
        map.put("location", "1");


        Call call = RetrofitUtils.createApi(MyApiServer.class).getMarketCoinRecommendTab("628309", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseListCallBack<MarketCoinRecommendTab>(this) {
            @Override
            protected void onSuccess(List<MarketCoinRecommendTab> data, String SucMessage) {
                if (data == null)
                    return;

                mBinding.rvRecommend.setAdapter(getRvRecommendAdapter(data, mCoinTabArrayList));
                mBinding.rvRecommend.setLayoutManager(getLinearLayoutManager());

            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                super.onReqFailure(errorCode, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    private MarketCoinTabRecommendAdapter getRvRecommendAdapter(List<MarketCoinRecommendTab> data, List<MarketCoinTab> marketCoinTabList){

        MarketCoinTabRecommendAdapter marketCoinTabAdapter = new MarketCoinTabRecommendAdapter(data, marketCoinTabList);

        marketCoinTabAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            MarketCoinRecommendTab coinRecommendTab = marketCoinTabAdapter.getItem(position);
            addRequest(coinRecommendTab.getEname());
        });

        return marketCoinTabAdapter;

    }


    /**
     * 获取布局管理器
     *
     * @return
     */
    @NonNull
    private GridLayoutManager getLinearLayoutManager() {
        return new GridLayoutManager(this, 4){
            @Override
            public boolean canScrollVertically() {  //禁止自滚动
                return false;
            }
        };
    }


    public void addRequest(String ename) {

        List<String> enameList = new ArrayList<>();
        enameList.add(ename);

        Map<String, Object> map = new HashMap<>();

        map.put("userId", SPUtilHelper.getUserId());
        map.put("type", "C");
        map.put("enameList", enameList);


        Call call = RetrofitUtils.getBaseAPiService().successRequest("628400", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data == null)
                    return;

                if (data.isSuccess()){
                    UITipDialog.showSuccess(CoinTabSelectActivity.this, getString(R.string.do_succ), dialogInterface -> {
                        getListRequest();
                    });
                }else {
                    UITipDialog.showSuccess(CoinTabSelectActivity.this, getString(R.string.do_fall));
                }

            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                super.onReqFailure(errorCode, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    public void deleteRequest(String id) {

        Map<String, Object> map = new HashMap<>();

        map.put("userId", SPUtilHelper.getUserId());
        map.put("type", "C");
        map.put("id", id);


        Call call = RetrofitUtils.getBaseAPiService().successRequest("628401", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data == null)
                    return;

                if (data.isSuccess()){
                    UITipDialog.showSuccess(CoinTabSelectActivity.this, getString(R.string.do_succ), dialogInterface -> {
                        getListRequest();
                    });
                }else {
                    UITipDialog.showSuccess(CoinTabSelectActivity.this, getString(R.string.do_fall));
                }

            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                super.onReqFailure(errorCode, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    public void getSearchListRequest(String keywords) {

        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelper.getUserId());
        map.put("keywords", keywords);


        Call call = RetrofitUtils.createApi(MyApiServer.class).getMarketCoinRecommendTab("628309", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseListCallBack<MarketCoinRecommendTab>(this) {
            @Override
            protected void onSuccess(List<MarketCoinRecommendTab> data, String SucMessage) {
                if (data == null)
                    return;

                if (data.size() == 0){
                    ToastUtil.show(CoinTabSelectActivity.this, getString(R.string.no_search_info));
                }

                mBinding.rvSearch.setAdapter(getRvSearchAdapter(data, mCoinTabArrayList));
                mBinding.rvSearch.setLayoutManager(getLinearLayoutManager());

            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                super.onReqFailure(errorCode, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    private MarketCoinTabRecommendAdapter getRvSearchAdapter(List<MarketCoinRecommendTab> data, List<MarketCoinTab> marketCoinTabList){

        MarketCoinTabRecommendAdapter marketCoinTabAdapter = new MarketCoinTabRecommendAdapter(data, marketCoinTabList);

        marketCoinTabAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            MarketCoinRecommendTab coinRecommendTab = marketCoinTabAdapter.getItem(position);
            addRequest(coinRecommendTab.getEname());
        });

        return marketCoinTabAdapter;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().post(new EventCoinTabSelectedNotify());
    }
}
