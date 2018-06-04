package com.cdkj.link_community.module.active;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.base.AbsRefreshListFragment;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.adapters.ActiveApproveUserAdapter;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.model.ActiveUserModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.baselibrary.appmanager.CdRouteHelper.DATA_SIGN;
import static com.cdkj.link_community.module.message.FastMessageListFragment.ISFIRSTREQUEST;

/**
 * Created by cdkj on 2018/6/2.
 */

public class ActiveApproveUserFragment extends AbsRefreshListFragment {

    private boolean isFirstRequest;

    private String code;
    private String status;

    /**
     * @param
     * @return
     */
    public static ActiveApproveUserFragment getInstance(Boolean isFirstRequest, String code, String status) {
        ActiveApproveUserFragment fragment = new ActiveApproveUserFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DATA_SIGN, status);
        bundle.putString("code", code);
        bundle.putBoolean(ISFIRSTREQUEST, isFirstRequest);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()){
            mRefreshHelper.onDefaluteMRefresh(true);
        }
    }

    @Override
    protected void lazyLoad() {
        if (mRefreshBinding != null){
            mRefreshHelper.onDefaluteMRefresh(true);
        }

    }

    @Override
    protected void onInvisible() {

    }

    @Override
    protected void afterCreate(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            isFirstRequest = getArguments().getBoolean(ISFIRSTREQUEST);
            status = getArguments().getString(DATA_SIGN);
            code = getArguments().getString("code");

            initRefreshHelper(MyCdConfig.LISTLIMIT);

            if (isFirstRequest){
                mRefreshHelper.onDefaluteMRefresh(true);
            }

        }
    }

    @Override
    public RecyclerView.Adapter getListAdapter(List listData) {
        return new ActiveApproveUserAdapter(listData);
    }

    @Override
    public void getListRequest(int pageIndex, int limit, boolean isShowDialog) {
        Map<String, String> map = new HashMap<>();
        map.put("status", status);
        map.put("actCode", code);
        map.put("start", pageIndex + "");
        map.put("limit", limit + "");

        if (isShowDialog) showLoadingDialog();

        Call call = RetrofitUtils.createApi(MyApiServer.class).getActiveUser("628528", StringUtils.getJsonToString(map));

        addCall(call);
        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<ActiveUserModel>>(mActivity) {
            @Override
            protected void onSuccess(ResponseInListModel<ActiveUserModel> data, String SucMessage) {
                mRefreshHelper.setData(data.getList(), getString(R.string.no_approve_user), 0);
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
