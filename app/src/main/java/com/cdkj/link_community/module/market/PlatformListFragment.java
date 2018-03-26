package com.cdkj.link_community.module.market;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.AbsRefreshListFragment;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.adapters.CoinListAdapter;
import com.cdkj.link_community.adapters.PlatformListAdapter;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.databinding.LayoutToBbsBinding;
import com.cdkj.link_community.model.CoinBBSInfoTotalCount;
import com.cdkj.link_community.model.CoinListModel;
import com.cdkj.link_community.model.MarketInterval;
import com.cdkj.link_community.module.coin_bbs.CoinBBSDetailsActivity;
import com.cdkj.link_community.module.coin_bbs.CoinBBSDetailsIntoActivity;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 添加行情 列表Fragment
 * Created by cdkj on 2018/3/24.
 */

public class PlatformListFragment extends AbsRefreshListFragment {

    private boolean isFirstRequest;//是否进行了第一次请求

    private String mPlatformType;

    private boolean isRequesting;//是否正在请求中 用于轮询判断

    private LayoutToBbsBinding mToBBSBinding;//进吧布局
    private PlatformListAdapter platformListAdapter;


    /**
     * @param platformType 币种类型
     * @return
     */
    public static PlatformListFragment getInstanse(String platformType, Boolean isFirstRequest) {
        PlatformListFragment fragment = new PlatformListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CdRouteHelper.DATASIGN, platformType);
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
            mPlatformType = getArguments().getString(CdRouteHelper.DATASIGN);
            isFirstRequest = getArguments().getBoolean("isFirstRequest");
        }

        mToBBSBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.layout_to_bbs, null, false);

        mToBBSBinding.tvName.setText(mPlatformType);
        mToBBSBinding.btnToBbs.setOnClickListener(view -> CoinBBSDetailsIntoActivity.open(mActivity, mPlatformType));

        initRefreshHelper(MyCdConfig.LISTLIMIT);

        if (isFirstRequest) {
            mRefreshHelper.onDefaluteMRefresh(true);
            getToBBSinfoRequest();
        }
    }

    @Override
    public RecyclerView.Adapter getListAdapter(List listData) {
        platformListAdapter = new PlatformListAdapter(listData);
        platformListAdapter.setHeaderAndEmpty(true);
        return platformListAdapter;
    }

    @Override
    public void getListRequest(int pageindex, int limit, boolean isShowDialog) {

        if (TextUtils.isEmpty(mPlatformType)) return;

        Map<String, String> map = new HashMap<>();

        map.put("exchangeEname", mPlatformType);
        map.put("start", pageindex + "");
        map.put("limit", limit + "");

        if (isShowDialog) showLoadingDialog();

        Call call = RetrofitUtils.createApi(MyApiServer.class).getCoinList("628340", StringUtils.getJsonToString(map));

        addCall(call);
        isRequesting = true;
        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<CoinListModel>>(mActivity) {
            @Override
            protected void onSuccess(ResponseInListModel<CoinListModel> data, String SucMessage) {
                mRefreshHelper.setData(data.getList(), getString(R.string.no_platform_info), 0);
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

        if (mPlatformType == null) {
            return;
        }

        Map<String, String> map = new HashMap<>();

        map.put("toCoin", mPlatformType);

        Call call = RetrofitUtils.createApi(MyApiServer.class).getCoinBBsTotalCountDetails("628850", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<CoinBBSInfoTotalCount>(mActivity) {
            @Override
            protected void onSuccess(CoinBBSInfoTotalCount data, String SucMessage) {
                if (TextUtils.equals(data.getIsExistPlate(), "1")) { //币吧存在
                    if (platformListAdapter.getHeaderLayoutCount() == 0) {
                        platformListAdapter.addHeaderView(mToBBSBinding.getRoot());
                    }
                } else {
                    platformListAdapter.removeAllHeaderView();
                }

                mToBBSBinding.tvInfo.setText("现在有" + data.getTotalCount() + "个帖子在讨论，你也一起来吧!");
            }

            @Override
            protected void onFinish() {
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
        if (mActivity == null || mActivity.isFinishing() || mRefreshHelper == null || isRequesting) {
            return;
        }
        mRefreshHelper.onDefaluteMRefresh(false);
    }


}
