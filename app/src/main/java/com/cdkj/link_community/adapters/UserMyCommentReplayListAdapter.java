package com.cdkj.link_community.adapters;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.model.UserMessageComment;
import com.cdkj.link_community.module.user.UserCenterMessageRepyListActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 我的评论 (回复我的)
 * Created by cdkj on 2018/3/19.
 */

public class UserMyCommentReplayListAdapter extends BaseQuickAdapter<UserMessageComment, BaseViewHolder> {


    public UserMyCommentReplayListAdapter(@Nullable List<UserMessageComment> data) {
        super(R.layout.item_user_my_comments, data);

    }


    @Override
    protected void convert(BaseViewHolder viewHolder, UserMessageComment item) {
        if (item == null) return;

        if (viewHolder.getLayoutPosition() % 2 == 0) {
            viewHolder.setBackgroundColor(R.id.lin_bg, ContextCompat.getColor(mContext, R.color.item_bg_other));
        } else {
            viewHolder.setBackgroundColor(R.id.lin_bg, ContextCompat.getColor(mContext, R.color.white));
        }

        ImgUtils.loadQiniuLogo(mContext, item.getPhoto(), viewHolder.getView(R.id.img_logo));

        viewHolder.setText(R.id.tv_name, item.getNickname());
        viewHolder.setText(R.id.tv_content, item.getContent());
        viewHolder.setText(R.id.tv_time, DateUtil.formatStringData(item.getCommentDatetime(), DateUtil.DEFAULT_DATE_FMT));
        viewHolder.addOnClickListener(R.id.lin_msg);
        if (item.getNews() != null) {

            if (StringUtils.splitAsPicList(item.getNews().getAdvPic()).size() > 0) {
                ImgUtils.loadImg(mContext, StringUtils.splitAsPicList(item.getNews().getAdvPic()).get(0), viewHolder.getView(R.id.img_msg));
            }
            viewHolder.setText(R.id.tv_msg_title, item.getNews().getTitle());

        }

        viewHolder.getView(R.id.img_logo).setOnClickListener(view -> {
            UserCenterMessageRepyListActivity.open(mContext, item.getUserId(), item.getNickname(), item.getPhoto());
        });

    }

}