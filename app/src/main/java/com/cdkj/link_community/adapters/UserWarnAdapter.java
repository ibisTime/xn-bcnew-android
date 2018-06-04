package com.cdkj.link_community.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.cdkj.baselibrary.utils.MoneyUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.databinding.ItemUserWarnBinding;
import com.cdkj.link_community.model.UserWarnModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;


/**
 * Created by cdkj on 2018/4/25.
 */

public class UserWarnAdapter extends BaseQuickAdapter<UserWarnModel, BaseViewHolder> {

    public UserWarnAdapter(@Nullable List<UserWarnModel> data) {
        super(R.layout.item_user_warn, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserWarnModel item) {
        ItemUserWarnBinding mBinding = DataBindingUtil.bind(helper.itemView);

        String unit;
        if (item.getWarnCurrency().equals("CNY")){
            unit = MoneyUtils.MONEYSING;
        }else {
            unit = MoneyUtils.MONEYSING_USD;
        }

        mBinding.tvPrice.setText(unit + item.getWarnPrice());

        if (TextUtils.equals(item.getWarnDirection(), "1")){
            mBinding.tvType.setText("上涨至");
        }else {
            mBinding.tvType.setText("下跌至");
        }


        helper.addOnClickListener(R.id.iv_delete);
    }
}
