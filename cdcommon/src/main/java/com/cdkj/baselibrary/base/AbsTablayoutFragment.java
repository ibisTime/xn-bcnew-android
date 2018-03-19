package com.cdkj.baselibrary.base;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.adapters.TablayoutAdapter;
import com.cdkj.baselibrary.databinding.ActivityTabBinding;

import java.util.List;

/**
 *
 * Created by cdkj on 2017/6/15.
 */

public abstract class AbsTablayoutFragment extends BaseLazyFragment {

    protected ActivityTabBinding mTabLayoutBinding;

    /*Tablayout 适配器*/
    protected TablayoutAdapter tablayoutAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mTabLayoutBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_tab, null, false);
        initViewPager();
        return mTabLayoutBinding.getRoot();
    }


    private void initViewPager() {

        tablayoutAdapter = new TablayoutAdapter(getChildFragmentManager());

        List<Fragment> mFragments = getFragments();
        List<String> mTitles = getFragmentTitles();

        tablayoutAdapter.addFrag(mFragments, mTitles);

        mTabLayoutBinding.viewpager.setAdapter(tablayoutAdapter);
        mTabLayoutBinding.tablayout.setupWithViewPager(mTabLayoutBinding.viewpager);        //viewpager和tablayout关联
        mTabLayoutBinding.viewpager.setOffscreenPageLimit(tablayoutAdapter.getCount());
//        mTabLayoutBinding.tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);//设置滑动模式 /TabLayout.MODE_SCROLLABLE 可滑动 ，TabLayout.MODE_FIXED表示不可滑动
    }

    //获取要显示的fragment
    public abstract List<Fragment> getFragments();

    //获取要显示的title
    public abstract List<String> getFragmentTitles();

}
