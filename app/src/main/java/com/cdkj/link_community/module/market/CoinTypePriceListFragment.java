package com.cdkj.link_community.module.market;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.base.AbsRefreshListFragment;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.adapters.CoinPriceListAdapter;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.model.CoinPrice;
import com.cdkj.link_community.model.MarketInterval;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 币种列表（币价） Fragment
 * Created by cdkj on 2018/3/24.
 */

public class CoinTypePriceListFragment extends AbsRefreshListFragment {

    private boolean isFirstRequest;//是否进行了第一次请求
    private boolean isRequesting;//是否正在请求中 用于轮询判断

    /**
     * @return
     */
    public static CoinTypePriceListFragment getInstance(Boolean isFirstRequest) {
        CoinTypePriceListFragment fragment = new CoinTypePriceListFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isFirstRequest", isFirstRequest);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void lazyLoad() {

        if (mRefreshBinding == null || isFirstRequest) return;
        isFirstRequest = true;
        mRefreshHelper.onDefaluteMRefresh(true);

    }

    @Override
    protected void onInvisible() {

    }


    @Override
    protected void afterCreate(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (getArguments() != null) {
            isFirstRequest = getArguments().getBoolean("isFirstRequest");
        }

        initRefreshHelper(MyCdConfig.LISTLIMIT);

        if (isFirstRequest) {
            mRefreshHelper.onDefaluteMRefresh(true);
        }
    }

    @Override
    public RecyclerView.Adapter getListAdapter(List listData) {
        return new CoinPriceListAdapter(listData);
    }

    @Override
    public void getListRequest(int pageindex, int limit, boolean isShowDialog) {


        Map<String, String> map = new HashMap<>();

        map.put("start", pageindex + "");
        map.put("limit", limit + "");

        if (isShowDialog) showLoadingDialog();

        Call call = RetrofitUtils.createApi(MyApiServer.class).getCoinPriceList("628341", StringUtils.getJsonToString(map));

        addCall(call);
        isRequesting=true;
        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<CoinPrice>>(mActivity) {
            @Override
            protected void onSuccess(ResponseInListModel<CoinPrice> data, String SucMessage) {
                mRefreshHelper.setDataAsync(data.getList(), getString(R.string.no_coin_info), R.drawable.no_dynamic);
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                super.onReqFailure(errorCode, errorMessage);
                mRefreshHelper.loadError(errorMessage, 0);
            }

            @Override
            protected void onFinish() {
                isRequesting=false;
                disMissLoading();
            }
        });

    }

    /**
     * 轮询刷新
     *
     * @param
     */
    @Subscribe
    public void IntervalRefreshEvent(MarketInterval marketInterval) {
        if (mActivity == null || mActivity.isFinishing() || !getUserVisibleHint() || mRefreshHelper == null || isRequesting) {
            return;
        }
        mRefreshHelper.onDefaluteMRefresh(false);
    }


}
