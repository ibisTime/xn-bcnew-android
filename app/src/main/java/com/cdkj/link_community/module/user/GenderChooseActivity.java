package com.cdkj.link_community.module.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.link_community.R;
import com.cdkj.link_community.databinding.ActivityChooseGenderBinding;

/**
 * 性别选择
 * Created by cdkj on 2018/3/22.
 */

public class GenderChooseActivity extends AbsBaseLoadActivity {

    private ActivityChooseGenderBinding mBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, GenderChooseActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_choose_gender, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        mBinding.tvCancle.setOnClickListener(view -> finish());

    }
}
