package com.cdkj.link_community.module.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.cdkj.baselibrary.adapters.TablayoutAdapter;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.link_community.R;
import com.cdkj.link_community.databinding.ActivityUserMyMessageBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cdkj on 2018/5/18.
 */

public class MyMessageActivity extends AbsBaseLoadActivity {

    private ActivityUserMyMessageBinding mBinding;

    private List<String> mTitleList;
    private List<Fragment> mFragmentList;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, MyMessageActivity.class);
        context.startActivity(intent);
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_user_my_message, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle("我的文章");

        mTitleList = new ArrayList<>();
        mFragmentList = new ArrayList<>();

        initViewPager();
    }

    protected void initViewPager() {

        TablayoutAdapter tablayoutAdapter = new TablayoutAdapter(getSupportFragmentManager());

        List<Fragment> mFragments = getFragments();
        List<String> mTitles = getFragmentTitles();

        tablayoutAdapter.addFrag(mFragments, mTitles);

        mBinding.viewpager.setAdapter(tablayoutAdapter);
        mBinding.tablayout.setupWithViewPager(mBinding.viewpager);        //viewpager和tablayout关联
        mBinding.viewpager.setOffscreenPageLimit(tablayoutAdapter.getCount());
//        mTabLayoutBinding.tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);//设置滑动模式 /TabLayout.MODE_SCROLLABLE 可滑动 ，TabLayout.MODE_FIXED表示不可滑动
    }

    public List<Fragment> getFragments() {
        mFragmentList.add(MyReleaseMessageListFragment.getInstance(true, "0"));
//        mFragmentList.add(MyReleaseMessageListFragment.getInstance(false, ""));
        mFragmentList.add(MyReleaseMessageListFragment.getInstance(false, "1"));
        return mFragmentList;
    }

    public List<String> getFragmentTitles() {
        mTitleList.add("待审核");
//        mTitleList.add("审核中");
        mTitleList.add("已通过");
        return mTitleList;
    }
}
