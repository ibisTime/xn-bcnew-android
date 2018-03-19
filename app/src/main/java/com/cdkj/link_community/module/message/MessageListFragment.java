package com.cdkj.link_community.module.message;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.cdkj.baselibrary.base.AbsRefreshListFragment;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.link_community.adapters.MessageListAdapter;
import com.cdkj.link_community.model.MessageModel;

import java.util.List;

/**
 * 资讯列表
 * Created by cdkj on 2018/3/19.
 */

public class MessageListFragment extends AbsRefreshListFragment {

    public static MessageListFragment getInstanse() {
        MessageListFragment fragment = new MessageListFragment();
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

        listData.add(new MessageModel());
        MessageModel messageModel= new MessageModel();
        messageModel.setPic("d");
        listData.add(messageModel);

        return new MessageListAdapter(listData);
    }

    @Override
    public void getListRequest(int pageindex, int limit, boolean isShowDialog) {

    }
}
