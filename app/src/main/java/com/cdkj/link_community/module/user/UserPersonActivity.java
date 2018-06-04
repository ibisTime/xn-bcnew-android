package com.cdkj.link_community.module.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.cdkj.baselibrary.adapters.ViewPagerAdapter;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.databinding.ActivityUserPersonBinding;
import com.cdkj.link_community.model.TabCurrentModel;
import com.cdkj.link_community.model.UserInfoModel;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;

import static com.cdkj.baselibrary.appmanager.CdRouteHelper.DATA_SIGN;

/**
 * Created by cdkj on 2018/4/30.
 */

public class UserPersonActivity extends AbsBaseLoadActivity{

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
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_user_person, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        if (getIntent() == null)
            return;

        infoModel = getIntent().getParcelableExtra(DATA_SIGN);

        mBaseBinding.titleView.setBackgroundColor(ContextCompat.getColor(this, R.color.user_center_bg));
        mBaseBinding.viewV.setVisibility(View.GONE);

        mBaseBinding.titleView.setMidTitle(getString(R.string.user_center));

        mBinding.tvUserName.setText(infoModel.getNickname());
        ImgUtils.loadQiniuLogo(this, infoModel.getPhoto(), mBinding.imgUserLogo);

        initViews();
    }

    private void initViews() {

        //设置fragment数据
        ArrayList fragments = new ArrayList<>();

        fragments.add(UserDynamicListFragment.getInstance());
        fragments.add(MyReleaseMessageListFragment.getInstance(true,"1"));

        mBinding.viewpager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));
        mBinding.viewpager.setOffscreenPageLimit(fragments.size());

        mBinding.viewpager.setPagingEnabled(true);

        mBinding.viewindicator.setmLinWidth(25);
        mBinding.viewindicator.setTabItemTitles(Arrays.asList("动态", "资讯"));
        mBinding.viewindicator.setViewPager(mBinding.viewpager, 0);

    }

    @Subscribe
    public void setTabToHotMsgEvent(TabCurrentModel tabCurrentModel){
        if (tabCurrentModel == null)
            return;

        mBinding.viewindicator.setViewPager(mBinding.viewpager, tabCurrentModel.getCurrent());
    }
}
