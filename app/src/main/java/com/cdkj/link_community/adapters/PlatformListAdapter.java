package com.cdkj.link_community.adapters;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.cdkj.link_community.R;
import com.cdkj.link_community.model.CoinListModel;
import com.cdkj.link_community.utils.AccountUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.cdkj.link_community.utils.AccountUtil.formatPercent;

/**
 * 平台列表适配器
 * Created by cdkj on 2018/3/19.
 */

public class PlatformListAdapter extends BaseQuickAdapter<CoinListModel, BaseViewHolder> {

    public PlatformListAdapter(@Nullable List<CoinListModel> data) {
        super(R.layout.item_market_platform, data);
    }

    public void setData(List<CoinListModel> data){
        mData = data;
    }


    @Override
    protected void convert(BaseViewHolder viewHolder, CoinListModel item) {
        if (item == null) return;

        if (viewHolder.getLayoutPosition() % 2 == 0) {
            viewHolder.setBackgroundColor(R.id.lin_bg, ContextCompat.getColor(mContext, R.color.item_bg_other));
        } else {
            viewHolder.setBackgroundColor(R.id.lin_bg, ContextCompat.getColor(mContext, R.color.white));
        }

        viewHolder.setText(R.id.tv_platform, item.getSymbol());
        viewHolder.setText(R.id.tv_coin_num, item.getToSymbol() + "量:" + AccountUtil.scale(item.getVolume(), 6));

        viewHolder.setText(R.id.tv_price, mContext.getString(R.string.money_sing) + AccountUtil.scale(item.getLastCnyPrice(),2));

        viewHolder.setText(R.id.tv_price_2, item.getLastPrice());

        Log.e("item.getIsWarn()",item.getIsWarn());
        Log.e("item.getSymbol()",item.getSymbol());

        viewHolder.setGone(R.id.iv_warn, item.getIsWarn().equals("1"));

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