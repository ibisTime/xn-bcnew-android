package com.cdkj.link_community.adapters;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.cdkj.baselibrary.utils.BigDecimalUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.model.PlateLlistModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
import java.util.List;

import static com.cdkj.link_community.utils.AccountUtil.getMarketShowColor;
import static com.cdkj.link_community.utils.AccountUtil.getMarketShowString;

/**
 * 热门板块
 * Created by cdkj on 2018/4/29.
 */
public class HotPlateAdapter extends BaseQuickAdapter<PlateLlistModel, BaseViewHolder> {

    public HotPlateAdapter(@Nullable List<PlateLlistModel> data) {
        super(R.layout.item_plate_hot, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PlateLlistModel item) {

        if (item == null) return;

        if (TextUtils.isEmpty(item.getName())) {

            helper.setText(R.id.tv_plate_name, "--");

        } else {
            helper.setText(R.id.tv_plate_name, item.getName());
        }


        if (TextUtils.isEmpty(item.getBestSymbol())) {

            helper.setText(R.id.tv_coin, "--");

        } else {
            helper.setText(R.id.tv_coin, item.getBestSymbol());
        }


        BigDecimal avgChange = BigDecimalUtils.multiply(item.getAvgChange(), new BigDecimal(100));

        helper.setText(R.id.tv_change, getMarketShowString(avgChange));

        helper.setText(R.id.tv_best_change, getMarketShowString(BigDecimalUtils.multiply(item.getBestChange(), new BigDecimal(100))));

        helper.setTextColor(R.id.tv_change, getMarketShowColor(item.getAvgChange()));
        helper.setTextColor(R.id.tv_best_change, getMarketShowColor(item.getBestChange()));

    }


}
