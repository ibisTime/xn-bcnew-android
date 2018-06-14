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
import com.cdkj.link_community.adapters.MessageListAdapter;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.model.FastMessage;
import com.cdkj.link_community.module.message.MessageDetailsActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.baselibrary.appmanager.CdRouteHelper.DATA_SIGN;
import static com.cdkj.link_community.module.message.FastMessageListFragment.ISFIRSTREQUEST;

/**
 * Created by cdkj on 2018/4/30.
 */

public class MyReleaseMessageListFragment extends AbsRefreshListFragment {

    private String status;
    private boolean isFirstRequest;

    /**
     * @param
     * @return
     */
    public static MyReleaseMessageListFragment getInstance(Boolean isFirstRequest, String status) {
        MyReleaseMessageListFragment fragment = new MyReleaseMessageListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DATA_SIGN, status);
        bundle.putBoolean(ISFIRSTREQUEST, isFirstRequest);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void lazyLoad() {
        if (mRefreshBinding == null) return;

        mRefreshHelper.onDefaluteMRefresh(true);
    }

    @Override
    protected void onInvisible() {

    }

    @Override
    protected void afterCreate(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            status = getArguments().getString(DATA_SIGN);
            isFirstRequest = getArguments().getBoolean(ISFIRSTREQUEST);

            initRefreshHelper(MyCdConfig.LISTLIMIT);

            if (isFirstRequest) {
                mRefreshHelper.onDefaluteMRefresh(true);
            }

        }

    }

    @Override
    public RecyclerView.Adapter getListAdapter(List listData) {
        MessageListAdapter msgAdapter = new MessageListAdapter(listData, mActivity);

        msgAdapter.setOnItemClickListener((adapter, view, position) -> {
            MessageDetailsActivity.open(mActivity, msgAdapter.getItem(position).getCode());
        });

        return msgAdapter;
    }

    @Override
    public void getListRequest(int pageIndex, int limit, boolean isShowDialog) {
        Map<String, String> map = new HashMap<>();

        map.put("ownerId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());
        map.put("type", "");
        map.put("status", status);
        map.put("start", pageIndex + "");
        map.put("limit", limit + "");

        if (isShowDialog) showLoadingDialog();

        Call call = RetrofitUtils.createApi(MyApiServer.class).getMsgList("628198", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<FastMessage>>(mActivity) {
            @Override
            protected void onSuccess(ResponseInListModel<FastMessage> data, String SucMessage) {
                mRefreshHelper.setData(data.getList(), getString(R.string.no_article), R.drawable.no_note);
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
