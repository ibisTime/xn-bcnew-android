package com.cdkj.link_community.module.maintab;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.adapters.ViewPagerAdapter;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.link_community.R;
import com.cdkj.link_community.databinding.FragmentMarketBinding;
import com.cdkj.link_community.module.market.MyChooseFragment;
import com.cdkj.link_community.module.market.search.SearchMarketActivity;

import java.util.ArrayList;

/**
 * 首页 资讯
 * Created by cdkj on 2018/3/13.
 */

public class MarketPageFragment extends BaseLazyFragment {

    private FragmentMarketBinding mBinding;


    public static MarketPageFragment getInstanse() {
        MarketPageFragment fragment = new MarketPageFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_market, null, false);

        initTopTitle();

        initViewPager();

        return mBinding.getRoot();
    }

    private void initTopTitle() {

        mBinding.titleLayout.fraToSearch.setOnClickListener(view -> SearchMarketActivity.open(mActivity));

    }

    private void initViewPager() {
        //设置fragment数据
        ArrayList fragments = new ArrayList<>();

        fragments.add(MyChooseFragment.getInstanse());
        fragments.add(MyChooseFragment.getInstanse());
        fragments.add(MyChooseFragment.getInstanse());

        mBinding.viewpager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(), fragments));
        mBinding.viewpager.setOffscreenPageLimit(fragments.size());


    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void onInvisible() {

    }
}
