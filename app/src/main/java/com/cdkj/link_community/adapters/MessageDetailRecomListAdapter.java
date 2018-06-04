package com.cdkj.link_community.adapters;

import android.app.Activity;
import android.support.annotation.Nullable;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.model.MessageDetailsNoteList;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.lang.ref.SoftReference;
import java.math.BigDecimal;
import java.util.List;

import static com.cdkj.baselibrary.utils.DateUtil.DEFAULT_DATE_FMT;

/**
 * 资讯适配器
 * Created by cdkj on 2018/3/19.
 */

public class MessageDetailRecomListAdapter extends BaseQuickAdapter<MessageDetailsNoteList, BaseViewHolder> {

    private Activity activity;

    public MessageDetailRecomListAdapter(@Nullable List<MessageDetailsNoteList> data, Activity activity) {
        super(R.layout.item_message, data);
        SoftReference<Activity> mS = new SoftReference<>(activity);
        this.activity = mS.get();

    }

    @Override
    protected void convert(BaseViewHolder viewHolder, MessageDetailsNoteList item) {
        if (item == null) return;
        if (StringUtils.splitAsPicList(item.getAdvPic()).size() > 0) {
            ImgUtils.loadImg(this.activity, StringUtils.splitAsPicList(item.getAdvPic()).get(0), viewHolder.getView(R.id.img));
        }

        setShowData(viewHolder, item);

    }

    private void setShowData(BaseViewHolder viewHolder, MessageDetailsNoteList item) {
        viewHolder.setText(R.id.tv_msg_title, item.getTitle());
        viewHolder.setText(R.id.tv_date, DateUtil.formatStringData(item.getShowDatetime(), DEFAULT_DATE_FMT));

        viewHolder.setText(R.id.tv_collection, StringUtils.formatNum(new BigDecimal(item.getReadCount())) );
    }

}