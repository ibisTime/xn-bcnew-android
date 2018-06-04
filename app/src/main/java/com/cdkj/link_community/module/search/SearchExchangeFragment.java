package com.cdkj.link_community.module.search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsRefreshListFragment;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.adapters.SearchMarketListAdapter;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.model.CoinListModel;
import com.cdkj.link_community.model.event.EventSearch;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by cdkj on 2018/5/17.
 */

public class SearchExchangeFragment extends AbsRefreshListFragment {

    private SearchMarketListAdapter searchMarketListAdapter;

    // 默认一个不可能查出来的
    private String mSearchKey= "...";

    public static SearchExchangeFragment getInstance() {
        SearchExchangeFragment fragment = new SearchExchangeFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void onInvisible() {

    }

    @Override
    protected void afterCreate(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initRefreshHelper(MyCdConfig.LISTLIMIT);
    }

    @Override
    public RecyclerView.Adapter getListAdapter(List listData) {
        searchMarketListAdapter = new SearchMarketListAdapter(listData);

        searchMarketListAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (!SPUtilHelper.isLogin(mActivity, false)) {
                return;
            }
            addMarketRequest(searchMarketListAdapter, position);

        });

        return searchMarketListAdapter;
    }

    @Override
    public void getListRequest(int pageindex, int limit, boolean isShowDialog) {

        if (mSearchKey == null) return;

        Map<String, String> map = new HashMap<>();

        map.put("percentPeriod", "24h");
        map.put("keywords", mSearchKey);
        map.put("start", pageindex + "");
        map.put("limit", limit + "");
        map.put("userId", SPUtilHelper.getUserId());

        if (isShowDialog) showLoadingDialog();

        Call call = RetrofitUtils.createApi(MyApiServer.class).getCoinList("628350", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<CoinListModel>>(mActivity) {
            @Override
            protected void onSuccess(ResponseInListModel<CoinListModel> data, String SucMessage) {
                mRefreshHelper.setData(data.getList(), getString(R.string.no_search_info), 0);
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
     * 添加自选
     *
     * @param
     */
    public void addMarketRequest(SearchMarketListAdapter addMarketListAdapter, int position) {
        CoinListModel model = addMarketListAdapter.getItem(position);

        if (model == null) return;

        if (TextUtils.equals(model.getIsChoice(), "1")) {
            UITipDialog.showInfo(mActivity, getString(R.string.you_have_add));
            return;
        }

        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelper.getUserId());
        map.put("exchangeEname", model.getExchangeEname());
        map.put("toSymbol", model.getToSymbol());
        map.put("symbol", model.getSymbol());

        Call call = RetrofitUtils.getBaseAPiService().successRequest("628330", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(mActivity) {

            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {
                    model.setIsChoice("1");
                    addMarketListAdapter.notifyItemChanged(position);
                    UITipDialog.showSuccess(mActivity, getString(R.string.add_market_succ));
                }
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    @Subscribe
    public void startSearch(EventSearch eventSearch){

//        if (eventSearch.getLocation() == 1){
            mSearchKey = eventSearch.getContent();

            mRefreshHelper.onDefaluteMRefresh(true);
//        }

    }
}
