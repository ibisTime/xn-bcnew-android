package com.cdkj.link_community.module.market;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.AbsRefreshListFragment;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.baselibrary.databinding.LayoutCommonRecyclerRefreshBinding;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.interfaces.RefreshHelper;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.adapters.MarketChooseListAdapter;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.databinding.FragmentMarketBinding;
import com.cdkj.link_community.databinding.PhotoEmptyViewBinding;
import com.cdkj.link_community.interfaces.DataEmptyToPhotoCallBack;
import com.cdkj.link_community.model.CoinListModel;
import com.cdkj.link_community.model.LoinSucc;
import com.cdkj.link_community.model.MarketInterval;
import com.cdkj.link_community.model.MyChooseMarket;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 自选Fragment
 * Created by cdkj on 2018/3/13.
 */

public class MyChooseFragment extends BaseLazyFragment {

    protected LayoutCommonRecyclerRefreshBinding mRefreshBinding;

    private boolean isRequesting;//是否正在请求中 用于轮询判断

    protected RefreshHelper mRefreshHelper;

    public static MyChooseFragment getInstanse() {
        MyChooseFragment fragment = new MyChooseFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRefreshBinding = DataBindingUtil.inflate(getLayoutInflater(), com.cdkj.baselibrary.R.layout.layout_common_recycler_refresh, null, false);
        initRefreshHelper(10);

        if (SPUtilHelpr.isLoginNoStart()) { //未登录不用请求
            mRefreshHelper.onDefaluteMRefresh(true);
        }

        return mRefreshBinding.getRoot();
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
            public RecyclerView getRecyclerView() {
                return mRefreshBinding.rv;
            }

            @Override
            public RecyclerView.Adapter getAdapter(List listData) {
                MarketChooseListAdapter adapter = new MarketChooseListAdapter(listData);
                adapter.setOnItemChildClickListener((adapter1, view, position) -> {
                    switch (view.getId()) {
                        case R.id.btn_to_top: //置顶
                            if (position == 0) {
                                UITipDialog.showSuccess(mActivity, getString(R.string.set_to_top_succ));
                                return;
                            }
                            toTopAddMarketRequest(adapter, position);
                            break;
                        case R.id.btn_delete: //删除
                            removeAddMarketRequest(adapter, position);
                            break;
                    }
                });
                return adapter;
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

        map.put("id", market.getId());

        Call call = RetrofitUtils.getBaseAPiService().successRequest("628332", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(mActivity) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {

                if (data.isSuccess()) {
                    adapter.remove(position);
                    if (adapter.getData().isEmpty()) {
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

        if (!SPUtilHelpr.isLoginNoStart()) {
            return;
        }

        Map<String, String> map = new HashMap<>();

        map.put("start", pageindex + "");
        map.put("limit", limit + "");
        map.put("userId", SPUtilHelpr.getUserId());

        if (isShowDialog) showLoadingDialog();

        Call call = RetrofitUtils.createApi(MyApiServer.class).getMyChooseMarketList("628336", StringUtils.getJsonToString(map));

        addCall(call);
        isRequesting = true;
        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<MyChooseMarket>>(mActivity) {
            @Override
            protected void onSuccess(ResponseInListModel<MyChooseMarket> data, String SucMessage) {
                mRefreshHelper.setData(data.getList());
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
        if (!SPUtilHelpr.isLogin(mActivity, false)) {
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
            if (mActivity == null || mActivity.isFinishing() || !SPUtilHelpr.isLoginNoStart()) {
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
        if (mActivity == null || mActivity.isFinishing() || !SPUtilHelpr.isLoginNoStart() || mRefreshHelper == null || isRequesting) {
            return;
        }
        mRefreshHelper.onDefaluteMRefresh(false);
    }


}
