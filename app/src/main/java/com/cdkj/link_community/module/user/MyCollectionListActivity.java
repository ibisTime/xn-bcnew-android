package com.cdkj.link_community.module.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsRefreshListActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.adapters.MyCollectionListAdapter;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.model.FastMessage;
import com.cdkj.link_community.module.message.MessageDetailsActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 我的收藏
 * Created by cdkj on 2018/3/24.
 */

public class MyCollectionListActivity extends AbsRefreshListActivity {

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, MyCollectionListActivity.class);
        context.startActivity(intent);
    }


    @Override
    public RecyclerView.Adapter getListAdapter(List listData) {
        MyCollectionListAdapter myCollectionListAdapter = new MyCollectionListAdapter(listData);

        myCollectionListAdapter.setOnItemClickListener((adapter, view, position) -> {

            MessageDetailsActivity.open(MyCollectionListActivity.this, myCollectionListAdapter.getItem(position).getCode());

        });

        return myCollectionListAdapter;
    }

    @Override
    public void getListRequest(int pageIndex, int limit, boolean isShowDialog) {
        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelper.getUserId());
        map.put("start", pageIndex + "");
        map.put("limit", limit + "");

        if (isShowDialog) showLoadingDialog();

        Call call = RetrofitUtils.createApi(MyApiServer.class).getCollectionList("628207", StringUtils.getJsonToString(map));

        addCall(call);


        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<FastMessage>>(this) {
            @Override
            protected void onSuccess(ResponseInListModel<FastMessage> data, String SucMessage) {
                mRefreshHelper.setData(data.getList(), getString(R.string.no_collection), R.drawable.no_dynamic);
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
    protected void onResume() {
        super.onResume();

        mRefreshHelper.onDefaluteMRefresh(true);
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        mBaseBinding.titleView.setMidTitle(getString(R.string.collection));

        initRefreshHelper(MyCdConfig.LISTLIMIT);

    }
}
