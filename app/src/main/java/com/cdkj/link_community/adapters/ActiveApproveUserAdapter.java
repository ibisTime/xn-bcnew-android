package com.cdkj.link_community.adapters;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.model.ActiveUserModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by cdkj on 2018/6/2.
 */

public class ActiveApproveUserAdapter extends BaseQuickAdapter<ActiveUserModel, BaseViewHolder>{

    public ActiveApproveUserAdapter(@Nullable List<ActiveUserModel> data) {
        super(R.layout.item_active_approve_user,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ActiveUserModel item) {

        ImageView ivAvatar = helper.getView(R.id.iv_avatar);
        ImgUtils.loadQiniuLogo(mContext, item.getPhoto(), ivAvatar);

        switch (item.getStatus()){

            case "1":
                helper.setText(R.id.tv_status, "已通过");
                break;

            case "2":
                helper.setText(R.id.tv_status, "不通过");
                break;

            case "0":
                helper.setText(R.id.tv_status, "未审核");
                break;

        }

    }
}
