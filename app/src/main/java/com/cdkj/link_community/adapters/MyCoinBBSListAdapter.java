package com.cdkj.link_community.adapters;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.cdkj.link_community.R;
import com.cdkj.link_community.model.CoinBBSListModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 币吧 Fragment
 * Created by cdkj on 2018/3/19.
 */

public class MyCoinBBSListAdapter extends BaseQuickAdapter<CoinBBSListModel, BaseViewHolder> {


    public MyCoinBBSListAdapter(@Nullable List<CoinBBSListModel> data) {
        super(R.layout.item_coin_bbs, data);
    }


    @Override
    protected void convert(BaseViewHolder viewHolder, CoinBBSListModel item) {
        if (item == null) return;

        viewHolder.setText(R.id.tv_bbs_name, "#" + item.getName() + "#");
        viewHolder.setText(R.id.tv_focus_on, mContext.getString(R.string.focus_num) + item.getKeepCount());
        viewHolder.setText(R.id.tv_post_num, mContext.getString(R.string.post_num) + item.getPostCount());
        viewHolder.setText(R.id.tv_today_num, "" + item.getDayCommentCount());

        if (viewHolder.getLayoutPosition() % 2 == 0) {
            viewHolder.setBackgroundColor(R.id.lin_bg, ContextCompat.getColor(mContext, R.color.item_bg_other));
        } else {
            viewHolder.setBackgroundColor(R.id.lin_bg, ContextCompat.getColor(mContext, R.color.white));
        }

        if (true) {  //是否关注
            viewHolder.setText(R.id.btn_state, R.string.focus_on_done);
            viewHolder.setBackgroundRes(R.id.btn_state, R.drawable.bbs_focus_on_bg);
            viewHolder.setTextColor(R.id.btn_state, ContextCompat.getColor(mContext, R.color.white));
        } else {
            viewHolder.setText(R.id.btn_state, R.string.focus_on);
            viewHolder.setBackgroundRes(R.id.btn_state, R.drawable.bbs_focus_on_bg_un);
            viewHolder.setTextColor(R.id.btn_state, ContextCompat.getColor(mContext, R.color.bbs_state_color));
        }

        viewHolder.addOnClickListener(R.id.btn_state);
    }

}