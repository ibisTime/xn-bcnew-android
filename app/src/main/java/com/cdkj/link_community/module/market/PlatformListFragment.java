package com.cdkj.link_community.module.market;

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
import com.cdkj.link_community.adapters.PlatformListAdapter;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.model.CoinListModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 平台 列表Fragment
 * Created by cdkj on 2018/3/24.
 */

public class PlatformListFragment extends AbsRefreshListFragment {

    private boolean isFirstRequest;//是否进行了第一次请求

    private String mPlatformType;

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
            mPlatformType = getArguments().getString(CdRouteHelper.DATASIGN);
            isFirstRequest = getArguments().getBoolean("isFirstRequest");
        }

        initRefreshHelper(MyCdConfig.LISTLIMIT);

        if (isFirstRequest) {
            mRefreshHelper.onDefaluteMRefresh(true);
        }
    }

    @Override
    public RecyclerView.Adapter getListAdapter(List listData) {
        return new PlatformListAdapter(listData);
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
                disMissLoading();
            }
        });


    }
}
