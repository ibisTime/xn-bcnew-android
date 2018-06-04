package com.cdkj.link_community.module.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.adapters.ViewPagerAdapter;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.link_community.R;
import com.cdkj.link_community.databinding.ActivityUserCollectListBinding;
import com.cdkj.link_community.model.TabCurrentModel;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by cdkj on 2018/4/30.
 */

public class UserCollectListActivity extends AbsBaseLoadActivity {

    private ActivityUserCollectListBinding mBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, UserCollectListActivity.class);
        context.startActivity(intent);
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_user_collect_list, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle(getString(R.string.collection));

        initViews();
    }

    private void initViews() {

        //设置fragment数据
        ArrayList fragments = new ArrayList<>();

        fragments.add(MyCollectMessageListFragment.getInstance());
        fragments.add(MyCollectActiveListFragment.getInstance());

        mBinding.viewpager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));
        mBinding.viewpager.setOffscreenPageLimit(fragments.size());

        mBinding.viewpager.setPagingEnabled(true);

        mBinding.viewindicator.setmLinWidth(25);
        mBinding.viewindicator.setTabItemTitles(Arrays.asList(getString(R.string.msg), getString(R.string.active)));
        mBinding.viewindicator.setViewPager(mBinding.viewpager, 0);

    }

    @Subscribe
    public void setTabToHotMsgEvent(TabCurrentModel tabCurrentModel){
        if (tabCurrentModel == null)
            return;

        mBinding.viewindicator.setViewPager(mBinding.viewpager, tabCurrentModel.getCurrent());
    }

}
