package com.cdkj.link_community.adapters;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.cdkj.link_community.R;
import com.cdkj.link_community.model.CoinListModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 平台列表适配器
 * Created by cdkj on 2018/3/19.
 */

public class PlatformListAdapter extends BaseQuickAdapter<CoinListModel, BaseViewHolder> {

    public PlatformListAdapter(@Nullable List<CoinListModel> data) {
        super(R.layout.item_market_platform, data);
    }


    @Override
    protected void convert(BaseViewHolder viewHolder, CoinListModel item) {
        if (item == null) return;

        if (viewHolder.getLayoutPosition() % 2 == 0) {
            viewHolder.setBackgroundColor(R.id.lin_bg, ContextCompat.getColor(mContext, R.color.item_bg_other));
        } else {
            viewHolder.setBackgroundColor(R.id.lin_bg, ContextCompat.getColor(mContext, R.color.white));
        }

        viewHolder.setText(R.id.tv_platform, item.getExchangeCname());
        viewHolder.setText(R.id.tv_coin_num, item.getCoinSymbol() + "量:" + item.getVolume());
        viewHolder.setText(R.id.tv_price, mContext.getString(R.string.money_sing) + item.getLastCnyPrice());
        viewHolder.setText(R.id.tv_price_2, item.getLastPrice());

        if (item.getChangeRate() == 0) {
            viewHolder.setBackgroundRes(R.id.btn_state, R.drawable.market_gray_bg);
            viewHolder.setText(R.id.btn_state, item.getChangeRate() + "%");
        } else if (item.getChangeRate() > 0) {
            viewHolder.setBackgroundRes(R.id.btn_state, R.drawable.market_green_bg);
            viewHolder.setText(R.id.btn_state, "+" + item.getChangeRate() + "%");
        } else {
            viewHolder.setBackgroundRes(R.id.btn_state, R.drawable.market_red_bg);
            viewHolder.setText(R.id.btn_state, item.getChangeRate() + "%");
        }
    }

}