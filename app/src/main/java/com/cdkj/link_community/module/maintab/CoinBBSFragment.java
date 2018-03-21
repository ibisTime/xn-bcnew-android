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
import com.cdkj.link_community.databinding.FragmentCoinBbsBinding;
import com.cdkj.link_community.module.message.FastMessageListFragment;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 币吧
 * Created by cdkj on 2018/3/21.
 */

public class CoinBBSFragment extends BaseLazyFragment {


    private FragmentCoinBbsBinding mBinding;

    public static CoinBBSFragment getInstanse() {
        CoinBBSFragment fragment = new CoinBBSFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_coin_bbs, null, false);

        initViewPager();

        return mBinding.getRoot();

    }

    private void initViewPager() {

        //设置fragment数据
        ArrayList fragments = new ArrayList<>();

        fragments.add(FastMessageListFragment.getInstanse());
        fragments.add(FastMessageListFragment.getInstanse());
        fragments.add(FastMessageListFragment.getInstanse());

        mBinding.viewpager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(), fragments));
        mBinding.viewpager.setOffscreenPageLimit(fragments.size());

        mBinding.viewpager.setPagingEnabled(true);
        mBinding.viewindicator.setVisibleTabCount(fragments.size());
        mBinding.viewindicator.setmLinWidth(25);
        mBinding.viewindicator.setTabItemTitles(Arrays.asList("全部", "热门", "关注"));
        mBinding.viewindicator.setViewPager(mBinding.viewpager, 0);

    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void onInvisible() {

    }
}
