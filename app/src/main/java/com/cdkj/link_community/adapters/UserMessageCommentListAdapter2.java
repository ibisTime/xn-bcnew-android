package com.cdkj.link_community.adapters;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.model.ReplyComment;
import com.cdkj.link_community.module.user.UserCenterMessageRepyListActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
import java.util.List;

/**
 * 我的评论
 * Created by cdkj on 2018/3/19.
 */

public class UserMessageCommentListAdapter2 extends BaseQuickAdapter<ReplyComment, BaseViewHolder> {


    public UserMessageCommentListAdapter2(@Nullable List<ReplyComment> data) {
        super(R.layout.item_user_my_comments_2, data);

    }


    @Override
    protected void convert(BaseViewHolder viewHolder, ReplyComment item) {
        if (item == null) return;

        ImgUtils.loadQiniuLogo(mContext, item.getPhoto(), viewHolder.getView(R.id.img_logo));

        viewHolder.setText(R.id.tv_name, SPUtilHelper.getUserNickName());
        viewHolder.setText(R.id.tv_content, item.getContent());
        viewHolder.setText(R.id.tv_time, DateUtil.formatStringData(item.getCommentDatetime(), DateUtil.DEFAULT_DATE_FMT));

        viewHolder.setText(R.id.tv_like_num, StringUtils.formatNum(new BigDecimal(item.getPointCount())));


        viewHolder.setText(R.id.tv_replay_name, "回复  " + item.getParentNickName());
        viewHolder.setGone(R.id.tv_replay_name, !TextUtils.isEmpty(item.getParentNickName()));


        LogUtil.E("abcdd"+item.getIsPoint());

        if (TextUtils.equals(item.getIsPoint(), "1")) {
            viewHolder.setImageResource(R.id.img_is_like, R.drawable.gave_a_like_2);
        } else {
            viewHolder.setImageResource(R.id.img_is_like, R.drawable.gave_a_like_2_un);
        }

        viewHolder.addOnClickListener(R.id.lin_like);
        viewHolder.getView(R.id.img_logo).setOnClickListener(view -> {
            UserCenterMessageRepyListActivity.open(mContext, item.getUserId(), item.getNickname(), item.getPhoto());
        });
    }

}