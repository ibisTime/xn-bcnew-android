package com.cdkj.link_community.module.maintab;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.link_community.R;
import com.cdkj.link_community.databinding.FragmentUserBinding;

/**
 * 我的
 * Created by cdkj on 2018/3/22.
 */

public class UserFragment extends BaseLazyFragment {


    private FragmentUserBinding mBinding;

    public static UserFragment getInstanse() {
        UserFragment fragment = new UserFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_user, null, false);

        return mBinding.getRoot();
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void onInvisible() {

    }
}
