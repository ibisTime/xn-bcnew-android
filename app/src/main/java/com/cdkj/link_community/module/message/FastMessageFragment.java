package com.cdkj.link_community.module.message;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.adapters.TablayoutAdapter;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.link_community.R;
import com.cdkj.link_community.databinding.FragmentFastMessageBinding;
import com.cdkj.link_community.model.TabCurrentModel;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 快讯
 * Created by cdkj on 2018/3/19.
 */

public class FastMessageFragment extends BaseLazyFragment {


    private FragmentFastMessageBinding mBinding;
    private boolean isCreate;


    public static FastMessageFragment getInstance() {
        FastMessageFragment fragment = new FastMessageFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_fast_message, null, false);
        return mBinding.getRoot();
    }


    private void initViews() {

        TablayoutAdapter tablayoutAdapter = new TablayoutAdapter(getChildFragmentManager());
        //设置fragment数据
        ArrayList fragments = new ArrayList<>();

        fragments.add(FastMessageListFragment.getInstance(FastMessageListFragment.ALLMESSAGE, true));
        fragments.add(FastMessageListFragment.getInstance(FastMessageListFragment.HOTMESSAGE, false));

        tablayoutAdapter.addFrag(fragments, Arrays.asList(getString(R.string.all), getString(R.string.hot_message)));

        mBinding.viewpager.setAdapter(tablayoutAdapter);

        mBinding.tablayout.setupWithViewPager(mBinding.viewpager);        //viewpager和tablayout关联

        mBinding.viewpager.setOffscreenPageLimit(fragments.size());

    }


    @Subscribe
    public void setTabToHotMsgEvent(TabCurrentModel tabCurrentModel) {
        if (tabCurrentModel == null || tabCurrentModel.getCurrent() < 0 || tabCurrentModel.getCurrent() > 1)
            return;

        mBinding.viewpager.setCurrentItem(tabCurrentModel.getCurrent());
//        mBinding.viewindicator.setViewPager(mBinding.viewpager, tabCurrentModel.getCurrent());
    }

    @Override
    protected void lazyLoad() {

        if (mBinding == null || isCreate) {
            return;
        }
        isCreate = true;
        initViews();


    }

    @Override
    protected void onInvisible() {

    }
}
