package com.cdkj.link_community.module.active;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsRefreshListFragment;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.adapters.MyActiveListAdapter;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.model.MyActiveModel;

import java.util.List;
import java.util.Map;

import retrofit2.Call;

public class MyActiveListFragment extends AbsRefreshListFragment {

    public boolean isPass;//是否已通过

    /**
     * @param ispass
     * @return
     */
    public static MyActiveListFragment getInstanse(boolean ispass) {
        MyActiveListFragment fragment = new MyActiveListFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(CdRouteHelper.DATA_SIGN, ispass);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void afterCreate(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isPass = getArguments().getBoolean(CdRouteHelper.DATA_SIGN);
        initRefreshHelper(MyCdConfig.LISTLIMIT);
        mRefreshHelper.onDefaluteMRefresh(true);
    }

    @Override
    public RecyclerView.Adapter getListAdapter(List listData) {
        MyActiveListAdapter mAdapter = new MyActiveListAdapter(listData);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            ActiveDetailsActivity.open(mActivity, mAdapter.getItem(position).getActivity().getCode());
        });

        return mAdapter;
    }

    @Override
    public void getListRequest(int pageIndex, int limit, boolean isShowDialog) {

        Map<String, String> map = RetrofitUtils.getRequestMap();

        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());
        map.put("start", "0");
        map.put("limit", limit + "");
        if (isPass) {
            map.put("status", "1");
        } else {
            map.put("status", "02");
        }

        if (isShowDialog) showLoadingDialog();

        Call call = RetrofitUtils.createApi(MyApiServer.class).getMyActiveList("628527", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<MyActiveModel>>(mActivity) {
            @Override
            protected void onSuccess(ResponseInListModel<MyActiveModel> data, String SucMessage) {
                mRefreshHelper.setData(data.getList(), getString(R.string.no_my_active), R.drawable.no_dynamic);
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

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void onInvisible() {

    }
}
