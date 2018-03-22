package com.cdkj.link_community.module.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.link_community.R;
import com.cdkj.link_community.databinding.ActivityNicknameUpdateBinding;
import com.cdkj.link_community.manager.MyRouteHelper;

/**
 * 昵称更新
 * Created by cdkj on 2018/3/22.
 */

public class NickNameUpdateActivity extends AbsBaseLoadActivity {

    private ActivityNicknameUpdateBinding mBinding;


    /**
     * @param context
     * @param nickName
     */
    public static void open(Context context, String nickName) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, NickNameUpdateActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, nickName);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_nickname_update, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        mBaseBinding.titleView.setMidTitle(getString(R.string.nick_name));

        mBaseBinding.titleView.setRightTitle(getString(R.string.sure));


    }
}
