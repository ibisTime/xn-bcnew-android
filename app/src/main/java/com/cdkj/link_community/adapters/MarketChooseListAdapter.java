package com.cdkj.link_community.adapters;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.cdkj.link_community.R;
import com.cdkj.link_community.model.MessageModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;

import java.util.List;

/**
 * 行情自选适配器
 * Created by cdkj on 2018/3/19.
 */

public class MarketChooseListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public MarketChooseListAdapter(@Nullable List<String> data) {
        super(R.layout.item_market_add_done, data);

    }


    @Override
    protected void convert(BaseViewHolder viewHolder, String item) {
        if (item == null) return;

    }

}