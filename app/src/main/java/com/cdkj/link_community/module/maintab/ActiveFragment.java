package com.cdkj.link_community.module.maintab;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.interfaces.RefreshHelper;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.adapters.ActiveListAdapter;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.databinding.FragmentActiveBinding;
import com.cdkj.link_community.model.ActiveModel;
import com.cdkj.link_community.module.active.ActiveDetailsActivity;
import com.cdkj.link_community.module.search.SearchActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by cdkj on 2018/4/27.
 */

public class ActiveFragment extends BaseLazyFragment {

    private FragmentActiveBinding mBinding;

    private RefreshHelper mActiveRefreshHelper; //最新评论刷新

    public static ActiveFragment getInstance() {
        ActiveFragment fragment = new ActiveFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_active, null, false);

        if (getArguments() != null) {
//            isFirstRequest = getArguments().getBoolean(CdRouteHelper.DATASIGN);
        }


        initListener();
        initRefreshHelper();

        return mBinding.getRoot();

    }

    private void initListener() {
        mBinding.fraToSearch.setOnClickListener(view -> SearchActivity.open(mActivity));

    }

    private void initRefreshHelper() {
        mActiveRefreshHelper = new RefreshHelper(mActivity, new BaseRefreshCallBack(mActivity) {
            @Override
            public View getRefreshLayout() {
                return mBinding.refreshLayout;
            }

            @Override
            public RecyclerView getRecyclerView() {

                return mBinding.rvActive;
            }

            @Override
            public RecyclerView.Adapter getAdapter(List listData) {

                ActiveListAdapter mAdapter = getActiveListAdapter(listData);

                return mAdapter;
            }

            @Override
            public void getListDataRequest(int pageindex, int limit, boolean isShowDialog) {
                getListRequest(pageindex, limit, isShowDialog);
            }
        });

        mActiveRefreshHelper.init(MyCdConfig.LISTLIMIT);

    }

    @NonNull
    private ActiveListAdapter getActiveListAdapter(List listData) {
        ActiveListAdapter mAdapter = new ActiveListAdapter(listData);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (!SPUtilHelper.isLogin(mActivity, false)) {
                return;
            }
            ActiveDetailsActivity.open(mActivity, mAdapter.getItem(position).getCode());
        });

        return mAdapter;
    }



    public void getListRequest(int pageIndex, int limit, boolean isShowDialog) {
        Map<String, String> map = new HashMap<>();
        map.put("status", "1");
        map.put("userId", SPUtilHelper.getUserId());
        map.put("start", pageIndex + "");
        map.put("limit", limit + "");

        if (isShowDialog) showLoadingDialog();

        Call call = RetrofitUtils.createApi(MyApiServer.class).getActiveList("628507", StringUtils.getJsonToString(map));

        addCall(call);
        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<ActiveModel>>(mActivity) {
            @Override
            protected void onSuccess(ResponseInListModel<ActiveModel> data, String SucMessage) {
                mActiveRefreshHelper.setData(data.getList(), getString(R.string.no_active), R.drawable.no_dynamic);
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                super.onReqFailure(errorCode, errorMessage);
                mActiveRefreshHelper.loadError(errorMessage, 0);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    @Override
    protected void lazyLoad() {

        if (mBinding == null) {
            return;
        }
        mActiveRefreshHelper.onDefaluteMRefresh(true);

    }

    @Override
    protected void onInvisible() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            if (mBinding == null) {
                return;
            }
            mActiveRefreshHelper.onDefaluteMRefresh(false);
        }
    }
}
