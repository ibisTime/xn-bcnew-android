package com.cdkj.link_community.adapters;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.model.MessageDetailsNoteList;
import com.cdkj.link_community.model.MessageModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;

import java.util.List;

import static com.cdkj.baselibrary.utils.DateUtil.DEFAULT_DATE_FMT;

/**
 * 资讯适配器
 * Created by cdkj on 2018/3/19.
 */

public class MessageDetailRecomListAdapter extends BaseQuickAdapter<MessageDetailsNoteList, BaseViewHolder> {

    private int THREETYPE = 1;
    private int DEFTYPE = 0; //默认布局

    public MessageDetailRecomListAdapter(@Nullable List<MessageDetailsNoteList> data) {
        super(R.layout.item_message, data);

    }

    @Override
    protected void convert(BaseViewHolder viewHolder, MessageDetailsNoteList item) {
        if (item == null) return;
        if (StringUtils.splitAsPicList(item.getAdvPic()).size() > 0) {
            ImgUtils.loadQiniuRoundImg(mContext, StringUtils.splitAsPicList(item.getAdvPic()).get(0), viewHolder.getView(R.id.img));
        }

        setShowData(viewHolder, item);

    }

    private void setShowData(BaseViewHolder viewHolder, MessageDetailsNoteList item) {
        viewHolder.setText(R.id.tv_msg_title, item.getTitle());
        viewHolder.setText(R.id.tv_date,  DateUtil.formatStringData(item.getShowDatetime(), DEFAULT_DATE_FMT));

        if (item.getCollectCount() <= 999) {
            viewHolder.setText(R.id.tv_collection, item.getCollectCount() + mContext.getString(R.string.collection));
        } else {
            viewHolder.setText(R.id.tv_collection, "999+" + mContext.getString(R.string.collection));
        }
    }

}