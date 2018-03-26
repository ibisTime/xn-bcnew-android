package com.cdkj.link_community.adapters;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.model.ReplyComment;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 我的评论
 * Created by cdkj on 2018/3/19.
 */

public class UserMyCommentListAdapter2 extends BaseQuickAdapter<ReplyComment, BaseViewHolder> {


    public UserMyCommentListAdapter2(@Nullable List<ReplyComment> data) {
        super(R.layout.item_user_my_comments_2, data);

    }


    @Override
    protected void convert(BaseViewHolder viewHolder, ReplyComment item) {
        if (item == null) return;

        ImgUtils.loadQiniuLogo(mContext, item.getPhoto(), viewHolder.getView(R.id.img_logo));

        viewHolder.setText(R.id.tv_name, SPUtilHelpr.getUserNickName());
        viewHolder.setText(R.id.tv_content, item.getContent());
        viewHolder.setText(R.id.tv_replay_name, "回复  " + item.getParentNickName());
        viewHolder.setText(R.id.tv_time, DateUtil.formatStringData(item.getCommentDatetime(), DateUtil.DEFAULT_DATE_FMT));

        if (item.getPointCount() > 999) {
            viewHolder.setText(R.id.tv_like_num, "999+");
        } else {
            viewHolder.setText(R.id.tv_like_num, item.getPointCount()+"");
        }

        if (TextUtils.equals(item.getIsPoint(), "1")) {
            viewHolder.setImageResource(R.id.img_is_like, R.drawable.gave_a_like_2);
        } else {
            viewHolder.setImageResource(R.id.img_is_like, R.drawable.gave_a_like_2_un);
        }

        viewHolder.addOnClickListener(R.id.lin_like);

    }

}