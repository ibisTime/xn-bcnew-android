package com.cdkj.link_community.adapters;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.cdkj.baselibrary.utils.BigDecimalUtils;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.model.ActiveModel;
import com.cdkj.link_community.model.PlateLlistModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
import java.util.List;

import static com.cdkj.link_community.utils.AccountUtil.getShowColor;
import static com.cdkj.link_community.utils.AccountUtil.getShowString;

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

        helper.setText(R.id.tv_change, getShowString(avgChange));

        helper.setText(R.id.tv_best_change, getShowString(BigDecimalUtils.multiply(item.getBestChange(), new BigDecimal(100))));

        helper.setTextColor(R.id.tv_change, getShowColor(item.getAvgChange()));
        helper.setTextColor(R.id.tv_best_change, getShowColor(item.getBestChange()));

    }


}
