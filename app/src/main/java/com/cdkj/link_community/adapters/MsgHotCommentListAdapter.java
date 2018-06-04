package com.cdkj.link_community.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.model.MsgDetailsComment;
import com.cdkj.link_community.module.message.MessageCommentDetailsActivity;
import com.cdkj.link_community.module.user.UserCenterMessageRepyListActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
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

        setReplyData(viewHolder, item);


        ImgUtils.loadQiniuLogo(mContext, item.getPhoto(), viewHolder.getView(R.id.img_logo));

        viewHolder.setText(R.id.tv_name, item.getNickname());
        viewHolder.setText(R.id.tv_time, DateUtil.formatStringData(item.getCommentDatetime(), DEFAULT_DATE_FMT));
        viewHolder.setText(R.id.tv_content, item.getContent());

        if (item.getIsPoint() == 1) {
            viewHolder.setImageResource(R.id.img_is_like, R.drawable.gave_a_like_2);
        } else {
            viewHolder.setImageResource(R.id.img_is_like, R.drawable.gave_a_like_2_un);
        }


        viewHolder.setText(R.id.tv_like_num, StringUtils.formatNum(new BigDecimal(item.getPointCount())));

        viewHolder.addOnClickListener(R.id.lin_like);

        viewHolder.getView(R.id.img_logo).setOnClickListener(view -> {
            UserCenterMessageRepyListActivity.open(mContext, item.getUserId(), item.getNickname(), item.getPhoto());
        });

    }

    /**
     * 设置回复数据
     *
     * @param viewHolder
     * @param item
     */
    private void setReplyData(BaseViewHolder viewHolder, MsgDetailsComment item) {
        RecyclerView recyclerView = viewHolder.getView(R.id.recycler_comment);

        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.setLayoutManager(getLinearLayoutManager());

        ReplyCommentListAdapter replyCommentListAdapter = new ReplyCommentListAdapter(item.getCommentList());

        replyCommentListAdapter.setOnItemClickListener((adapter, view, position) -> {
            MessageCommentDetailsActivity.open(mContext, item.getCode(), false);
        });

        recyclerView.setAdapter(replyCommentListAdapter);

        viewHolder.setGone(R.id.recycler_comment, !replyCommentListAdapter.getData().isEmpty());
    }


    @NonNull
    private LinearLayoutManager getLinearLayoutManager() {
        return new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {  //禁止自滚动
                return false;
            }
        };
    }


}