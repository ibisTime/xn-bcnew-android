package com.cdkj.link_community.module.market;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.base.AbsRefreshListFragment;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.adapters.CoinListAdapter;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.databinding.LayoutToBbsBinding;
import com.cdkj.link_community.model.CoinBBSInfoTotalCount;
import com.cdkj.link_community.model.CoinListModel;
import com.cdkj.link_community.model.MarketInterval;
import com.cdkj.link_community.module.coin_bbs.CoinBBSDetailsIntoActivity;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 币种 列表Fragment
 * Created by cdkj on 2018/3/24.
 */

public class CoinTypeListFragment extends AbsRefreshListFragment {

    private boolean isFirstRequest;//是否进行了第一次请求

    private String mCoinType;

    private boolean isRequesting;
    private CoinListAdapter coinListAdapte;
    private LayoutToBbsBinding mToBBSBinding;//进吧布局

    /**
     * @param coinType 币种类型
     * @return
     */
    public static CoinTypeListFragment getInstanse(String coinType, Boolean isFirstRequest) {
        CoinTypeListFragment fragment = new CoinTypeListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CdRouteHelper.DATASIGN, coinType);
        bundle.putBoolean("isFirstRequest", isFirstRequest);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void lazyLoad() {
        if (mRefreshBinding == null) return;
        if (!isFirstRequest) {
            isFirstRequest = true;
            mRefreshHelper.onDefaluteMRefresh(true);
        }
        getToBBSinfoRequest();

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

        mToBBSBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.layout_to_bbs, null, false);

        mToBBSBinding.tvName.setText(mCoinType);
        mToBBSBinding.btnToBbs.setOnClickListener(view -> CoinBBSDetailsIntoActivity.open(mActivity, mCoinType));

        initRefreshHelper(MyCdConfig.LISTLIMIT);

        if (isFirstRequest) {
            mRefreshHelper.onDefaluteMRefresh(true);
        }
    }

    @Override
    public RecyclerView.Adapter getListAdapter(List listData) {
        coinListAdapte = new CoinListAdapter(listData);
        coinListAdapte.setHeaderAndEmpty(true);
        return coinListAdapte;
    }

    @Override
    public void getListRequest(int pageindex, int limit, boolean isShowDialog) {

        if (TextUtils.isEmpty(mCoinType)) return;

        Map<String, String> map = new HashMap<>();

        map.put("coinSymbol", mCoinType);
        map.put("start", pageindex + "");
        map.put("limit", limit + "");

        if (isShowDialog) showLoadingDialog();

        Call call = RetrofitUtils.createApi(MyApiServer.class).getCoinList("628340", StringUtils.getJsonToString(map));

        addCall(call);
        isRequesting = true;
        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<CoinListModel>>(mActivity) {
            @Override
            protected void onSuccess(ResponseInListModel<CoinListModel> data, String SucMessage) {
                mRefreshHelper.setData(data.getList(), getString(R.string.no_coin_info), 0);
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                super.onReqFailure(errorCode, errorMessage);
                mRefreshHelper.loadError(errorMessage, 0);
            }

            @Override
            protected void onFinish() {
                isRequesting = false;
                disMissLoading();
            }
        });

    }

    /**
     * 获取进吧信息
     */
    private void getToBBSinfoRequest() {

        if (mCoinType == null) {
            return;
        }

        Map<String, String> map = new HashMap<>();

        map.put("toCoin", mCoinType);

        Call call = RetrofitUtils.createApi(MyApiServer.class).getCoinBBsTotalCountDetails("628850", StringUtils.getJsonToString(map));

        addCall(call);


        call.enqueue(new BaseResponseModelCallBack<CoinBBSInfoTotalCount>(mActivity) {
            @Override
            protected void onSuccess(CoinBBSInfoTotalCount data, String SucMessage) {
                if (TextUtils.equals(data.getIsExistPlate(), "1")) { //币吧存在
                    if (coinListAdapte.getHeaderLayoutCount() == 0) {
                        coinListAdapte.addHeaderView(mToBBSBinding.getRoot());
                    }
                } else {
                    coinListAdapte.removeAllHeaderView();
                }

                mToBBSBinding.tvInfo.setText("现在有" + data.getTotalCount() + "个帖子在讨论，你也一起来吧!");
            }

            @Override
            protected void onFinish() {
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
        if (mActivity == null || mActivity.isFinishing() || !getUserVisibleHint() || mRefreshHelper == null || mRefreshHelper == null || isRequesting) {
            return;
        }
        mRefreshHelper.onDefaluteMRefresh(false);
    }


}
