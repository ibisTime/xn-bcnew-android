package com.cdkj.link_community.adapters;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.model.UserMessageComment;
import com.cdkj.link_community.module.user.UserCenterBBSRepyListActivity;
import com.cdkj.link_community.module.user.UserCenterMessageRepyListActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.lang.ref.SoftReference;
import java.util.List;

/**
 * 我的评论
 * Created by cdkj on 2018/3/19.
 */

public class UserMessageCommentListAdapter extends BaseQuickAdapter<UserMessageComment, BaseViewHolder> {

    private Activity activity;
    public UserMessageCommentListAdapter(@Nullable List<UserMessageComment> data,Activity activity) {
        super(R.layout.item_user_my_comments, data);
        SoftReference<Activity> mS = new SoftReference<>(activity);
        this.activity = mS.get();
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

        viewHolder.setText(R.id.tv_name, SPUtilHelpr.getUserNickName());
        viewHolder.setText(R.id.tv_content, item.getContent());
        viewHolder.setText(R.id.tv_time, DateUtil.formatStringData(item.getCommentDatetime(), DateUtil.DEFAULT_DATE_FMT));

        if (TextUtils.isEmpty(item.getParentNickName())) {
            viewHolder.setText(R.id.tv_replay_name, "进行了评论");
        } else {
            viewHolder.setText(R.id.tv_replay_name, "回复 " + item.getParentNickName());
        }


        viewHolder.addOnClickListener(R.id.lin_msg);

        if (item.getNews() != null) {

            if (StringUtils.splitAsPicList(item.getNews().getAdvPic()).size() > 0) {
                ImgUtils.loadImg(this.activity, StringUtils.splitAsPicList(item.getNews().getAdvPic()).get(0), viewHolder.getView(R.id.img_msg));
            }
            viewHolder.setText(R.id.tv_msg_title, item.getNews().getTitle());

        }

        viewHolder.getView(R.id.img_logo).setOnClickListener(view -> {
            UserCenterMessageRepyListActivity.open(mContext, item.getUserId(), item.getNickname(), item.getPhoto());
        });
    }

}