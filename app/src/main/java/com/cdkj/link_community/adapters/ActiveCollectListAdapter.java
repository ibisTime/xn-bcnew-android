package com.cdkj.link_community.adapters;

import android.support.annotation.Nullable;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.model.ActiveModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by cdkj on 2018/4/29.
 */

public class ActiveCollectListAdapter extends BaseQuickAdapter<ActiveModel, BaseViewHolder> {

    public ActiveCollectListAdapter(@Nullable List<ActiveModel> data) {
        super(R.layout.item_my_active_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ActiveModel item) {

        ImgUtils.loadImg(mContext, item.getAdvPic(), helper.getView(R.id.iv_adv));

        helper.setText(R.id.tv_title, item.getTitle());

//        if (item.getPrice() == 0){
//            helper.setText(R.id.tv_price, "免费");
//        }else {
//            helper.setText(R.id.tv_price,MONEY_SIGN + AccountUtil.moneyFormat(item.getPrice()));
//        }
        helper.setText(R.id.tv_price,item.getPrice());

        helper.setText(R.id.tv_date_time, DateUtil.formatStringData(item.getStartDatetime(), DateUtil.ACTIVE_DATE_FMT)
                + "-" + DateUtil.formatStringData(item.getEndDatetime(), DateUtil.ACTIVE_DATE_FMT));

        helper.setText(R.id.tv_location, item.getAddress());
        helper.setText(R.id.tv_browse, item.getReadCount()+"");

    }
}
