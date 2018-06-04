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
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsRefreshListFragment;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.adapters.AddMarketListAdapter;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.model.AddMarketModel;
import com.cdkj.link_community.model.CoinListModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 添加行情(币种) 列表Fragment
 * Created by cdkj on 2018/3/24.
 */

public class AddMarketListFragment extends AbsRefreshListFragment {

    private boolean isFirstRequest;//是否进行了第一次请求


    private AddMarketModel marketModel;

    /**
     * @param
     * @return
     */
    public static AddMarketListFragment getInstance(AddMarketModel addMarketModel, Boolean isFirstRequest) {
        AddMarketListFragment fragment = new AddMarketListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(CdRouteHelper.DATA_SIGN, addMarketModel);
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
            marketModel = getArguments().getParcelable(CdRouteHelper.DATA_SIGN);
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
        AddMarketListAdapter addMarketListAdapter = new AddMarketListAdapter(listData, marketModel.getType());

        addMarketListAdapter.setOnItemClickListener((adapter, view, position) -> {
            addMarketRequest(addMarketListAdapter, position);
        });

        return addMarketListAdapter;
    }

    @Override
    public void getListRequest(int pageindex, int limit, boolean isShowDialog) {

        if (marketModel == null || TextUtils.isEmpty(marketModel.getEname())) return;

        Map<String, String> map = new HashMap<>();

        if (TextUtils.equals(marketModel.getType(), "1")) {
            map.put("symbol", marketModel.getEname());//币种
        } else {
            map.put("exchangeEname", marketModel.getEname());//平台
        }
        map.put("start", pageindex + "");
        map.put("limit", limit + "");
        map.put("userId", SPUtilHelper.getUserId());
        map.put("percentPeriod", "24h");

        if (isShowDialog) showLoadingDialog();

        Call call = RetrofitUtils.createApi(MyApiServer.class).getCoinList("628350", StringUtils.getJsonToString(map));

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

        if (!SPUtilHelper.isLogin(mActivity, false)) {
            return;
        }

        if (TextUtils.equals(model.getIsChoice(), "1")) {
            UITipDialog.showInfo(mActivity, getString(R.string.you_have_add));
            return;
        }

        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelper.getUserId());
        map.put("exchangeEname", model.getExchangeEname());
        map.put("toSymbol", model.getToSymbol());
        map.put("symbol", model.getSymbol());

        Call call = RetrofitUtils.getBaseAPiService().successRequest("628330", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(mActivity) {

            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {
                    model.setIsChoice("1");
                    addMarketListAdapter.notifyItemChanged(position);
                    UITipDialog.showSuccess(mActivity, getString(R.string.add_market_succ));
                }
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

}
