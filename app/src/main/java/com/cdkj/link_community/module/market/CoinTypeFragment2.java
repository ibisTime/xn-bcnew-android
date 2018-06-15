package com.cdkj.link_community.module.market;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.adapters.TablayoutAdapter;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.views.tablayout.TabLayout;
import com.cdkj.link_community.R;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.databinding.FragmentMarketCoinTabBinding;
import com.cdkj.link_community.model.MarketCoinTab;
import com.cdkj.link_community.model.event.EventCoinTabSelectedNotify;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 币种 Fragment
 * Created by cdkj on 2018/3/24.
 */

public class CoinTypeFragment2 extends BaseLazyFragment {

    protected FragmentMarketCoinTabBinding mTabLayoutBinding;

    private List<MarketCoinTab> mCoinTabArrayList = new ArrayList<>();

    /*Tablayout 适配器*/
    protected TablayoutAdapter tablayoutAdapter;

    private boolean isFirstRequest;//是否进行了第一次请求

    private List<String> mTitleList;
    private List<Fragment> mFragmentList;

    public static CoinTypeFragment2 getInstance() {
        CoinTypeFragment2 fragment = new CoinTypeFragment2();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mTabLayoutBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_market_coin_tab, null, false);

        initListener();

        mTitleList = new ArrayList<>();
        mFragmentList = new ArrayList<>();

        return mTabLayoutBinding.getRoot();
    }

    private void initListener() {
        mTabLayoutBinding.rlAdd.setOnClickListener(view -> {
            if (!SPUtilHelper.isLogin(mActivity, false)) {
                return;
            }

            CoinTabSelectActivity.open(mActivity);
        });
    }


    @Override
    protected void lazyLoad() {

        if (mTabLayoutBinding == null || isFirstRequest) return;

        getListRequest(false);

        isFirstRequest = true;


    }

    @Override
    protected void onInvisible() {

    }

    /**
     * 获取币种请求
     */
//    private void getCoinTypeRequest() {
//
//        Map<String, String> map = new HashMap<>();
//
//        Call call = RetrofitUtils.createApi(MyApiServer.class).getCoinTypeList("628307", StringUtils.getJsonToString(map));
//
//        addCall(call);
//
//        showLoadingDialog();
//
//        call.enqueue(new BaseResponseListCallBack<CoinPlatformType>(mActivity) {
//
//            @Override
//            protected void onSuccess(List<CoinPlatformType> data, String SucMessage) {
//
//            }
//
//            @Override
//            protected void onFinish() {
//                disMissLoading();
//            }
//        });
//    }
    public void getListRequest(boolean isShow) {

        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelper.getUserId());
        map.put("type", "C");


        Call call = RetrofitUtils.createApi(MyApiServer.class).getMarketCoinTab("628405", StringUtils.getJsonToString(map));

        addCall(call);

        if (isShow)
            showLoadingDialog();

        call.enqueue(new BaseResponseListCallBack<MarketCoinTab>(mActivity) {
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


                initViewPagerData(mCoinTabArrayList);
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

    private void initViewPagerData(List<MarketCoinTab> data) {
//        mTitleList.add(getString(R.string.coin_price));
//        mFragmentList.add(CoinTypePriceListFragment.getInstance(true));

        mTitleList.clear();
        mFragmentList.clear();

        int i = 0;

        for (MarketCoinTab coinType : data) {
            if (coinType == null) continue;
            mTitleList.add(coinType.getNavName());
            mFragmentList.add(CoinTypeListFragment.getInstance(coinType.getNavName(), i == 0 ? true : false));

            i++;
        }

        initViewPager();
        mTabLayoutBinding.viewpager.setOffscreenPageLimit(0);
        mTabLayoutBinding.tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    protected void initViewPager() {

        tablayoutAdapter = new TablayoutAdapter(getChildFragmentManager());

        List<Fragment> mFragments = mFragmentList;
        List<String> mTitles = mTitleList;

        tablayoutAdapter.addFrag(mFragments, mTitles);

        mTabLayoutBinding.viewpager.setAdapter(tablayoutAdapter);
        mTabLayoutBinding.tablayout.setupWithViewPager(mTabLayoutBinding.viewpager);        //viewpager和tablayout关联
        mTabLayoutBinding.viewpager.setOffscreenPageLimit(tablayoutAdapter.getCount());
//        mTabLayoutBinding.tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);//设置滑动模式 /TabLayout.MODE_SCROLLABLE 可滑动 ，TabLayout.MODE_FIXED表示不可滑动
    }

    @Subscribe
    public void doRefresh(EventCoinTabSelectedNotify selectedNotify) {
        getListRequest(true);
    }

}
