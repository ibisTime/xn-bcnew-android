package com.cdkj.link_community.module.coin_bbs;

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
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.AbsRefreshListFragment;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.adapters.MyCoinBBSListAdapter;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.model.CoinBBSListModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by cdkj on 2018/3/25.
 */

public class MyFocuseOnCoinBBSListFragment extends AbsRefreshListFragment {

    public static final String TYPE = "type";

    private boolean isFirstRequest;//是否创建时进行请求第一次请求

    /**
     * @param isFirstRequest
     * @return
     */
    public static MyFocuseOnCoinBBSListFragment getInstanse(boolean isFirstRequest) {
        MyFocuseOnCoinBBSListFragment fragment = new MyFocuseOnCoinBBSListFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(CdRouteHelper.DATASIGN, isFirstRequest);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void lazyLoad() {

        if (mRefreshBinding == null) {
            return;
        }
        mRefreshHelper.onDefaluteMRefresh(true);
        isFirstRequest = true;

    }

    @Override
    protected void onInvisible() {

    }

    @Override
    protected void afterCreate(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (getArguments() != null) {
            isFirstRequest = getArguments().getBoolean(CdRouteHelper.DATASIGN);
        }

        initRefreshHelper(MyCdConfig.LISTLIMIT);

        if (isFirstRequest) {
            mRefreshHelper.onDefaluteMRefresh(true);
        }

    }

    @Override
    public RecyclerView.Adapter getListAdapter(List listData) {
        MyCoinBBSListAdapter coinBBSListAdapter = new MyCoinBBSListAdapter(listData);

        coinBBSListAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (coinBBSListAdapter.getItem(position) != null) {
                CoinBBSDetailsActivity.open(mActivity, coinBBSListAdapter.getItem(position).getCode());
            }
        });

        coinBBSListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            focuseonRequest(coinBBSListAdapter, position);
        });

        return coinBBSListAdapter;
    }

    @Override
    public void getListRequest(int pageindex, int limit, boolean isShowDialog) {

        if (!SPUtilHelpr.isLoginNoStart()) {
            mRefreshHelper.setData(new ArrayList(), getString(R.string.no_coin_bbs), 0);
            return;
        }


        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelpr.getUserId());
        map.put("start", pageindex + "");
        map.put("limit", limit + "");

        if (isShowDialog) showLoadingDialog();

        Call call = RetrofitUtils.createApi(MyApiServer.class).getCoinBBSList("628245", StringUtils.getJsonToString(map));

        addCall(call);
        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<CoinBBSListModel>>(mActivity) {
            @Override
            protected void onSuccess(ResponseInListModel<CoinBBSListModel> data, String SucMessage) {
                mRefreshHelper.setData(data.getList(), getString(R.string.no_coin_bbs), 0);
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

    /**
     * 关注/解除关注
     *
     * @param adapter
     * @param position
     */
    public void focuseonRequest(MyCoinBBSListAdapter adapter, int position) {

        if (!SPUtilHelpr.isLogin(mActivity, false)) {
            return;
        }

        CoinBBSListModel coinBBSListModel = adapter.getItem(position);

        if (coinBBSListModel == null || TextUtils.isEmpty(coinBBSListModel.getCode())) return;

        Map<String, String> map = new HashMap<>();
        map.put("code", coinBBSListModel.getCode());
        map.put("userId", SPUtilHelpr.getUserId());

        Call call = RetrofitUtils.getBaseAPiService().successRequest("628240", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(mActivity) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {

                if (data.isSuccess()) {
                    UITipDialog.showSuccess(mActivity, getString(R.string.bbs_cancel_succ));
                    adapter.remove(position);
                    if (adapter.getData().size() == 0) {
                        adapter.notifyDataSetChanged();
                    }
                }

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });


    }

}
