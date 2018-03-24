package com.cdkj.link_community.module.market;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.AbsRefreshListFragment;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.baselibrary.databinding.LayoutCommonRecyclerRefreshBinding;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.interfaces.RefreshHelper;
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
                return new MarketChooseListAdapter(listData);
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
                Map<String, String> map = new HashMap<>();

                map.put("start", pageindex + "");
                map.put("limit", limit + "");
                map.put("userId", SPUtilHelpr.getUserId());

                if (isShowDialog) showLoadingDialog();

                Call call = RetrofitUtils.createApi(MyApiServer.class).getCoinList("628336", StringUtils.getJsonToString(map));

                addCall(call);

                call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<CoinListModel>>(mActivity) {
                    @Override
                    protected void onSuccess(ResponseInListModel<CoinListModel> data, String SucMessage) {

                        mRefreshHelper.setData(data.getList());
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
        });
        mRefreshHelper.init(limit);

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

    }

    @Override
    protected void onInvisible() {

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

}
