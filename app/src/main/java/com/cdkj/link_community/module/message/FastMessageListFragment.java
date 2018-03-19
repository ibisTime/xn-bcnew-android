package com.cdkj.link_community.module.message;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.cdkj.baselibrary.base.AbsRefreshListFragment;
import com.cdkj.link_community.adapters.FastMessageListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 快讯列表
 * Created by cdkj on 2018/3/13.
 */

public class FastMessageListFragment extends AbsRefreshListFragment {

    public static FastMessageListFragment getInstanse() {
        FastMessageListFragment fragment = new FastMessageListFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void onInvisible() {

    }

    @Override
    protected void afterCreate(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initRefreshHelper(10);
    }

    @Override
    public RecyclerView.Adapter getListAdapter(List listData) {

        List<String> f = new ArrayList<>();

        f.add("dd");
        f.add("dd");
        f.add("dd");
        f.add("dd");
        f.add("dd");
        f.add("dd");
        f.add("dd");
        f.add("dd");
        f.add("dd");
        f.add("dd");
        f.add("dd");
        f.add("dd");
        f.add("dd");
        f.add("dd");
        f.add("dd");
        FastMessageListAdapter fastMessageListAdapter = new FastMessageListAdapter(f);

        fastMessageListAdapter.setOnItemChildClickListener((adapter, view, position) -> {

            FastMessageToShareActivity.open(mActivity);

        });

        return fastMessageListAdapter;
    }

    @Override
    public void getListRequest(int pageindex, int limit, boolean isShowDialog) {

    }
}
