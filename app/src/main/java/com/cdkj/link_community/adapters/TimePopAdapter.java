package com.cdkj.link_community.adapters;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author qi
 * @updateDts 2018/6/14
 */

public class TimePopAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public TimePopAdapter(@Nullable List<String> data) {
        super(data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        TextView tv=new TextView(mContext);
        tv.setText(item);
    }
}
