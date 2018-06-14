package com.cdkj.link_community.adapters;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.cdkj.baselibrary.utils.BigDecimalUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.model.PlateLlistModel;
import com.cdkj.link_community.utils.AccountUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
import java.util.List;

/**
 * 热门板块
 * Created by cdkj on 2018/4/29.
 */

public class AllPlateAdapter extends BaseQuickAdapter<PlateLlistModel, BaseViewHolder> {

    public AllPlateAdapter(@Nullable List<PlateLlistModel> data) {
        super(R.layout.item_plate, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PlateLlistModel item) {

        if (item == null) return;

        if (TextUtils.isEmpty(item.getName())) {

            helper.setText(R.id.tv_name, "--");

        } else {
            helper.setText(R.id.tv_name, item.getName());
        }


        if (TextUtils.isEmpty(item.getBestSymbol())) {

            helper.setText(R.id.tv_best_name, "--");

        } else {
            helper.setText(R.id.tv_best_name, item.getBestSymbol());
        }
        if (TextUtils.isEmpty(item.getWorstSymbol())) {
            helper.setText(R.id.tv_worst_name, "--");

        } else {
            helper.setText(R.id.tv_worst_name, item.getWorstSymbol());
        }


        helper.setText(R.id.tv_best_change, AccountUtil.getShowString(BigDecimalUtils.multiply(item.getBestChange(), new BigDecimal(100))));
        helper.setText(R.id.tv_worst_change, AccountUtil.getShowString(BigDecimalUtils.multiply(item.getWorstChange(), new BigDecimal(100))));
        helper.setText(R.id.btn_state, AccountUtil.getShowString(BigDecimalUtils.multiply(item.getAvgChange(), new BigDecimal(100))));

        helper.setTextColor(R.id.tv_best_change, AccountUtil.getShowColor(item.getBestChange()));
        helper.setTextColor(R.id.tv_worst_change, AccountUtil.getShowColor(item.getWorstChange()));

        helper.setBackgroundRes(R.id.btn_state, AccountUtil.getShowBtnBg(item.getAvgChange()));


    }
}
