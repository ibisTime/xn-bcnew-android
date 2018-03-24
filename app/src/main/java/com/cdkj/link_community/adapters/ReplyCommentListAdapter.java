package com.cdkj.link_community.adapters;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.cdkj.link_community.R;
import com.cdkj.link_community.model.ReplyComment;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 评论回复
 * Created by cdkj on 2018/3/19.
 */

public class ReplyCommentListAdapter extends BaseQuickAdapter<ReplyComment, BaseViewHolder> {


    public ReplyCommentListAdapter(@Nullable List<ReplyComment> data) {
        super(R.layout.item_message_reply_commen, data);
    }


    @Override
    protected void convert(BaseViewHolder viewHolder, ReplyComment item) {
        if (item == null) return;

        // 1 顶级，0非顶级

        if (TextUtils.equals(item.getIsTop(), "1")) {
            viewHolder.setText(R.id.tv_name, item.getNickname() + ":");
        } else {
            viewHolder.setText(R.id.tv_name, item.getNickname() + " 回复 " + item.getParentNickName() + ":");
        }

        viewHolder.setText(R.id.tv_content, item.getContent());
    }

}