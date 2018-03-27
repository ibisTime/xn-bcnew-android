package com.cdkj.link_community.adapters;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.model.UserBBSComment;
import com.cdkj.link_community.model.UserMessageComment;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 币圈 资讯评论
 * Created by cdkj on 2018/3/19.
 */

public class UserCenterBBSRePlyListAdapter extends BaseQuickAdapter<UserBBSComment, BaseViewHolder> {


    public UserCenterBBSRePlyListAdapter(@Nullable List<UserBBSComment> data) {
        super(R.layout.item_user_center_reply, data);
    }


    @Override
    protected void convert(BaseViewHolder viewHolder, UserBBSComment item) {
        if (item == null) return;

        if (viewHolder.getLayoutPosition() % 2 == 0) {
            viewHolder.setBackgroundColor(R.id.lin_bg, ContextCompat.getColor(mContext, R.color.item_bg_other));
        } else {
            viewHolder.setBackgroundColor(R.id.lin_bg, ContextCompat.getColor(mContext, R.color.white));
        }

//        viewHolder.setText(R.id.tv_name, SPUtilHelpr.getUserNickName());
        viewHolder.setText(R.id.tv_content, item.getContent());
        viewHolder.setText(R.id.tv_time, DateUtil.formatStringData(item.getCommentDatetime(), DateUtil.DEFAULT_DATE_FMT));


        if (TextUtils.equals(item.getIsMyComment(), "1")) {
            viewHolder.setText(R.id.tv_replay_name, "评论了");
        } else {
            viewHolder.setText(R.id.tv_replay_name, item.getNickname() + "回复了他");
        }

        viewHolder.setGone(R.id.cardview, false);
        if (item.getPost() != null) {

            viewHolder.setText(R.id.tv_msg_title, item.getPost().getContent());

        }

    }

}