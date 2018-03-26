package com.cdkj.link_community.module.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.adapters.ViewPagerAdapter;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.link_community.R;
import com.cdkj.link_community.databinding.ActivityMyCommentBinding;

import java.util.ArrayList;

/**
 * 我的币圈评论
 * Created by cdkj on 2018/3/22.
 */

public class MyCoinBBSCommentsActivity extends AbsBaseLoadActivity {

    private ActivityMyCommentBinding mBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, MyCoinBBSCommentsActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_my_comment, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        initViewPager();
        initListener();
    }

    private void initListener() {

        mBinding.topLayout.fraFinish.setOnClickListener(view -> finish());

        /*顶部点击切换*/
        mBinding.topLayout.radiogroup.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {
                case R.id.radio_left:
                    mBinding.viewpager.setCurrentItem(0, true);
                    break;
                case R.id.radio_right:
                    mBinding.viewpager.setCurrentItem(1, true);
                    break;
            }

        });
    }


    /**
     * 初始化ViewPager
     */
    private void initViewPager() {

        //设置fragment数据
        ArrayList fragments = new ArrayList<>();

        fragments.add(MyCommentsList2Fragment.getInstanse());
        fragments.add(MyCommentsListFragment.getInstanse());

        mBinding.viewpager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));
        mBinding.viewpager.setOffscreenPageLimit(fragments.size());
    }


}
