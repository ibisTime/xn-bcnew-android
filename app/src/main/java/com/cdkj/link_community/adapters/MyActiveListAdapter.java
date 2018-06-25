package com.cdkj.link_community.adapters;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.utils.BigDecimalUtils;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.MoneyUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.model.ActiveModel;
import com.cdkj.link_community.model.MyActiveModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by cdkj on 2018/4/29.
 */
public class MyActiveListAdapter extends BaseQuickAdapter<MyActiveModel, BaseViewHolder> {

    public MyActiveListAdapter(@Nullable List<MyActiveModel> data) {
        super(R.layout.item_my_active_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyActiveModel item) {


//        if (item.getActivity().getPrice() == 0){
//            helper.setText(R.id.tv_price, "免费");
//        }else {
//            helper.setText(R.id.tv_price,MONEY_SIGN + AccountUtil.moneyFormat(item.getActivity().getPrice()));
//        }
        ActiveModel activeModel = item.getActivity();

        if (activeModel != null) {
            ImgUtils.loadImg(mContext, activeModel.getAdvPic(), helper.getView(R.id.iv_adv));

            helper.setText(R.id.tv_title, activeModel.getTitle());


            if (TextUtils.isEmpty(activeModel.getPrice()) || TextUtils.equals("免费", activeModel.getPrice()) || !BigDecimalUtils.compareToZERO(activeModel.getPrice())) {
                helper.setText(R.id.tv_price, "免费");
            } else {
                helper.setText(R.id.tv_price, MoneyUtils.MONEYSING + activeModel.getPrice());
            }

            helper.setText(R.id.tv_date_time, DateUtil.formatStringData(item.getActivity().getStartDatetime(), DateUtil.ACTIVE_DATE_FMT)
                    + "-" + DateUtil.formatStringData(item.getActivity().getEndDatetime(), DateUtil.ACTIVE_DATE_FMT));

            helper.setText(R.id.tv_location, item.getActivity().getMeetAddress());
            if (item.getActivity().getReadCount() > 10000) {
                helper.setText(R.id.tv_browse, "9999");
            } else {
                helper.setText(R.id.tv_browse, item.getActivity().getReadCount() + "");
            }

            helper.setGone(R.id.view_gray, TextUtils.equals("9", activeModel.getStatus())); //已结束显示灰色图片
            helper.setGone(R.id.tv_is_finished, TextUtils.equals("9", activeModel.getStatus())); //已结束显示灰色图片

        }

//        if (TextUtils.equals("0", item.getStatus()) || TextUtils.equals("1", item.getStatus())) {
//            helper.setText(R.id.tv_state, "已报名");
//            helper.setImageResource(R.id.img_state, R.drawable.activity_doing);
//        } else if (TextUtils.equals("9", item.getStatus())) {
//            helper.setText(R.id.tv_state, "已结束");
//            helper.setImageResource(R.id.img_state, R.drawable.activity_finished);
//        }


    }
}
