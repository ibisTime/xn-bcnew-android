package com.cdkj.link_community.adapters;

import android.support.annotation.Nullable;

import com.cdkj.link_community.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 我的评论
 * Created by cdkj on 2018/3/19.
 */

public class UserMyCommentListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public UserMyCommentListAdapter(@Nullable List<String> data) {
        super(R.layout.item_user_my_comments, data);

    }


    @Override
    protected void convert(BaseViewHolder viewHolder, String item) {
        if (item == null) return;

    }

}