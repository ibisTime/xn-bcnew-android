package com.cdkj.link_community.adapters;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.cdkj.baselibrary.utils.BigDecimalUtils;
import com.cdkj.baselibrary.utils.BitmapUtils;
import com.cdkj.baselibrary.utils.MoneyUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.model.PlateDetailsModel;
import com.cdkj.link_community.utils.AccountUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by cdkj on 2018/4/29.
 */
public class PlateDetailAdapter extends BaseQuickAdapter<PlateDetailsModel.ListBean, BaseViewHolder> {

    public PlateDetailAdapter(@Nullable List<PlateDetailsModel.ListBean> data) {
        super(R.layout.item_plate_details, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PlateDetailsModel.ListBean item) {

        if (item == null) return;

        if (TextUtils.isEmpty(item.getSymbol())) {

            helper.setText(R.id.tv_name, "--");

        } else {
            helper.setText(R.id.tv_name, item.getSymbol());
        }


        helper.setText(R.id.tv_count, "量:" + StringUtils.formatNum(item.getAmount()));

        helper.setText(R.id.tv_price, MoneyUtils.MONEYSING + StringUtils.formatNum2(item.getLastCnyPrice()));

        if (TextUtils.isEmpty(item.getToSymbol())) {
            helper.setText(R.id.tv_other_price, "--" + StringUtils.formatNum2(item.getLastPrice()));
        } else {
            helper.setText(R.id.tv_other_price, MoneyUtils.MONEYSING + StringUtils.formatNum2(item.getLastPrice()));
        }

        helper.setTextColor(R.id.tv_price, AccountUtil.getShowColor(item.getLastCnyPrice()));

        helper.setText(R.id.btn_state, AccountUtil.getShowString(BigDecimalUtils.multiply(new BigDecimal(100), item.getPercentChange24h())));


        helper.setBackgroundRes(R.id.btn_state, AccountUtil.getShowBtnBg(item.getPercentChange24h()));

    }


}
