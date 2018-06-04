package com.cdkj.link_community.adapters;

import android.support.annotation.Nullable;

import com.cdkj.link_community.R;
import com.cdkj.link_community.model.MarketCoinTab;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by cdkj on 2018/5/16.
 */

public class MarketCoinTabAdapter extends BaseQuickAdapter<MarketCoinTab, BaseViewHolder> {

    private boolean is;

    public MarketCoinTabAdapter(@Nullable List<MarketCoinTab> data, boolean is) {
        super(R.layout.item_market_coin_tab, data);

        this.is = is;
    }

    @Override
    protected void convert(BaseViewHolder helper, MarketCoinTab item) {

        if (is){
            if (helper.getLayoutPosition() == 0) {
                helper.setBackgroundRes(R.id.tv_coin, R.color.white);
            }else {
                helper.setBackgroundRes(R.id.tv_coin, R.drawable.common_market_coin_tab_bg);
            }
        }


        helper.setText(R.id.tv_coin, item.getNavName());

        helper.addOnClickListener(R.id.tv_coin);

    }
}
