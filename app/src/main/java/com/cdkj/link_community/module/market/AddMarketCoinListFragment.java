package com.cdkj.link_community.module.market;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.AbsRefreshListFragment;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.adapters.AddMarketListAdapter;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.model.CoinListModel;
import com.cdkj.link_community.model.CoinPlatformType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 添加行情(币种) 列表Fragment
 * Created by cdkj on 2018/3/24.
 */

public class AddMarketCoinListFragment extends AbsRefreshListFragment {

    private boolean isFirstRequest;//是否进行了第一次请求

    private String mCoinType;

    /**
     * @param
     * @return
     */
    public static AddMarketCoinListFragment getInstanse(String type, Boolean isFirstRequest) {
        AddMarketCoinListFragment fragment = new AddMarketCoinListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CdRouteHelper.DATASIGN, type);
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
            mCoinType = getArguments().getString(CdRouteHelper.DATASIGN);
            isFirstRequest = getArguments().getBoolean("isFirstRequest");
        }
        //防止局部刷新闪烁
        ((DefaultItemAnimator) mRefreshBinding.rv.getItemAnimator()).setSupportsChangeAnimations(false);
        initRefreshHelper(MyCdConfig.LISTLIMIT);

        if (isFirstRequest) {
            mRefreshHelper.onDefaluteMRefresh(true);
        }
    }

    @Override
    public RecyclerView.Adapter getListAdapter(List listData) {
        AddMarketListAdapter addMarketListAdapter = new AddMarketListAdapter(listData);

        addMarketListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            addMarketRequest(addMarketListAdapter, position);
        });

        return addMarketListAdapter;
    }

    @Override
    public void getListRequest(int pageindex, int limit, boolean isShowDialog) {

        if (TextUtils.isEmpty(mCoinType)) return;

        Map<String, String> map = new HashMap<>();

        map.put("coinSymbol", mCoinType);
        map.put("start", pageindex + "");
        map.put("limit", limit + "");
        map.put("userId", SPUtilHelpr.getUserId());

        if (isShowDialog) showLoadingDialog();

        Call call = RetrofitUtils.createApi(MyApiServer.class).getCoinList("628340", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<CoinListModel>>(mActivity) {
            @Override
            protected void onSuccess(ResponseInListModel<CoinListModel> data, String SucMessage) {
                mRefreshHelper.setData(data.getList(), getString(R.string.no_add_market), 0);
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                super.onReqFailure(errorCode, errorMessage);
                mRefreshHelper.loadError(errorMessage, 0);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    /**
     * 添加自选
     *
     * @param
     */
    public void addMarketRequest(AddMarketListAdapter addMarketListAdapter, int position) {
        CoinListModel model = addMarketListAdapter.getItem(position);

        if (model == null) return;

        if (!SPUtilHelpr.isLogin(mActivity, false)) {
            return;
        }

        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelpr.getUserId());
        map.put("exchangeEname", model.getExchangeEname());
        map.put("refCoin", model.getToCoinSymbol());
        map.put("symbol", model.getCoinSymbol());

        Call call = RetrofitUtils.getBaseAPiService().successRequest("628330", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(mActivity) {

            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {
                    addMarketListAdapter.notifyItemChanged(position);
                }
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

}
