package com.cdkj.link_community.adapters;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.widget.TextView;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.model.FastMessage;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 快讯适配器
 * Created by cdkj on 2018/3/19.
 */

public class FastMessageListAdapter extends BaseQuickAdapter<FastMessage, BaseViewHolder> {

    private boolean isHotMessage;//是否是热门信息 热门信息 和 全部颜色不一样

    public FastMessageListAdapter(@Nullable List<FastMessage> data, boolean isHotMessage) {
        super(R.layout.item_fast_message, data);
        this.isHotMessage = isHotMessage;
    }


    @Override
    protected void convert(BaseViewHolder viewHolder, FastMessage item) {
        if (item == null) return;

        TextView textView = viewHolder.getView(R.id.tv_content);
        //设置首行缩进

//        SpannableStringBuilder span = new SpannableStringBuilder("缩" + Html.fromHtml(item.getContent()));
//        span.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 0, 1,
//                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        textView.setText(Html.fromHtml(item.getContent()));

        String month = DateUtil.formatStringData(item.getShowDatetime(), "MM月dd日");
        String week = DateUtil.getWeekOfDate(item.getShowDatetime());

        viewHolder.setText(R.id.tv_title, month + week);
        viewHolder.setText(R.id.tv_time, DateUtil.formatStringData(item.getShowDatetime(), "HH:mm:ss"));

        viewHolder.addOnClickListener(R.id.linlayout_to_share);

        setOpenState2(item, textView);

        textView.setOnClickListener(view -> {
            if (SPUtilHelper.isLoginNoStart() && !TextUtils.equals(item.getIsRead(), "1")) {
                setIsRead(item.getCode());
                item.setIsRead("1");
                setReadedState(item, textView);
            }
            setOpenState(item, textView);
            item.setOpen(!item.isOpen());
        });

        setReadedState(item, textView);

//        viewHolder.setGone(R.id.img_date_bg, isShowDateBg(viewHolder.getLayoutPosition()));
        viewHolder.setGone(R.id.tv_title, isShowDateBg(viewHolder.getLayoutPosition()));
        //viewHolder.setGone(R.id.relayout_point, !isShowDateBg(viewHolder.getLayoutPosition()));

    }

    /**
     * 设置是否显示日期背景
     *
     * @param position
     * @return
     */
    public boolean isShowDateBg(int position) {

        if (position == 0 || mData.size() == 1) {
            return true;
        }

        int onPosition = position - 1;

        if (onPosition < 0 || mData.get(onPosition) == null || mData.get(position) == null || TextUtils.isEmpty(mData.get(onPosition).getShowDatetime()) || TextUtils.isEmpty(mData.get(position).getShowDatetime())) {
            return true;
        }

        if (DateUtil.inSameDay(new Date(mData.get(onPosition).getShowDatetime()), new Date(mData.get(position).getShowDatetime()))) {
            return false;
        }
        return true;
    }


    /**
     * 设置点击状态
     *
     * @param item
     * @param textView
     */
    private void setOpenState(FastMessage item, TextView textView) {
        if (!item.isOpen()) {
            textView.setMaxLines(Integer.MAX_VALUE);
            textView.setEllipsize(null);
        } else {
            textView.setMaxLines(3);
            textView.setEllipsize(TextUtils.TruncateAt.END);
        }
    }

    /**
     * 设置显示状态
     *
     * @param item
     * @param textView
     */
    private void setOpenState2(FastMessage item, TextView textView) {
        if (item.isOpen()) {
            textView.setMaxLines(Integer.MAX_VALUE);
            textView.setEllipsize(null);
        } else {
            textView.setMaxLines(3);
            textView.setEllipsize(TextUtils.TruncateAt.END);
        }
    }

    /**
     * 设置阅读状态
     *
     * @param item
     * @param textView
     */
    private void setReadedState(FastMessage item, TextView textView) {
        if (TextUtils.equals(item.getType(), "1")) {  //热门
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.text_red));
//            if (isHotMessage) {
//                textView.setTextColor(ContextCompat.getColor(mContext, R.color.text_red));
//            } else {
//                textView.setTextColor(ContextCompat.getColor(mContext, R.color.text_black_cd));
//            }
        } else {
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.text_black_cd));
        }
//        if (TextUtils.equals(item.getIsRead(), "1")) {
//
//            if (isHotMessage) {
//                textView.setTextColor(ContextCompat.getColor(mContext, R.color.red));
//            } else {
//
//                textView.setTextColor(ContextCompat.getColor(mContext, R.color.app_text_gray));
//            }
//        } else {
//            if (isHotMessage) {
//
//                textView.setTextColor(ContextCompat.getColor(mContext, R.color.red));
//
//            } else {
//
//                // 全部快讯里如果有热点快讯，蓝色字体显示
//                if (TextUtils.equals(item.getType(), "1")) {
//
//                    textView.setTextColor(ContextCompat.getColor(mContext, R.color.app_text_color_yellow));
//
//                } else {
//
//                    textView.setTextColor(ContextCompat.getColor(mContext, R.color.text_black_cd));
//
//                }
//            }
//        }
    }

    public void setIsRead(String code) {

        Map<String, String> map = new HashMap<>();

        map.put("code", code);
        map.put("userId", SPUtilHelper.getUserId());

        Call call = RetrofitUtils.createApi(MyApiServer.class).getFastMsgList("628094", StringUtils.getJsonToString(map));

        call.enqueue(new BaseResponseModelCallBack(null) {
            @Override
            protected void onSuccess(Object data, String SucMessage) {

            }

            @Override
            protected void onFinish() {

            }
        });
    }

}