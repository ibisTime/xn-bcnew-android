package com.cdkj.link_community.adapters;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.model.MessageModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;

import java.math.BigDecimal;
import java.util.List;

import static com.cdkj.baselibrary.utils.DateUtil.DEFAULT_DATE_FMT;

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
                if (StringUtils.splitAsPicList(entity.getAdvPic()).size() >= 3) {
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
                List<String> picList = StringUtils.splitAsPicList(item.getAdvPic());

                if (picList.size() >= 3) {
                    ImgUtils.loadImg(mContext, picList.get(0), viewHolder.getView(R.id.img_1));
                    ImgUtils.loadImg(mContext, picList.get(1), viewHolder.getView(R.id.img_2));
                    ImgUtils.loadImg(mContext, picList.get(2), viewHolder.getView(R.id.img_3));
                }
                setShowData(viewHolder, item);

                break;
            default:
                if (StringUtils.splitAsPicList(item.getAdvPic()).size() > 0) {
                    ImgUtils.loadImg(mContext, StringUtils.splitAsPicList(item.getAdvPic()).get(0), viewHolder.getView(R.id.img));
                }

                setShowData(viewHolder, item);
                break;
        }

    }

    private void setShowData(BaseViewHolder viewHolder, MessageModel item) {
        viewHolder.setText(R.id.tv_msg_title, item.getTitle());
        viewHolder.setText(R.id.tv_date, DateUtil.formatStringData(item.getShowDatetime(), DEFAULT_DATE_FMT));
        viewHolder.setText(R.id.tv_collection, StringUtils.formatNum(new BigDecimal(item.getCollectCount())) + mContext.getString(R.string.collection));

    }

}