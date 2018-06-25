package com.cdkj.link_community.module.active;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsRefreshListActivity;
import com.cdkj.baselibrary.base.AbsTablayoutActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.adapters.MyActiveListAdapter;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.model.MyActiveModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

public class MyActiveTabActivity extends AbsTablayoutActivity {

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, MyActiveTabActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        super.afterCreate(savedInstanceState);
        mBaseBinding.titleView.setMidTitle(getString(R.string.user_active));
    }

    @Override
    public List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(MyActiveListFragment.getInstanse(true));
        fragments.add(MyActiveListFragment.getInstanse(false));
        return fragments;
    }

    @Override
    public List<String> getFragmentTitles() {
        List<String> strings = new ArrayList<>();
        strings.add("已通过");
        strings.add("未通过");
        return strings;
    }
}
