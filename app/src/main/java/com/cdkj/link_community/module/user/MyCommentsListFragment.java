package com.cdkj.link_community.module.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.cdkj.baselibrary.base.AbsRefreshListFragment;
import com.cdkj.link_community.adapters.UserMyCommentListAdapter;

import java.util.List;

/**
 * 我的评论列表
 * Created by 李先俊 on 2018/3/22.
 */

public class MyCommentsListFragment extends AbsRefreshListFragment {


    public static MyCommentsListFragment getInstanse() {
        MyCommentsListFragment fragment = new MyCommentsListFragment();
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
        initRefreshHelper(15);
    }

    @Override
    public RecyclerView.Adapter getListAdapter(List listData) {

        listData.add("ddd");
        listData.add("ddd");
        listData.add("ddd");
        listData.add("ddd");
        listData.add("ddd");
        listData.add("ddd");
        listData.add("ddd");
        listData.add("ddd");
        listData.add("ddd");
        listData.add("ddd");
        listData.add("ddd");
        listData.add("ddd");

        return new UserMyCommentListAdapter(listData);
    }

    @Override
    public void getListRequest(int pageindex, int limit, boolean isShowDialog) {

    }
}
