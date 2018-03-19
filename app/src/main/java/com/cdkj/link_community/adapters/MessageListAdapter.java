package com.cdkj.link_community.adapters;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.cdkj.link_community.R;
import com.cdkj.link_community.model.MessageModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;

import java.util.List;

/**
 * 资讯适配器
 * Created by cdkj on 2018/3/19.
 */

public class MessageListAdapter extends BaseQuickAdapter<MessageModel, BaseViewHolder> {

    private int THREETYPE = 1;
    private int DEFTYPE = 0; //默认布局

    public MessageListAdapter(@Nullable List<MessageModel> data) {
        super(R.layout.item_message, data);

        //Step.1
        setMultiTypeDelegate(new MultiTypeDelegate<MessageModel>() {
            @Override
            protected int getItemType(MessageModel entity) {
                //根据你的实体类来判断布局类型
                if (TextUtils.isEmpty(entity.getPic())) {
                    return THREETYPE;
                }
                return DEFTYPE;
            }
        });
        //Step.2
        getMultiTypeDelegate()
                .registerItemType(DEFTYPE, R.layout.item_message)                 //默认布局
                .registerItemType(THREETYPE, R.layout.item_message_three_pic);   //三张图片布局

    }


    @Override
    protected void convert(BaseViewHolder viewHolder, MessageModel item) {
        if (item == null) return;

        //Step.3
        switch (viewHolder.getItemViewType()) {
            case 1:

                break;
            default:

                break;
        }

    }

}