package com.cdkj.link_community.adapters;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.cdkj.link_community.R;
import com.cdkj.link_community.model.CoinListModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 行情自选适配器
 * Created by cdkj on 2018/3/19.
 */

public class AddMarketListAdapter extends BaseQuickAdapter<CoinListModel, BaseViewHolder> {


    public String type;  //平台类型 1币种 0平台

    public AddMarketListAdapter(@Nullable List<CoinListModel> data, String type) {
        super(R.layout.item_market_add, data);
        this.type = type;
    }


    @Override
    protected void convert(BaseViewHolder viewHolder, CoinListModel item) {
        if (item == null) return;

        if (TextUtils.equals(type, "1")) {
            viewHolder.setText(R.id.tv_name, item.getExchangeCname());
        } else {
            viewHolder.setText(R.id.tv_name, item.getCoinSymbol());
        }

        viewHolder.setText(R.id.tv_name_2, item.getToCoinSymbol());

        if (TextUtils.equals(item.getIsChoice(), "1")) {
            viewHolder.setImageResource(R.id.img_add_state, R.drawable.add_market_done);
        } else {
            viewHolder.setImageResource(R.id.img_add_state, R.drawable.add_market_big);
        }

    }

}