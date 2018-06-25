package com.cdkj.link_community.adapters;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.MoneyUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.model.ActiveModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by cdkj on 2018/4/29.
 */

public class ActiveListAdapter extends BaseQuickAdapter<ActiveModel, BaseViewHolder> {

    public ActiveListAdapter(@Nullable List<ActiveModel> data) {
        super(R.layout.item_active_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ActiveModel item) {

        ImgUtils.loadImg(mContext, item.getAdvPic(), helper.getView(R.id.iv_adv));

        helper.setText(R.id.tv_title, item.getTitle());

        helper.setText(R.id.tv_price, MoneyUtils.MONEYSING + item.getPrice());

        helper.setText(R.id.tv_date_time, DateUtil.formatStringData(item.getStartDatetime(), DateUtil.ACTIVE_DATE_FMT)
                + "-" + DateUtil.formatStringData(item.getEndDatetime(), DateUtil.ACTIVE_DATE_FMT));

        helper.setText(R.id.tv_location, item.getMeetAddress());
        helper.setText(R.id.tv_browse, item.getReadCount() + "");

        helper.setGone(R.id.iv_top, TextUtils.equals(item.getIsTop(), "1"));

        if (item.getStatus().equals("9")) {
            helper.setText(R.id.tv_status, "已结束");
            helper.setVisible(R.id.tv_status, true);
            helper.setBackgroundRes(R.id.tv_status, R.drawable.common_active_status_bg_yellow);
            helper.setVisible(R.id.tv_status, true);
        } else if (SPUtilHelper.isLoginNoStart()) {  //已登录

            if (TextUtils.equals(item.getIsEnroll(), "2") || TextUtils.equals(item.getIsEnroll(), "1")) {
                helper.setText(R.id.tv_status, "已报名");
                helper.setVisible(R.id.tv_status, true);
                helper.setBackgroundRes(R.id.tv_status, R.drawable.common_active_status_bg_green);
            } else {
                helper.setVisible(R.id.tv_status, false);
            }
        }


    }
}
