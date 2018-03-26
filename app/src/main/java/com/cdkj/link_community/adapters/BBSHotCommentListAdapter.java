package com.cdkj.link_community.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.model.CoinBBSHotCircular;
import com.cdkj.link_community.module.coin_bbs.BBSCommentDetailsActivity;
import com.cdkj.link_community.module.message.CommentDetailsActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.cdkj.baselibrary.utils.DateUtil.DEFAULT_DATE_FMT;

/**
 * 行情自选适配器
 * Created by cdkj on 2018/3/19.
 */

public class BBSHotCommentListAdapter extends BaseQuickAdapter<CoinBBSHotCircular, BaseViewHolder> {


    public BBSHotCommentListAdapter(@Nullable List<CoinBBSHotCircular> data) {
        super(R.layout.item_message_commen, data);

    }


    @Override
    protected void convert(BaseViewHolder viewHolder, CoinBBSHotCircular item) {
        if (item == null) return;

        if (viewHolder.getLayoutPosition() % 2 == 0) {
            viewHolder.setBackgroundColor(R.id.lin_bg, ContextCompat.getColor(mContext, R.color.item_bg_other));
        } else {
            viewHolder.setBackgroundColor(R.id.lin_bg, ContextCompat.getColor(mContext, R.color.white));
        }

        setReplyData(viewHolder, item);


        ImgUtils.loadQiniuLogo(mContext, item.getPhoto(), viewHolder.getView(R.id.img_logo));

        viewHolder.setText(R.id.tv_name, item.getNickname());
        viewHolder.setText(R.id.tv_time, DateUtil.formatStringData(item.getPublishDatetime(), DEFAULT_DATE_FMT));
        viewHolder.setText(R.id.tv_content, item.getContent());

        if (TextUtils.equals(item.getIsPoint(), "1")) {
            viewHolder.setImageResource(R.id.img_is_like, R.drawable.gave_a_like_2);
        } else {
            viewHolder.setImageResource(R.id.img_is_like, R.drawable.gave_a_like_2_un);
        }


        if (item.getPointCount() > 999) {
            viewHolder.setText(R.id.tv_like_num, "999+");
        } else {
            viewHolder.setText(R.id.tv_like_num, item.getPointCount() + "");
        }

        viewHolder.addOnClickListener(R.id.lin_like);


    }

    /**
     * 设置回复数据
     *
     * @param viewHolder
     * @param item
     */
    private void setReplyData(BaseViewHolder viewHolder, CoinBBSHotCircular item) {
        RecyclerView recyclerView = viewHolder.getView(R.id.recycler_comment);

        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.setLayoutManager(getLinearLayoutManager());

        ReplyCommentListAdapter replyCommentListAdapter = new ReplyCommentListAdapter(item.getCommentList());

        replyCommentListAdapter.setOnItemClickListener((adapter, view, position) -> {
            BBSCommentDetailsActivity.open(mContext, item.getCode());
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