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
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.appmanager.WrapContentLinearLayoutManager;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.interfaces.RefreshHelper;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.adapters.MarketChooseListAdapter;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.databinding.FragmentMarketListHeaderBinding;
import com.cdkj.link_community.databinding.FragmentMarketMyChooseBinding;
import com.cdkj.link_community.databinding.PhotoEmptyViewBinding;
import com.cdkj.link_community.interfaces.DataEmptyToPhotoCallBack;
import com.cdkj.link_community.model.LoinSucc;
import com.cdkj.link_community.model.MarketInterval;
import com.cdkj.link_community.model.MyChooseMarket;
import com.cdkj.link_community.utils.DiffCallBackMyChoose;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 自选Fragment
 * Created by cdkj on 2018/3/13.
 */

public class MyChooseFragment extends BaseLazyFragment {

    protected FragmentMarketMyChooseBinding mRefreshBinding;

    private FragmentMarketListHeaderBinding mHeaderBinding;

    private boolean isRequesting;//是否正在请求中 用于轮询判断

    protected RefreshHelper mRefreshHelper;

    private String direction = "";

    // 是否需要完全刷新（不比较新老）列表
    private boolean isClearRefresh = false;

    private MarketChooseListAdapter marketChooseListAdapter;
    private List<MyChooseMarket> mCoinListModels = new ArrayList<>();

    public static MyChooseFragment getInstance() {
        MyChooseFragment fragment = new MyChooseFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRefreshBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_market_my_choose, null, false);

        mHeaderBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_market_list_header, null, false);

        initListener();
        initRefreshHelper(10);

        //防止局部刷新闪烁
        mRefreshBinding.refreshLayout.setEnableLoadmore(false);
        ((DefaultItemAnimator) mRefreshBinding.rv.getItemAnimator()).setSupportsChangeAnimations(false);
        mRefreshBinding.rv.setLayoutManager(new WrapContentLinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));

        if (SPUtilHelper.isLoginNoStart()) { //未登录不用请求
            mRefreshHelper.onDefaluteMRefresh(true);
        } else {
            mRefreshHelper.setData(new ArrayList());
        }

        return mRefreshBinding.getRoot();
    }

    private void initListener() {
        mHeaderBinding.cbUp.setOnClickListener((v) -> {

            if (mHeaderBinding.cbUp.isChecked()) {
                mHeaderBinding.cbDown.setChecked(false);
                mHeaderBinding.cbWarn.setChecked(false);

                direction = "1";
            } else {
                direction = "";
            }

            isClearRefresh = true;
            mRefreshHelper.onDefaluteMRefresh(true);

        });

        mHeaderBinding.cbDown.setOnClickListener((v) -> {
            if (mHeaderBinding.cbDown.isChecked()) {
                mHeaderBinding.cbUp.setChecked(false);
                mHeaderBinding.cbWarn.setChecked(false);

                direction = "0";
            } else {
                direction = "";
            }

            isClearRefresh = true;
            mRefreshHelper.onDefaluteMRefresh(true);
        });

        mHeaderBinding.cbWarn.setOnClickListener((v) -> {

            if (!SPUtilHelper.isLogin(mActivity, false)) {
                mHeaderBinding.cbWarn.setChecked(false);
            } else {
                if (mHeaderBinding.cbWarn.isChecked()) {
                    mHeaderBinding.cbDown.setChecked(false);
                    mHeaderBinding.cbUp.setChecked(false);

                    direction = "2";
                } else {
                    direction = "";
                }

                isClearRefresh = true;
                mRefreshHelper.onDefaluteMRefresh(true);
            }

        });

    }

    /**
     * 初始化刷新相关
     */
    protected void initRefreshHelper(int limit) {
        mRefreshHelper = new RefreshHelper(mActivity, new DataEmptyToPhotoCallBack() {
            @Override
            public View getRefreshLayout() {
                return mRefreshBinding.refreshLayout;
            }

            @Override
            public void onRefresh(int pageindex, int limit) {
                isClearRefresh = true;
                super.onRefresh(pageindex, limit);
            }

            @Override
            public RecyclerView getRecyclerView() {
                return mRefreshBinding.rv;
            }

            @Override
            public RecyclerView.Adapter getAdapter(List listData) {
                marketChooseListAdapter = new MarketChooseListAdapter(listData);
                marketChooseListAdapter.setHeaderAndEmpty(true);
                marketChooseListAdapter.setHeaderView(mHeaderBinding.getRoot());
                marketChooseListAdapter.setOnItemChildClickListener((adapter1, view, position) -> {
                    switch (view.getId()) {
                        case R.id.btn_to_top: //置顶
                            if (position == 0) {
                                UITipDialog.showSuccess(mActivity, getString(R.string.set_to_top_succ));
                                return;
                            }
                            toTopAddMarketRequest(marketChooseListAdapter, position);
                            break;
                        case R.id.btn_delete: //删除
                            removeAddMarketRequest(marketChooseListAdapter, position);
                            break;

                        case R.id.content:
                            MyChooseMarket model = marketChooseListAdapter.getItem(position);
                            MarketActivity.open(mActivity, model.getId());
                            break;
                    }
                });
                return marketChooseListAdapter;
            }

            @Override
            public View getEmptyView() {

                PhotoEmptyViewBinding emptyViewBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.photo_empty_view, null, false);

                emptyViewBinding.img.setImageResource(R.drawable.add_market_big);

                emptyViewBinding.img.setOnClickListener(view -> addClick());

                return emptyViewBinding.getRoot();
            }

            @Override
            public void getListDataRequest(int pageindex, int limit, boolean isShowDialog) {
                getChooseListRequest(pageindex, limit, isShowDialog);

            }

        });
        mRefreshHelper.init(limit);

    }

    /**
     * 自选删除操作
     *
     * @param adapter
     * @param position
     */
    private void removeAddMarketRequest(MarketChooseListAdapter adapter, int position) {

        if (adapter == null || position > adapter.getData().size()) {
            return;
        }

        MyChooseMarket market = adapter.getItem(position);

        if (market == null || TextUtils.isEmpty(market.getId())) {
            return;
        }

        Map<String, String> map = new HashMap<>();

        map.put("id", market.getChoiceId() + "");

        Call call = RetrofitUtils.getBaseAPiService().successRequest("628332", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(mActivity) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {

                if (data.isSuccess()) {
                    adapter.remove(position);
                    if (adapter.getData().size() == 0) {
                        adapter.notifyDataSetChanged();
                    }
                    UITipDialog.showSuccess(mActivity, getString(R.string.delete_succ));
                }
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    /**
     * 自选置顶操作
     *
     * @param adapter
     * @param position
     */
    private void toTopAddMarketRequest(MarketChooseListAdapter adapter, int position) {

        if (adapter == null || position > adapter.getData().size()) {
            return;
        }

        MyChooseMarket market = adapter.getItem(position);

        if (market == null || TextUtils.isEmpty(market.getId())) {
            return;
        }

        Map<String, String> map = new HashMap<>();

        map.put("id", market.getId());

        Call call = RetrofitUtils.getBaseAPiService().successRequest("628331", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(mActivity) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {

                if (data.isSuccess()) {
                    adapter.remove(position);
                    adapter.addData(0, market);
                    adapter.notifyDataSetChanged();
                    UITipDialog.showSuccess(mActivity, getString(R.string.set_to_top_succ));
                }
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    /**
     * 获取自选列表请求
     *
     * @param pageindex
     * @param limit
     * @param isShowDialog
     */
    private void getChooseListRequest(int pageindex, int limit, boolean isShowDialog) {

        if (!SPUtilHelper.isLoginNoStart()) {
            mRefreshHelper.setData(new ArrayList());
            return;
        }


        Map<String, String> map = new HashMap<>();

        map.put("direction", direction);
        map.put("start", pageindex + "");
        map.put("limit", limit + "");
        map.put("userId", SPUtilHelper.getUserId());

        if (isShowDialog) showLoadingDialog();

        Call call = RetrofitUtils.createApi(MyApiServer.class).getMyChooseMarketList("628351", StringUtils.getJsonToString(map));

        addCall(call);
        isRequesting = true;
        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<MyChooseMarket>>(mActivity) {
            @Override
            protected void onSuccess(ResponseInListModel<MyChooseMarket> data, String SucMessage) {

                if (marketChooseListAdapter.getData() == null || marketChooseListAdapter.getData().size() == 0) {
                    mHeaderBinding.llRoot.setVisibility(View.GONE);
                } else {
                    mHeaderBinding.llRoot.setVisibility(View.VISIBLE);
                }

                if (marketChooseListAdapter.getData() == null || marketChooseListAdapter.getData().size() == 0 || isClearRefresh) {

                    mRefreshHelper.setDataAsync(data.getList(), getString(R.string.no_coin_info), R.drawable.no_dynamic);
                } else {

                    if (mRefreshBinding.refreshLayout.isRefreshing()) { //停止刷新
                        mRefreshBinding.refreshLayout.finishRefresh();
                    }

                    if (marketChooseListAdapter.getData().size() == data.getList().size()) {
                        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallBackMyChoose(mCoinListModels, data.getList()), true);
                        diffResult.dispatchUpdatesTo(marketChooseListAdapter);
                    } else {
                        marketChooseListAdapter.notifyDataSetChanged();
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
     * 数据为空时图片点击
     */
    private void addClick() {
        if (!SPUtilHelper.isLogin(mActivity, false)) {
            return;
        }
        AddMarketActivity.open(mActivity);
    }


    @Override
    protected void lazyLoad() {
        if (mRefreshBinding == null || mRefreshHelper == null) return;
        mRefreshHelper.onDefaluteMRefresh(false);
    }

    @Override
    protected void onInvisible() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mRefreshHelper != null && getUserVisibleHint()) {
            mRefreshHelper.onDefaluteMRefresh(true);
        }
    }

    /**
     * 登录
     *
     * @param loinSucc
     */
    @Subscribe
    public void LoginSuccEvent(LoinSucc loinSucc) {
        if (mRefreshHelper != null) {
            if (mActivity == null || mActivity.isFinishing() || !SPUtilHelper.isLoginNoStart()) {
                return;
            }
            mRefreshHelper.onDefaluteMRefresh(false);
        }
    }

    /**
     * 轮询刷新
     *
     * @param
     */
    @Subscribe
    public void IntervalRefreshEvent(MarketInterval marketInterval) {
        //当前页面不显示时停止轮询
        if (mActivity == null || mActivity.isFinishing() || !getUserVisibleHint() || getParentFragment() == null || !getParentFragment().getUserVisibleHint()) {
            return;
        }
        
        if (!SPUtilHelper.isLoginNoStart() || mRefreshHelper == null || isRequesting) {
            return;
        }
        LogUtil.E("刷新 MyChooseFragment");
        mRefreshHelper.onMRefresh(false);
    }


}
