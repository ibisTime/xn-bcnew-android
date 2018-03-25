package com.cdkj.link_community.adapters;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.cdkj.link_community.R;
import com.cdkj.link_community.model.CoinListModel;
import com.cdkj.link_community.model.MessageModel;
import com.cdkj.link_community.model.MyChooseMarket;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;

import java.util.List;

/**
 * 行情自选适配器
 * Created by cdkj on 2018/3/19.
 */

public class MarketChooseListAdapter extends BaseQuickAdapter<MyChooseMarket, BaseViewHolder> {


    public MarketChooseListAdapter(@Nullable List<MyChooseMarket> data) {
        super(R.layout.item_market_add_done, data);

    }


    @Override
    protected void convert(BaseViewHolder viewHolder, MyChooseMarket item) {
        if (item == null || item.getMarketFxh() == null) return;

        if (viewHolder.getLayoutPosition() % 2 == 0) {
            viewHolder.setBackgroundColor(R.id.content, ContextCompat.getColor(mContext, R.color.item_bg_other));
        } else {
            viewHolder.setBackgroundColor(R.id.content, ContextCompat.getColor(mContext, R.color.white));
        }

        viewHolder.setText(R.id.tv_name, item.getMarketFxh().getCoinSymbol());
        viewHolder.setText(R.id.tv_name_2, item.getMarketFxh().getExchangeEname());

        viewHolder.setText(R.id.tv_coin_num, item.getMarketFxh().getToCoinSymbol() + "量:" + item.getMarketFxh().getVolume());

        viewHolder.setText(R.id.tv_price, mContext.getString(R.string.money_sing) + item.getMarketFxh().getLastCnyPrice());
        viewHolder.setText(R.id.tv_price_2, item.getMarketFxh().getLastPrice());


        if (item.getMarketFxh().getChangeRate() == 0) {
            viewHolder.setBackgroundRes(R.id.btn_state, R.drawable.market_gray_bg);
            viewHolder.setText(R.id.btn_state, item.getMarketFxh().getChangeRate() + "%");
            viewHolder.setTextColor(R.id.tv_price, ContextCompat.getColor(mContext, R.color.market_gray));
        } else if (item.getMarketFxh().getChangeRate() > 0) {
            viewHolder.setBackgroundRes(R.id.btn_state, R.drawable.market_green_bg);
            viewHolder.setText(R.id.btn_state, "+" + item.getMarketFxh().getChangeRate() + "%");
            viewHolder.setTextColor(R.id.tv_price, ContextCompat.getColor(mContext, R.color.market_green));
        } else {
            viewHolder.setBackgroundRes(R.id.btn_state, R.drawable.market_red_bg);
            viewHolder.setText(R.id.btn_state, item.getMarketFxh().getChangeRate() + "%");
            viewHolder.setTextColor(R.id.tv_price, ContextCompat.getColor(mContext, R.color.market_red));
        }

        viewHolder.addOnClickListener(R.id.btn_to_top);
        viewHolder.addOnClickListener(R.id.btn_delete);

    }

}