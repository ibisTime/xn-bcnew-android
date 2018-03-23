package com.cdkj.link_community.adapters;

import android.support.annotation.Nullable;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.model.MsgDetailsComment;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.cdkj.baselibrary.utils.DateUtil.DEFAULT_DATE_FMT;

/**
 * 行情自选适配器
 * Created by cdkj on 2018/3/19.
 */

public class MsgHotCommentListAdapter extends BaseQuickAdapter<MsgDetailsComment, BaseViewHolder> {


    public MsgHotCommentListAdapter(@Nullable List<MsgDetailsComment> data) {
        super(R.layout.item_message_commen, data);

    }


    @Override
    protected void convert(BaseViewHolder viewHolder, MsgDetailsComment item) {
        if (item == null) return;

        ImgUtils.loadQiniuLogo(mContext, item.getPhoto(), viewHolder.getView(R.id.img_logo));

        viewHolder.setText(R.id.tv_name, item.getNickname());
        viewHolder.setText(R.id.tv_time, DateUtil.formatStringData(item.getCommentDatetime(), DEFAULT_DATE_FMT));
        viewHolder.setText(R.id.tv_content, item.getContent());

        if (item.getPointCount() > 999) {
            viewHolder.setText(R.id.tv_like_num, "999+");
        } else {
            viewHolder.setText(R.id.tv_like_num, item.getPointCount() + "");
        }


    }

}