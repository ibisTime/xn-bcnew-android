package com.cdkj.link_community.adapters;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.cdkj.link_community.R;
import com.cdkj.link_community.model.CoinListModel;
import com.cdkj.link_community.model.CoinPrice;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 币价格列表适配器
 * Created by cdkj on 2018/3/19.
 */

public class CoinPriceListAdapter extends BaseQuickAdapter<CoinPrice, BaseViewHolder> {

    public CoinPriceListAdapter(@Nullable List<CoinPrice> data) {
        super(R.layout.item_market_coin_price, data);

    }


    @Override
    protected void convert(BaseViewHolder viewHolder, CoinPrice item) {
        if (item == null) return;

        if (viewHolder.getLayoutPosition() % 2 == 0) {
            viewHolder.setBackgroundColor(R.id.lin_bg, ContextCompat.getColor(mContext, R.color.item_bg_other));
        } else {
            viewHolder.setBackgroundColor(R.id.lin_bg, ContextCompat.getColor(mContext, R.color.white));
        }
//
        viewHolder.setText(R.id.tv_price, mContext.getString(R.string.money_sing) + item.getPriceCny());
        viewHolder.setText(R.id.tv_coin_name, item.getSymbol());
        viewHolder.setText(R.id.tv_coin_info, "24H量/" + item.getH24VolumeCny());
//
        if (item.getPercentChange24h() == 0) {
            viewHolder.setBackgroundRes(R.id.btn_state, R.drawable.market_gray_bg);
            viewHolder.setText(R.id.btn_state, item.getPercentChange24h() + "%");
            viewHolder.setTextColor(R.id.tv_price, ContextCompat.getColor(mContext, R.color.market_gray));
        } else if (item.getPercentChange24h() > 0) {
            viewHolder.setBackgroundRes(R.id.btn_state, R.drawable.market_green_bg);
            viewHolder.setText(R.id.btn_state, "+" + item.getPercentChange24h() + "%");
            viewHolder.setTextColor(R.id.tv_price, ContextCompat.getColor(mContext, R.color.market_green));
        } else {
            viewHolder.setBackgroundRes(R.id.btn_state, R.drawable.market_red_bg);
            viewHolder.setText(R.id.btn_state, item.getPercentChange24h() + "%");
            viewHolder.setTextColor(R.id.tv_price, ContextCompat.getColor(mContext, R.color.market_red));
        }
    }

}