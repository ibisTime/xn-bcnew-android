package com.cdkj.link_community.module.market;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.link_community.R;
import com.cdkj.link_community.databinding.FragmentMarketBinding;

/**
 * 自选Fragment
 * Created by cdkj on 2018/3/13.
 */

public class PlatformFragment extends BaseLazyFragment {

    private FragmentMarketBinding mBinding;


    public static PlatformFragment getInstanse() {
        PlatformFragment fragment = new PlatformFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_market, null, false);


        return mBinding.getRoot();
    }


    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void onInvisible() {

    }
}
