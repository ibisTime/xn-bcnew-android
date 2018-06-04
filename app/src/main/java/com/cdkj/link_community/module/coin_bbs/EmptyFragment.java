package com.cdkj.link_community.module.coin_bbs;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.baselibrary.databinding.EmptyViewBinding;
import com.cdkj.link_community.R;

/**
 * Created by cdkj on 2018/3/26.
 */

public class EmptyFragment extends BaseLazyFragment {
    public static EmptyFragment getInstance() {
        EmptyFragment fragment = new EmptyFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EmptyViewBinding emptyViewBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.empty_view, null, false);
        return emptyViewBinding.getRoot();
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void onInvisible() {

    }
}
