package com.cdkj.link_community.module.coin_bbs;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.cdkj.baselibrary.adapters.ViewPagerAdapter;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.DisplayHelper;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.adapters.MarketChooseListAdapter;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.databinding.ActivityCoinBbsDetailsBinding;
import com.cdkj.link_community.model.CoinBBSDetails;
import com.cdkj.link_community.module.message.FastMessageListFragment;
import com.cdkj.link_community.module.message.MessageListFragment;
import com.cdkj.link_community.views.MyScrollView;
import com.cdkj.link_community.views.ViewPagerIndicator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 币吧详情
 * Created by cdkj on 2018/3/21.
 */

public class CoinBBSDetailsActivity extends AbsBaseLoadActivity {

    private ActivityCoinBbsDetailsBinding mBinding;

    private int moveHeight = 150;

    private String mBBSCode;//币吧编号

    /**
     * @param context
     * @param bbsCode 币吧编号
     */
    public static void open(Context context, String bbsCode) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, CoinBBSDetailsActivity.class);
        intent.putExtra(CdRouteHelper.APPLOGIN, bbsCode);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_coin_bbs_details, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        if (getIntent() != null) {
            mBBSCode = getIntent().getStringExtra(CdRouteHelper.APPLOGIN);
        }

        mBaseBinding.titleView.setMidTitle(getString(R.string.coin_bbs));

        mBinding.refreshLayout.setEnableRefresh(false);
        mBinding.refreshLayout.setEnableLoadmore(true);
        mBinding.refreshLayout.setEnableLoadmoreWhenContentNotFull(true);

        //设置fragment数据
        ArrayList fragments = new ArrayList<>();

        fragments.add(FastMessageListFragment.getInstanse(0, false));
        fragments.add(FastMessageListFragment.getInstanse(0, false));
        fragments.add(FastMessageListFragment.getInstanse(0, false));

        mBinding.viewpager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));
        mBinding.viewpager.setOffscreenPageLimit(fragments.size());

        mBinding.viewindicator.setmLinWidth(25);
        mBinding.viewindicator.setVisibleTabCount(fragments.size());
        mBinding.viewindicator.setTabItemTitles(Arrays.asList(getString(R.string.bbs_circular), getString(R.string.msg), getString(R.string.market)));
        mBinding.viewindicator.setViewPager(mBinding.viewpager, 0);


        mBinding.viewindicatorTop.setmLinWidth(25);
        mBinding.viewindicatorTop.setVisibleTabCount(fragments.size());
        mBinding.viewindicatorTop.setTabItemTitles(Arrays.asList(getString(R.string.bbs_circular), getString(R.string.msg), getString(R.string.market)));
        mBinding.viewindicatorTop.setViewPager(mBinding.viewpager, 0);
        mBinding.viewindicatorTop.setVisibility(View.GONE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        mBinding.recyclerViewLeft.setLayoutManager(linearLayoutManager);

        mBinding.recyclerViewLeft.setAdapter(new MarketChooseListAdapter(new ArrayList<>()));

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

        getBBSDetails();
    }


    public void getBBSDetails() {

        if (TextUtils.isEmpty(mBBSCode)) return;

        Map<String, String> map = new HashMap<>();

        map.put("code", mBBSCode);
        map.put("userId", SPUtilHelpr.getUserId());

        showLoadingDialog();

        Call call = RetrofitUtils.createApi(MyApiServer.class).getCoinBBsDetails("628238", StringUtils.getJsonToString(map));

        call.enqueue(new BaseResponseModelCallBack<CoinBBSDetails>(this) {
            @Override
            protected void onSuccess(CoinBBSDetails data, String SucMessage) {
                setShowData(data);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });


    }

    /**
     * 设置显示数据
     *
     * @param data
     */
    private void setShowData(CoinBBSDetails data) {

        if (data == null) return;

        mBinding.tvName.setText("#" + data.getName() + "#");
        mBinding.tvFocusOnNum.setText(getString(R.string.focus_num) + data.getPostCount());
        mBinding.tvPostNum.setText(getString(R.string.post_num) + data.getPostCount());
        mBinding.tvTodayNum.setText(getString(R.string.bbs_today_num) + data.getDayCommentCount());
        mBinding.expandTextView.setText(data.getIntroduce() + "");

        mBinding.tvTodayChange.setText("涨跌浮:" + data.getCoin().getTodayChange());
        mBinding.tvTodayVol.setText("成交(24h):" + data.getCoin().getTodayVol());

        if (data.getCoin() == null) return;

        mBinding.tvCirculation.setText(data.getCoin().getTotalSupply());
        mBinding.tvIssue.setText(data.getCoin().getMaxSupply());
        mBinding.tvIssueMarket.setText(data.getCoin().getMarketCap());
        mBinding.tvIssueRank.setText(data.getCoin().getRank());


    }

}
