package com.cdkj.link_community.module.market;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.link_community.R;
import com.cdkj.link_community.databinding.ActivityMarketProjectBinding;
import com.cdkj.link_community.model.CoinListModel;

/**
 * Created by cdkj on 2018/5/24.
 */

public class MarketProjectActivity extends AbsBaseLoadActivity {

    private CoinListModel model;
    private boolean isOpen;// 是否展开状态 用于

    private ActivityMarketProjectBinding mBinding;

    /**
     * @param context
     */
    public static void open(Context context, CoinListModel model) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, MarketProjectActivity.class);
        intent.putExtra(CdRouteHelper.DATA_SIGN, model);
        context.startActivity(intent);
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_market_project, null, false);

        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        mBaseBinding.titleView.setMidTitle("项目分析");

        if (getIntent() != null) {
            model = (CoinListModel) getIntent().getSerializableExtra(CdRouteHelper.DATA_SIGN);
        }

        setView();
        initListener();
    }


    private void setView() {
        if (model.getCoin() == null)
            return;

        mBinding.tvContent.setText(model.getCoin().getIntroduce());
        mBinding.tvCName.setText(model.getCoin().getCname());
        mBinding.tvEName.setText(model.getCoin().getEname());

        mBinding.tvTurnover.setText(model.getCoin().getTotalSupply());
        mBinding.tvIssue.setText(model.getCoin().getMaxSupply());
        mBinding.tvTurnoverMarketCap.setText(model.getCoin().getTotalSupplyMarket());
        mBinding.tvTotalMarketCap.setText(model.getCoin().getMaxSupplyMarket());

        mBinding.tvExchange.setText(model.getCoin().getPutExchange());
        mBinding.tvExchangeTop10.setText(model.getCoin().getTopExchange());
        mBinding.tvWalletType.setText(model.getCoin().getWalletType());
        mBinding.tvSkypegmwcn.setText(model.getCoin().getWebUrl());

        mBinding.tvIcoTime.setText(model.getCoin().getIcoDatetime());
        mBinding.tvIcoCost.setText(model.getCoin().getIcoCost());
        mBinding.tvIcoAmount.setText(model.getCoin().getRaiseAmount());
        mBinding.tvIcoCoin.setText(model.getCoin().getTokenDist());

        mBinding.tvNewCommit.setText(model.getCoin().getLastCommitCount());
        mBinding.tvTotalCommit.setText(model.getCoin().getTotalCommitCount());
        mBinding.tvTotalContribute.setText(model.getCoin().getTotalDist());
        mBinding.tvFans.setText(model.getCoin().getFansCount());
        mBinding.tvCollect.setText(model.getCoin().getKeepCount());
        mBinding.tvCopy.setText(model.getCoin().getCopyCount());

    }

    private void initListener() {
        mBinding.llOpen.setOnClickListener(view -> {
            setShowState();
        });
    }

    /**
     * 设置点击状态
     */
    private void setShowState() {
        if (isOpen) {
            mBinding.tvContent.setMaxLines(Integer.MAX_VALUE);
            mBinding.tvContent.setEllipsize(null);
            mBinding.tvOpen.setText("点击收起");
            mBinding.ivOpen.setBackgroundResource(R.drawable.market_project_up);
        } else {
            mBinding.tvContent.setMaxLines(3);
            mBinding.tvContent.setEllipsize(TextUtils.TruncateAt.END);
            mBinding.tvOpen.setText("展开全文");
            mBinding.ivOpen.setBackgroundResource(R.drawable.market_project_down);
        }

        isOpen = !isOpen;
    }

}
