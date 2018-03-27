package com.cdkj.link_community.adapters;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

import com.cdkj.baselibrary.utils.DisplayHelper;
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

            String replyNameInfo = item.getNickname() + " 回复 " + item.getParentNickName() + ":";

            if (!TextUtils.isEmpty(item.getNickname()) && !TextUtils.isEmpty(item.getParentNickName())) {
                SpannableString ss = new SpannableString(replyNameInfo);
                ss.setSpan(new ForegroundColorSpan(Color.parseColor("#898989")), item.getNickname().length(), item.getNickname().length() + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss.setSpan(new AbsoluteSizeSpan(DisplayHelper.sp2px(mContext, 14)), item.getNickname().length(), item.getNickname().length() + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHolder.setText(R.id.tv_name, ss);
            } else {
                viewHolder.setText(R.id.tv_name, replyNameInfo);
            }

        }

        viewHolder.setText(R.id.tv_content, item.getContent());
    }

}