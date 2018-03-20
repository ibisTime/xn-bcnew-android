package com.cdkj.link_community.module.market;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.base.AbsRefreshListFragment;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.baselibrary.databinding.LayoutCommonRecyclerRefreshBinding;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.interfaces.RefreshHelper;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.adapters.MarketChooseListAdapter;
import com.cdkj.link_community.databinding.FragmentMarketBinding;
import com.cdkj.link_community.databinding.PhotoEmptyViewBinding;
import com.cdkj.link_community.interfaces.DataEmptyToPhotoCallBack;

import java.util.List;

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
        mRefreshHelper.setData(null);
        return mRefreshBinding.getRoot();
    }

    /**
     * 初始化刷新相关
     */
    protected void initRefreshHelper(int limit) {
        mRefreshHelper = new RefreshHelper(mActivity, new DataEmptyToPhotoCallBack() {
            @Override
            public View getRefreshLayout() {
                mRefreshBinding.refreshLayout.setEnableRefresh(false);
                mRefreshBinding.refreshLayout.setEnableLoadmore(false);
                return mRefreshBinding.refreshLayout;
            }

            @Override
            public RecyclerView getRecyclerView() {
                return mRefreshBinding.rv;
            }

            @Override
            public RecyclerView.Adapter getAdapter(List listData) {
                listData.add("");
                listData.add("");
                listData.add("");
                listData.add("");
                listData.add("");
                listData.add("");
                listData.add("");
                listData.add("");
                listData.add("");
                listData.add("");
                return new MarketChooseListAdapter(listData);
            }

            @Override
            public View getEmptyView() {

                PhotoEmptyViewBinding emptyViewBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.photo_empty_view, null, false);

                emptyViewBinding.img.setImageResource(R.drawable.add_market_big);

                emptyViewBinding.img.setOnClickListener(view -> emptyViewClick());

                return emptyViewBinding.getRoot();
            }

            @Override
            public void getListDataRequest(int pageindex, int limit, boolean isShowDialog) {

            }
        });
        mRefreshHelper.init(limit);

    }


    /**
     * 数据为空时图片点击
     */
    private void emptyViewClick() {

    }


    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void onInvisible() {

    }
}
