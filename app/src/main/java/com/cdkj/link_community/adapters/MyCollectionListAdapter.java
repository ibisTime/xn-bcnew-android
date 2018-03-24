package com.cdkj.link_community.adapters;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.cdkj.link_community.R;
import com.cdkj.link_community.model.CollectionList;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 我的评论
 * Created by cdkj on 2018/3/19.
 */

public class MyCollectionListAdapter extends BaseQuickAdapter<CollectionList, BaseViewHolder> {


    public MyCollectionListAdapter(@Nullable List<CollectionList> data) {
        super(R.layout.item_collection, data);

    }


    @Override
    protected void convert(BaseViewHolder viewHolder, CollectionList item) {
        if (item == null) return;

        if (viewHolder.getLayoutPosition() / 2 == 0) {
            viewHolder.setBackgroundColor(R.id.lin_bg, ContextCompat.getColor(mContext, R.color.item_bg_other));
        } else {
            viewHolder.setBackgroundColor(R.id.lin_bg, ContextCompat.getColor(mContext, R.color.white));
        }

        viewHolder.setText(R.id.tv_title, item.getTitle());
        viewHolder.setText(R.id.tv_author, mContext.getString(R.string.author) + item.getAuther());

    }

}