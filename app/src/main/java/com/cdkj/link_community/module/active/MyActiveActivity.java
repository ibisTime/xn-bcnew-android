package com.cdkj.link_community.module.active;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsRefreshListActivity;
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

public class MyActiveActivity extends AbsRefreshListActivity {

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, MyActiveActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        mBaseBinding.titleView.setMidTitle(getString(R.string.user_active));
        mBaseBinding.titleView.setRightTitle("联系我们");
        mBaseBinding.titleView.setRightFraClickListener(view -> {
            CdRouteHelper.openWebViewActivityForkey(getString(R.string.about_us), "about_us");
        });

        initRefreshHelper(MyCdConfig.LISTLIMIT);

    }

    @Override
    protected void onResume() {
        super.onResume();

        mRefreshHelper.onDefaluteMRefresh(true);
    }

    @Override
    public RecyclerView.Adapter getListAdapter(List listData) {
        MyActiveListAdapter mAdapter = new MyActiveListAdapter(listData);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            ActiveDetailsActivity.open(this, mAdapter.getItem(position).getActivity().getCode());
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

        Call call = RetrofitUtils.createApi(MyApiServer.class).getMyActiveList("628527", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<MyActiveModel>>(this) {
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
}
