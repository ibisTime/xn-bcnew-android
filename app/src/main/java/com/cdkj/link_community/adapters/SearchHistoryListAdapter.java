package com.cdkj.link_community.adapters;

import android.support.annotation.Nullable;

import com.cdkj.link_community.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 行情自选适配器
 * Created by cdkj on 2018/3/19.
 */

public class SearchHistoryListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public SearchHistoryListAdapter(@Nullable List<String> data) {
        super(R.layout.item_search_history, data);

    }


    @Override
    protected void convert(BaseViewHolder viewHolder, String item) {
        if (item == null) return;

        viewHolder.setText(R.id.tv_search, item);
    }

}