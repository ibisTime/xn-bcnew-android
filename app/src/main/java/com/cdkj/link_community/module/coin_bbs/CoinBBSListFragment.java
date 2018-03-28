package com.cdkj.link_community.module.coin_bbs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
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
import com.cdkj.link_community.adapters.CoinBBSListAdapter;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.model.CoinBBSListModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by cdkj on 2018/3/25.
 */

public class CoinBBSListFragment extends AbsRefreshListFragment {

    public static final String TYPE = "type";
    public static final String HOT = "1"; //热门
    public static final String NOHOT = "0"; //普通

    private boolean isFirstRequest;//是否创建时进行请求第一次请求

    private String mBBSType;//请求币吧类型

    /**
     * @param isFirstRequest
     * @return
     */
    public static CoinBBSListFragment getInstanse(String type, boolean isFirstRequest) {
        CoinBBSListFragment fragment = new CoinBBSListFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(CdRouteHelper.DATASIGN, isFirstRequest);
        bundle.putString(TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void lazyLoad() {

        if (mRefreshBinding == null) {
            return;
        }
        mRefreshHelper.onDefaluteMRefresh(true);

    }

    @Override
    protected void onInvisible() {

    }

    @Override
    protected void afterCreate(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (getArguments() != null) {
            mBBSType = getArguments().getString(TYPE);
            isFirstRequest = getArguments().getBoolean(CdRouteHelper.DATASIGN);
        }

        initRefreshHelper(MyCdConfig.LISTLIMIT);

        if (isFirstRequest) {
            mRefreshHelper.onDefaluteMRefresh(true);
        }

        //防止局部刷新闪烁
        ((DefaultItemAnimator) mRefreshBinding.rv.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    @Override
    public RecyclerView.Adapter getListAdapter(List listData) {
        CoinBBSListAdapter coinBBSListAdapter = new CoinBBSListAdapter(listData, TextUtils.equals(mBBSType, HOT));

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


        Map<String, String> map = new HashMap<>();

        if (TextUtils.equals(mBBSType, HOT)) {
            map.put("location", "1");
        } else if (TextUtils.equals(mBBSType, NOHOT)) {
            map.put("location", "0");
        }

        map.put("userId", SPUtilHelpr.getUserId());
        map.put("start", pageindex + "");
        map.put("limit", limit + "");

        if (isShowDialog) showLoadingDialog();

        Call call = RetrofitUtils.createApi(MyApiServer.class).getCoinBBSList("628237", StringUtils.getJsonToString(map));

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
    public void focuseonRequest(CoinBBSListAdapter adapter, int position) {

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

                    if (TextUtils.equals(coinBBSListModel.getIsKeep(), "1")) {
                        coinBBSListModel.setIsKeep("0");
                        coinBBSListModel.setKeepCount(coinBBSListModel.getKeepCount() - 1);
                        UITipDialog.showSuccess(mActivity, getString(R.string.bbs_cancel_succ));
                    } else {
                        coinBBSListModel.setKeepCount(coinBBSListModel.getKeepCount() + 1);
                        UITipDialog.showSuccess(mActivity, getString(R.string.bbs_focuse_on_succ));
                        coinBBSListModel.setIsKeep("1");
                    }

                    adapter.notifyItemChanged(position);
                }

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }


}
