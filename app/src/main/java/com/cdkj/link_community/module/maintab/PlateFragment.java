package com.cdkj.link_community.module.maintab;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.interfaces.RefreshHelper;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.views.ScrollGridLayoutManager;
import com.cdkj.link_community.R;
import com.cdkj.link_community.adapters.AllPlateAdapter;
import com.cdkj.link_community.adapters.HotPlateAdapter;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.databinding.FragmentPlateBinding;
import com.cdkj.link_community.model.PlateLlistModel;
import com.cdkj.link_community.module.plate.PlateDetailsActivity;
import com.cdkj.link_community.module.search.SearchActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 版块
 * Created by cdkj on 2018/6/12.
 */

public class PlateFragment extends BaseLazyFragment {

    private FragmentPlateBinding mBinding;
    private HotPlateAdapter mHotPlateAdapter;
    private AllPlateAdapter mAllPlateAdapter;

    private RefreshHelper mRefreshHelper;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_plate, null, false);
        mBinding.refreshLayout.setVisibility(View.GONE);
        iniRefreshHelper();
        initRecyclerView();

        mBinding.fraToSearch.setOnClickListener(view -> SearchActivity.open(mActivity));

        return mBinding.getRoot();
    }

    public static PlateFragment getInstance() {
        PlateFragment fragment = new PlateFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    /**
     * 初始化热门版块recyclerView
     */
    private void initRecyclerView() {

        mBinding.recyclerHot.setLayoutManager(new ScrollGridLayoutManager(mActivity, 3));

        mHotPlateAdapter = new HotPlateAdapter(new ArrayList<>());

        mBinding.recyclerHot.setAdapter(mHotPlateAdapter);

        mBinding.recyclerAll.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });


        mHotPlateAdapter.setOnItemClickListener((adapter, view, position) -> {
            PlateLlistModel plateLlistModel = (PlateLlistModel) adapter.getItem(position);
            PlateDetailsActivity.open(mActivity, plateLlistModel.getCode());

        });
    }

    /**
     * 初始化刷新操作
     */
    private void iniRefreshHelper() {

        mRefreshHelper = new RefreshHelper(mActivity, new BaseRefreshCallBack(mActivity) {
            @Override
            public View getRefreshLayout() {
                return mBinding.refreshLayout;
            }

            @Override
            public RecyclerView getRecyclerView() {
                return mBinding.recyclerAll;
            }

            @Override
            public RecyclerView.Adapter getAdapter(List listData) {
                mAllPlateAdapter = new AllPlateAdapter(listData);
                mAllPlateAdapter.setOnItemClickListener((adapter, view, position) -> {
                    PlateLlistModel plateLlistModel = (PlateLlistModel) adapter.getItem(position);
                    PlateDetailsActivity.open(mActivity, plateLlistModel.getCode());
                });
                return mAllPlateAdapter;
            }

            @Override
            public void onRefresh(int pageindex, int limit) {
                super.onRefresh(pageindex, limit);
                getHotPlateListDataRequest(false);
            }

            @Override
            public void getListDataRequest(int pageindex, int limit, boolean isShowDialog) {
                getAllPlaceListRequest(pageindex, limit, isShowDialog);
            }

            @Override
            public void reLoad() {
                mRefreshHelper.onDefaluteMRefresh(true);
            }
        });

        mRefreshHelper.init(10);
    }

    @Override
    protected void lazyLoad() {
        if (mBinding == null) {
            return;
        }
        getHotPlateListDataRequest(true);

    }

    @Override
    protected void onInvisible() {

    }

    /**
     * 获取热门版块
     */
    public void getHotPlateListDataRequest(boolean isShowDialog) {

        Map<String, String> map = new HashMap<>();

        map.put("start", "1");
        map.put("limit", "6");
        map.put("location", "1");

        if (isShowDialog) {
            showLoadingDialog();
        }

        Call<BaseResponseModel<ResponseInListModel<PlateLlistModel>>> call = RetrofitUtils.createApi(MyApiServer.class).getPlateList("628615", StringUtils.getJsonToString(map));

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<PlateLlistModel>>(mActivity) {
            @Override
            protected void onSuccess(ResponseInListModel<PlateLlistModel> data, String SucMessage) {
                mHotPlateAdapter.replaceData(data.getList());

            }

            @Override
            protected void onFinish() {
                if (isShowDialog) {
                    disMissLoading();
                }
                getAllPlaceListRequest(1, 10, false);
            }
        });

    }

    /**
     * 获取所有版块
     */
    public void getAllPlaceListRequest(int pageindex, int limit, boolean isShowDialog) {

        Map<String, String> map = new HashMap<>();

        map.put("start", pageindex + "");
        map.put("limit", limit + "");

        Call<BaseResponseModel<ResponseInListModel<PlateLlistModel>>> call = RetrofitUtils.createApi(MyApiServer.class).getPlateList("628615", StringUtils.getJsonToString(map));

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<PlateLlistModel>>(mActivity) {
            @Override
            protected void onSuccess(ResponseInListModel<PlateLlistModel> data, String SucMessage) {
                mRefreshHelper.setData(data.getList(), "暂无行情", R.drawable.no_dynamic);
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                mRefreshHelper.loadError(errorMessage, 0);
            }

            @Override
            protected void onFinish() {
                if (mBinding.refreshLayout.getVisibility() != View.VISIBLE) {
                    mBinding.refreshLayout.setVisibility(View.VISIBLE);
                }
            }
        });

    }

}
