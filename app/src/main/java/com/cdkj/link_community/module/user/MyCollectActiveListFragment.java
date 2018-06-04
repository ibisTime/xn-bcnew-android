package com.cdkj.link_community.module.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsRefreshListFragment;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.adapters.ActiveCollectListAdapter;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.model.ActiveModel;
import com.cdkj.link_community.module.active.ActiveDetailsActivity;

import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by cdkj on 2018/4/30.
 */

public class MyCollectActiveListFragment extends AbsRefreshListFragment {


    public static MyCollectActiveListFragment getInstance() {
        MyCollectActiveListFragment fragment = new MyCollectActiveListFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void lazyLoad() {
        if (mRefreshBinding == null ) return;

        mRefreshHelper.onDefaluteMRefresh(true);
    }

    @Override
    protected void onInvisible() {

    }

    @Override
    protected void afterCreate(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initRefreshHelper(MyCdConfig.LISTLIMIT);

    }

    @Override
    public RecyclerView.Adapter getListAdapter(List listData) {
        ActiveCollectListAdapter mAdapter = new ActiveCollectListAdapter(listData);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            ActiveDetailsActivity.open(mActivity, mAdapter.getItem(position).getCode());
        });

        return mAdapter;
    }

    @Override
    public void getListRequest(int pageIndex, int limit, boolean isShowDialog) {
        Map<String, String> map = RetrofitUtils.getRequestMap();

        map.put("userId", SPUtilHelper.getUserId());
        map.put("start", pageIndex + "");
        map.put("limit", limit + "");

        if (isShowDialog) showLoadingDialog();

        Call call = RetrofitUtils.createApi(MyApiServer.class).getActiveList("628515", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<ActiveModel>>(mActivity) {
            @Override
            protected void onSuccess(ResponseInListModel<ActiveModel> data, String SucMessage) {
                mRefreshHelper.setData(data.getList(), getString(R.string.no_active), 0);
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
