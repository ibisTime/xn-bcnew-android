package com.cdkj.link_community.module.active;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import com.cdkj.baselibrary.base.AbsTablayoutActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cdkj on 2018/6/2.
 */

public class ActiveApproveUserActivity extends AbsTablayoutActivity {

    private List<String> mTitleList;
    private List<Fragment> mFragmentList;

    private String code;

    /**
     * @param context
     */
    public static void open(Context context,String code) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, ActiveApproveUserActivity.class);
        intent.putExtra("code",code);
        context.startActivity(intent);
    }


    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle("资信调查");

        mTitleList = new ArrayList<>();
        mFragmentList = new ArrayList<>();

        if (getIntent() == null)
            return;

        code = getIntent().getStringExtra("code");

        initViewPagerData();
    }

    private void initViewPagerData() {

        mTitleList.add("已通过");
        mFragmentList.add(ActiveApproveUserFragment.getInstance(true, code,"1"));

        mTitleList.add("未审核");
        mFragmentList.add(ActiveApproveUserFragment.getInstance(false, code,"0"));

        initViewPager();
        mTabLayoutBinding.viewpager.setOffscreenPageLimit(2);
        mTabLayoutBinding.tablayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    public List<Fragment> getFragments() {
        return mFragmentList;
    }

    @Override
    public List<String> getFragmentTitles() {
        return mTitleList;
    }
}
