package com.cdkj.link_community.module.market;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.appmanager.WrapContentLinearLayoutManager;
import com.cdkj.baselibrary.base.AbsRefreshListFragment;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.adapters.PlatformListAdapter;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.databinding.FragmentMarketListHeaderBinding;
import com.cdkj.link_community.model.CoinListModel;
import com.cdkj.link_community.model.MarketInterval;
import com.cdkj.link_community.utils.DiffCallBack;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 添加行情 列表Fragment
 * Created by cdkj on 2018/3/24.
 */

public class PlatformListFragment extends AbsRefreshListFragment {


    private FragmentMarketListHeaderBinding mHeaderBinding;

    private boolean isFirstRequest;//是否进行了第一次请求
    private String mPlatformType;

    private boolean isRequesting;//是否正在请求中 用于轮询判断
    private PlatformListAdapter platformListAdapter;

    private String direction = "";

    private List<CoinListModel> mCoinListModels = new ArrayList<>();

    // 是否需要完全刷新（不比较）列表
    private boolean isClearRefresh = false;

    /**
     * @param platformType 币种类型
     * @return
     */
    public static PlatformListFragment getInstance(String platformType, Boolean isFirstRequest) {
        PlatformListFragment fragment = new PlatformListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CdRouteHelper.DATA_SIGN, platformType);
        bundle.putBoolean("isFirstRequest", isFirstRequest);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void lazyLoad() {
        if (mRefreshBinding == null) return;
        if (!isFirstRequest) {
            isFirstRequest = true;
            mRefreshHelper.onDefaluteMRefresh(true);
        }
    }


    @Override
    protected void onInvisible() {

    }


    @Override
    protected void afterCreate(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (getArguments() != null) {
            mPlatformType = getArguments().getString(CdRouteHelper.DATA_SIGN);
            isFirstRequest = getArguments().getBoolean("isFirstRequest");
        }

        mHeaderBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_market_list_header, null, false);

        initRefreshHelper(MyCdConfig.LISTLIMIT);

        //防止局部刷新闪烁
        ((DefaultItemAnimator) mRefreshBinding.rv.getItemAnimator()).setSupportsChangeAnimations(false);
        mRefreshBinding.rv.setLayoutManager(new WrapContentLinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL,false));

        if (isFirstRequest) {
            mRefreshHelper.onDefaluteMRefresh(true);
        }

        mRefreshBinding.refreshLayout.setEnableLoadmore(false);

        initListener();


    }

    private void initListener() {
        mHeaderBinding.cbUp.setOnClickListener((v) -> {

            if (mHeaderBinding.cbUp.isChecked()){
                mHeaderBinding.cbDown.setChecked(false);
                mHeaderBinding.cbWarn.setChecked(false);

                direction = "1";
            }else {
                direction = "";
            }

            isClearRefresh = true;
            mRefreshHelper.onDefaluteMRefresh(true);
        });

        mHeaderBinding.cbDown.setOnClickListener((v) -> {

            if (mHeaderBinding.cbDown.isChecked()){
                mHeaderBinding.cbUp.setChecked(false);
                mHeaderBinding.cbWarn.setChecked(false);

                direction = "0";
            }else {
                direction = "";
            }

            isClearRefresh = true;
            mRefreshHelper.onDefaluteMRefresh(true);
        });

        mHeaderBinding.cbWarn.setOnClickListener((v) -> {

            if (!SPUtilHelper.isLogin(mActivity, false)) {
                mHeaderBinding.cbWarn.setChecked(false);
            }else {
                if (mHeaderBinding.cbWarn.isChecked()){
                    mHeaderBinding.cbDown.setChecked(false);
                    mHeaderBinding.cbUp.setChecked(false);

                    direction = "2";
                }else {
                    direction = "";
                }

                isClearRefresh = true;
                mRefreshHelper.onDefaluteMRefresh(true);
            }


        });

    }



    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
        }
    }

    @Override
    public RecyclerView.Adapter getListAdapter(List listData) {
        platformListAdapter = new PlatformListAdapter(listData);
        platformListAdapter.setHeaderAndEmpty(true);
        platformListAdapter.setHeaderView(mHeaderBinding.getRoot());
        platformListAdapter.setOnItemClickListener((adapter, view, position) -> {
            CoinListModel model = platformListAdapter.getItem(position);
            MarketActivity.open(mActivity, model.getId());
        });
        return platformListAdapter;
    }

    @Override
    public void getListRequest(int pageindex, int limit, boolean isShowDialog) {

        if (TextUtils.isEmpty(mPlatformType)) return;

        if (TextUtils.equals(direction, "2")){
            if (TextUtils.isEmpty(SPUtilHelper.getUserId()))
                return;
        }

        if(mRefreshBinding.rv.getScrollState() != 0){
            //recycleView正在滑动, 不进行刷新
            return;
        }

        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelper.getUserId());
        map.put("percentPeriod", "24h");
        map.put("exchangeEname", mPlatformType);
        map.put("direction", direction);
        map.put("start", pageindex + "");
        map.put("limit",  "20000");
        if (isShowDialog) showLoadingDialog();

        Call call = RetrofitUtils.createApi(MyApiServer.class).getCoinList("628350", StringUtils.getJsonToString(map));

        addCall(call);
        isRequesting = true;
        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<CoinListModel>>(mActivity) {
            @Override
            protected void onSuccess(ResponseInListModel<CoinListModel> data, String SucMessage) {

                if (platformListAdapter.getData() == null || platformListAdapter.getData().size() == 0 || isClearRefresh) {
                    mRefreshHelper.setDataAsync(data.getList(), getString(R.string.no_platform_info), R.drawable.no_dynamic);
                }else {
                    //
                    mRefreshBinding.refreshLayout.finishRefresh();

                    if(platformListAdapter.getData().size() == data.getList().size()){
                        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallBack(mCoinListModels, data.getList()), false);
                        diffResult.dispatchUpdatesTo(platformListAdapter);
                    }else {
                        platformListAdapter.notifyDataSetChanged();
                    }
                }

                // 重置列表新老比较
                isClearRefresh = false;

                mCoinListModels.clear();
                mCoinListModels.addAll(data.getList());

            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                super.onReqFailure(errorCode, errorMessage);
                mRefreshHelper.loadError(errorMessage, 0);
            }

            @Override
            protected void onFinish() {
                isRequesting = false;
                disMissLoading();
            }
        });

    }

    /**
     * 轮询刷新
     *
     * @param
     */
    @Subscribe
    public void IntervalRefreshEvent(MarketInterval marketInterval) {

        if (mActivity == null || mActivity.isFinishing() || !getUserVisibleHint() || mRefreshHelper == null || isRequesting || TextUtils.isEmpty(SPUtilHelper.getUserId())) {
            return;
        }

        mRefreshHelper.onMRefresh(false);
    }

}
