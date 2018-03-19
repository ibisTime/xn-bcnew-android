package com.cdkj.link_community.module.message;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.adapters.ViewPagerAdapter;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.link_community.R;
import com.cdkj.link_community.databinding.FragmentFastMessageBinding;
import com.cdkj.link_community.module.maintab.FirstPageFragment;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 快讯
 * Created by cdkj on 2018/3/19.
 */

public class FastMessageFragment extends BaseLazyFragment {


    private FragmentFastMessageBinding mBinding;


    public static FastMessageFragment getInstanse() {
        FastMessageFragment fragment = new FastMessageFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_fast_message, null, false);

        initViews();

        return mBinding.getRoot();
    }

    private void initViews() {

        //设置fragment数据
        ArrayList fragments = new ArrayList<>();

        fragments.add(FastMessageListFragment.getInstanse());
        fragments.add(FastMessageListFragment.getInstanse());

        mBinding.viewpager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(), fragments));
        mBinding.viewpager.setOffscreenPageLimit(fragments.size());

        mBinding.viewpager.setPagingEnabled(true);

        mBinding.viewindicator.setmLinWidth(20);
        mBinding.viewindicator.setTabItemTitles(Arrays.asList("全部", "热点"));
        mBinding.viewindicator.setViewPager(mBinding.viewpager, 0);

    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void onInvisible() {

    }
}
