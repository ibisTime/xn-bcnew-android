package com.cdkj.link_community.module.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.cdkj.baselibrary.adapters.TablayoutAdapter;
import com.cdkj.baselibrary.adapters.ViewPagerAdapter;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.UIStatusBarHelper;
import com.cdkj.link_community.R;
import com.cdkj.link_community.databinding.ActivityUserPersonBinding;
import com.cdkj.link_community.model.TabCurrentModel;
import com.cdkj.link_community.model.UserInfoModel;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.cdkj.baselibrary.appmanager.CdRouteHelper.DATA_SIGN;

/**
 * Created by cdkj on 2018/4/30.
 */

public class UserPersonActivity extends AbsBaseLoadActivity {

    private ActivityUserPersonBinding mBinding;

    private UserInfoModel infoModel;

    public static void open(Context context, UserInfoModel infoModel) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, UserPersonActivity.class);
        intent.putExtra(DATA_SIGN, infoModel);
        context.startActivity(intent);
    }


    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_user_person, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        UIStatusBarHelper.translucent(this, ContextCompat.getColor(this, R.color.user_center_bg));
        if (getIntent() == null)
            return;

        infoModel = getIntent().getParcelableExtra(DATA_SIGN);

        mBinding.titleView.setBackgroundColor(ContextCompat.getColor(this, R.color.user_center_bg));
        mBaseBinding.viewV.setVisibility(View.GONE);

        mBinding.titleView.setMidTitle(getString(R.string.user_center));

        mBinding.tvUserName.setText(infoModel.getNickname());
        mBinding.titleView.setLeftTitleColor(com.cdkj.baselibrary.R.color.title_bg);
        mBinding.titleView.setRightTitleColor(com.cdkj.baselibrary.R.color.white);
        mBinding.titleView.setMidTitleColor(com.cdkj.baselibrary.R.color.white);
        mBinding.titleView.setLeftImg(com.cdkj.baselibrary.R.drawable.back_img);
        mBinding.titleView.setLeftFraClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImgUtils.loadQiniuLogo(this, infoModel.getPhoto(), mBinding.imgUserLogo);

        initViews();
    }

    private void initViews() {

        TablayoutAdapter tablayoutAdapter = new TablayoutAdapter(getSupportFragmentManager());
        //设置fragment数据
        ArrayList fragments = new ArrayList<>();

        fragments.add(UserDynamicListFragment.getInstance());
        fragments.add(MyReleaseMessageListFragment.getInstance(true, "1"));

        tablayoutAdapter.addFrag(fragments, Arrays.asList("动态", "文章"));

        mBinding.viewpager.setOffscreenPageLimit(fragments.size());

        mBinding.viewpager.setAdapter(tablayoutAdapter);

        mBinding.tablayout.setupWithViewPager(mBinding.viewpager);        //viewpager和tablayout关联
    }

    @Subscribe
    public void setTabToHotMsgEvent(TabCurrentModel tabCurrentModel) {
        if (tabCurrentModel == null || tabCurrentModel.getCurrent() < 0 || tabCurrentModel.getCurrent() > 1)
            return;

        mBinding.viewpager.setCurrentItem(tabCurrentModel.getCurrent());
    }
}
