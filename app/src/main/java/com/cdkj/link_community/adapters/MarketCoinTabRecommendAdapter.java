package com.cdkj.link_community.adapters;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.cdkj.link_community.R;
import com.cdkj.link_community.model.MarketCoinRecommendTab;
import com.cdkj.link_community.model.MarketCoinTab;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by cdkj on 2018/5/16.
 */

public class MarketCoinTabRecommendAdapter extends BaseQuickAdapter<MarketCoinRecommendTab, BaseViewHolder> {

    private List<MarketCoinTab> marketCoinTabList;

    public MarketCoinTabRecommendAdapter(@Nullable List<MarketCoinRecommendTab> data, List<MarketCoinTab> marketCoinTabList) {
        super(R.layout.item_market_coin_tab, data);

        this.marketCoinTabList = marketCoinTabList;
    }

    @Override
    protected void convert(BaseViewHolder helper, MarketCoinRecommendTab item) {

        helper.setGone(R.id.iv_selected, false);
        helper.setBackgroundRes(R.id.tv_coin, R.drawable.common_market_coin_tab_bg);
        helper.setTextColor(R.id.tv_coin, mContext.getResources().getColor(R.color.text_black_cd));
        for (MarketCoinTab marketCoinTab : marketCoinTabList) {
            if (TextUtils.equals(item.getSymbol(), marketCoinTab.getNavName())) {
                helper.setGone(R.id.iv_selected, true);
                helper.setBackgroundRes(R.id.tv_coin, R.drawable.common_market_coin_tab_bg_blue);
                helper.setTextColor(R.id.tv_coin, mContext.getResources().getColor(R.color.yellow));
            }
        }

        helper.setText(R.id.tv_coin, item.getSymbol());

        helper.addOnClickListener(R.id.tv_coin);
    }
}
