package com.cdkj.link_community.module.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.link_community.R;
import com.cdkj.link_community.databinding.ActivityChooseGenderBinding;
import com.cdkj.link_community.model.GenderUpdateModel;

import org.greenrobot.eventbus.EventBus;

/**
 * 性别选择
 * Created by cdkj on 2018/3/22.
 */

public class GenderChooseActivity extends AbsBaseLoadActivity {


    public static String MAN = "1";
    public static String WOMAN = "2";

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


        mBinding.tvMan.setOnClickListener(view -> chooseGender(MAN));

        mBinding.tvWoman.setOnClickListener(view -> chooseGender(WOMAN));

    }

    /**
     * @param gender
     */
    public void chooseGender(String gender) {
        GenderUpdateModel genderUpdateModel = new GenderUpdateModel();
        genderUpdateModel.setGender(gender);
        EventBus.getDefault().post(genderUpdateModel);
        finish();
    }

}
