package com.cdkj.link_community.module.coin_bbs;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.cdkj.baselibrary.adapters.ViewPagerAdapter;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.utils.DisplayHelper;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.link_community.R;
import com.cdkj.link_community.adapters.MarketChooseListAdapter;
import com.cdkj.link_community.databinding.ActivityCoinBbsDetailsBinding;
import com.cdkj.link_community.module.message.FastMessageListFragment;
import com.cdkj.link_community.module.message.MessageListFragment;
import com.cdkj.link_community.views.MyScrollView;
import com.cdkj.link_community.views.ViewPagerIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 李先俊 on 2018/3/21.
 */

public class CoinBBSDetailsActivity extends AbsBaseLoadActivity {

    private ActivityCoinBbsDetailsBinding mBinding;

    private int moveHeight = 150;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, CoinBBSDetailsActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_coin_bbs_details, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        mBaseBinding.titleView.setMidTitle(getString(R.string.coin_bbs));

        mBinding.expandTextView.setText("dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");

        //设置fragment数据
        ArrayList fragments = new ArrayList<>();

        fragments.add(FastMessageListFragment.getInstanse());
        fragments.add(MessageListFragment.getInstanse());
        fragments.add(MessageListFragment.getInstanse());

        mBinding.viewpager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));
        mBinding.viewpager.setOffscreenPageLimit(fragments.size());

        mBinding.viewindicator.setmLinWidth(25);
        mBinding.viewindicator.setVisibleTabCount(fragments.size());
        mBinding.viewindicator.setTabItemTitles(Arrays.asList(getString(R.string.all), getString(R.string.hot_message), getString(R.string.hot_message)));
        mBinding.viewindicator.setViewPager(mBinding.viewpager, 0);


        mBinding.viewindicatorTop.setmLinWidth(25);
        mBinding.viewindicatorTop.setVisibleTabCount(fragments.size());
        mBinding.viewindicatorTop.setTabItemTitles(Arrays.asList(getString(R.string.all), getString(R.string.hot_message), getString(R.string.hot_message)));
        mBinding.viewindicatorTop.setViewPager(mBinding.viewpager, 0);
        mBinding.viewindicatorTop.setVisibility(View.GONE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        mBinding.recyclerViewLeft.setLayoutManager(linearLayoutManager);

        List<String> list = new ArrayList<>();

        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");

        mBinding.recyclerViewLeft.setAdapter(new MarketChooseListAdapter(list));

        mBinding.scrollView.setOnScrollListener(new MyScrollView.MyOnScrollListener() {
            @Override
            public void onScroll(int y) {

            }

            @Override
            public void onCurrentY(int y) {
                if (y >= moveHeight) {
                    mBinding.viewindicatorTop.setVisibility(View.VISIBLE);
                } else {
                    mBinding.viewindicatorTop.setVisibility(View.GONE);
                }

            }
        });


        mBinding.linTop.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                moveHeight = mBinding.linTop.getHeight() + DisplayHelper.dpToPx(20);
            }
        });

    }
}
