package com.cdkj.link_community.module.market;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.base.AbsTablayoutFragment;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.model.CoinType;
import com.cdkj.link_community.module.message.FastMessageListFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 币种 Fragment
 * Created by cdkj on 2018/3/24.
 */

public class CoinTypeFragment extends AbsTablayoutFragment {

    private boolean isFirstRequest;//是否进行了第一次请求

    private List<String> mTitleList;
    private List<Fragment> mFragmentList;

    public static CoinTypeFragment getInstanse() {
        CoinTypeFragment fragment = new CoinTypeFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mTitleList = new ArrayList<>();
        mFragmentList = new ArrayList<>();

        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    protected void lazyLoad() {

        if (mTabLayoutBinding == null || isFirstRequest) return;

        getCoinTypeRequest();

        isFirstRequest = true;


    }

    @Override
    protected void onInvisible() {

    }


    @Override
    public List<Fragment> getFragments() {
        return mFragmentList;
    }

    @Override
    public List<String> getFragmentTitles() {
        return mTitleList;
    }


    /**
     * 获取币种请求
     */
    private void getCoinTypeRequest() {

        Map<String, String> map = new HashMap<>();

        Call call = RetrofitUtils.createApi(MyApiServer.class).getCoinTypeList("628307", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseListCallBack<CoinType>(mActivity) {

            @Override
            protected void onSuccess(List<CoinType> data, String SucMessage) {

                initViewPagerData(data);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    private void initViewPagerData(List<CoinType> data) {
        int i = 0;

        for (CoinType coinType : data) {
            if (coinType == null) continue;
            mTitleList.add(coinType.getEname());
            mFragmentList.add(FastMessageListFragment.getInstanse(0, false));
            i++;
        }

        initViewPager();
        mTabLayoutBinding.viewpager.setOffscreenPageLimit(4);
        mTabLayoutBinding.tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

}
