package com.cdkj.link_community.adapters;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.model.UserBBSComment;
import com.cdkj.link_community.module.user.UserCenterBBSRepyListActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 我的币圈评论
 * Created by cdkj on 2018/3/19.
 */

public class UserBBSCommentListAdapter2 extends BaseQuickAdapter<UserBBSComment, BaseViewHolder> {


    public UserBBSCommentListAdapter2(@Nullable List<UserBBSComment> data) {
        super(R.layout.item_user_bbs_comments, data);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, UserBBSComment item) {
        if (item == null) return;

        if (viewHolder.getLayoutPosition() % 2 == 0) {
            viewHolder.setBackgroundColor(R.id.lin_bg, ContextCompat.getColor(mContext, R.color.item_bg_other));
        } else {
            viewHolder.setBackgroundColor(R.id.lin_bg, ContextCompat.getColor(mContext, R.color.white));
        }

        ImgUtils.loadQiniuLogo(mContext, item.getPhoto(), viewHolder.getView(R.id.img_logo));

        viewHolder.setText(R.id.tv_name, SPUtilHelpr.getUserNickName());
        viewHolder.setText(R.id.tv_content, item.getContent());
        viewHolder.setText(R.id.tv_replay_name, "我 回复 " + item.getParentNickName());
        viewHolder.setText(R.id.tv_time, DateUtil.formatStringData(item.getCommentDatetime(), DateUtil.DEFAULT_DATE_FMT));


        if (item.getPost() != null) {
            viewHolder.setText(R.id.tv_msg_title, item.getPost().getContent());
        }


        viewHolder.getView(R.id.img_logo).setOnClickListener(view -> {
            UserCenterBBSRepyListActivity.open(mContext, item.getUserId(), item.getParentNickName(), item.getPhoto());
        });

    }

}