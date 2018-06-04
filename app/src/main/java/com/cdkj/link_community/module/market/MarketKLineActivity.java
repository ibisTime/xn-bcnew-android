package com.cdkj.link_community.module.market;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.base.BaseWebViewFragment;
import com.cdkj.baselibrary.model.eventmodels.FullScreenModel;
import com.cdkj.link_community.R;
import com.cdkj.link_community.databinding.ActivityMarketKLineBinding;
import com.cdkj.link_community.model.CoinListModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by cdkj on 2018/4/19.
 */

public class MarketKLineActivity extends FragmentActivity {

    private ActivityMarketKLineBinding mBinding;

    private CoinListModel model;

    private String url;

    /**
     * @param context
     */
    public static void open(Context context, CoinListModel model, String url) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, MarketKLineActivity.class);
        intent.putExtra(CdRouteHelper.DATA_SIGN, model);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_market_k_line, null, false);
        setContentView(mBinding.getRoot());

        if (getIntent() == null)
            return;

        url = getIntent().getStringExtra("url");
        model = (CoinListModel) getIntent().getSerializableExtra(CdRouteHelper.DATA_SIGN);

        EventBus.getDefault().register(this);

        initChartWebView();

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {//横屏

            url = getIntent().getStringExtra("url");
            model = (CoinListModel) getIntent().getSerializableExtra(CdRouteHelper.DATA_SIGN);

            initChartWebView();

        }
    }

    private void initChartWebView() {
        FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();

        mFragmentTransaction.add(R.id.fl_k_line, BaseWebViewFragment.getInstance(url + "/charts/index.html?symbol="
                + model.getSymbol()
                + "/"
                + model.getToSymbol()
                + "&exchange="
                + model.getExchangeEname()
                + "&isfull=1",true));
        mFragmentTransaction.commit();

    }

    @Subscribe
    public void toQuitFullScreen(FullScreenModel fullScreenModel){
        if (fullScreenModel == null)
            return;

        if (fullScreenModel.getIndex().equals("0"))
            finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }
}
