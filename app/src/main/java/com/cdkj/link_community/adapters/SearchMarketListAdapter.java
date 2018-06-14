package com.cdkj.link_community.adapters;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.cdkj.link_community.R;
import com.cdkj.link_community.model.CoinListModel;
import com.cdkj.link_community.utils.AccountUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.cdkj.link_community.utils.AccountUtil.formatPercent;

/**
 * 行情搜索 列表适配器
 * Created by cdkj on 2018/3/19.
 */

public class SearchMarketListAdapter extends BaseQuickAdapter<CoinListModel, BaseViewHolder> {

    public SearchMarketListAdapter(@Nullable List<CoinListModel> data) {
        super(R.layout.item_market_search, data);

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
        viewHolder.setText(R.id.tv_coin_name, item.getSymbol() + "/" + item.getToSymbol());
        viewHolder.setText(R.id.tv_price, mContext.getString(R.string.money_sing) + AccountUtil.scale(item.getLastCnyPrice(),2));
        viewHolder.setText(R.id.tv_price_2, item.getLastPrice());

        if (TextUtils.equals(item.getIsChoice(), "1")) {
            viewHolder.setImageResource(R.id.img_add_state, R.drawable.add_market_done);
        } else {
            viewHolder.setImageResource(R.id.img_add_state, R.drawable.add_unmarket_done);
        }

        if (item.getPercentChange() != null){
            double rate = Double.parseDouble(item.getPercentChange());
            viewHolder.setText(R.id.btn_state, formatPercent(rate*100) + "%");
            if (rate == 0) {
                viewHolder.setBackgroundRes(R.id.btn_state, R.drawable.market_gray_bg);
                viewHolder.setTextColor(R.id.tv_price, ContextCompat.getColor(mContext, R.color.market_gray));
            } else if (rate >= 0) {
                viewHolder.setText(R.id.btn_state, "+"+formatPercent(rate*100) + "%");
                viewHolder.setBackgroundRes(R.id.btn_state, R.drawable.market_green_bg);
                viewHolder.setTextColor(R.id.tv_price, ContextCompat.getColor(mContext, R.color.market_green));
            } else {
                viewHolder.setBackgroundRes(R.id.btn_state, R.drawable.market_red_bg);
                viewHolder.setTextColor(R.id.tv_price, ContextCompat.getColor(mContext, R.color.market_red));
            }
        }
    }

}