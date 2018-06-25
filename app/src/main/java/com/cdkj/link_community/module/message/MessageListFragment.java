package com.cdkj.link_community.module.message;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.base.AbsRefreshListFragment;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.adapters.MessageListAdapter;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.databinding.HeaderMessageListBinding;
import com.cdkj.link_community.loader.BannerImageLoader;
import com.cdkj.link_community.model.BannerModel;
import com.cdkj.link_community.model.MessageModel;
import com.cdkj.link_community.model.event.MessageBannerState;
import com.cdkj.link_community.module.active.ActiveDetailsActivity;
import com.youth.banner.BannerConfig;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 资讯列表
 * Created by cdkj on 2018/3/19.
 */

public class MessageListFragment extends AbsRefreshListFragment {

    private HeaderMessageListBinding mHeaderBinding;

    private String mMessageType;//资讯类型
    private boolean mIsFristRequest;//是否创建时就进行请求
    private String mTitle;//标题

    private final static String ISFRISTREQUEST = "isFristRequest";//是否创建时就进行请求

    private List<String> banner = new ArrayList<>();
    private List<String> bannerTitle = new ArrayList<>();
    private List<BannerModel> bannerData = new ArrayList<>();

    /**
     * @param type 资讯类型
     * @return
     */
    public static MessageListFragment getInstance(String type, boolean isFristRequest, String title) {
        MessageListFragment fragment = new MessageListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CdRouteHelper.DATA_SIGN, type);
        bundle.putBoolean(ISFRISTREQUEST, isFristRequest);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void afterCreate(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mHeaderBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.header_message_list, null, false);

        if (getArguments() != null) {
            mMessageType = getArguments().getString(CdRouteHelper.DATA_SIGN);
            mIsFristRequest = getArguments().getBoolean(ISFRISTREQUEST);
        }

        initRefreshHelper(MyCdConfig.LISTLIMIT);

        if (mIsFristRequest) {
            mRefreshHelper.onDefaluteMRefresh(false);
        }
    }


    @Override
    public RecyclerView.Adapter getListAdapter(List listData) {

        MessageListAdapter msgAdapter = new MessageListAdapter(listData, mActivity);
        msgAdapter.setHeaderAndEmpty(true);

        if (mIsFristRequest) {
            msgAdapter.addHeaderView(mHeaderBinding.getRoot());
        }

        msgAdapter.setOnItemClickListener((adapter, view, position) -> {
            MessageDetailsActivity.open(mActivity, msgAdapter.getItem(position).getCode());
        });

        return msgAdapter;
    }

    @Override
    public void getListRequest(int pageindex, int limit, boolean isShowDialog) {

        Map<String, String> map = new HashMap<>();

        map.put("type", mMessageType);
        map.put("start", pageindex + "");
        map.put("limit", limit + "");

        if (isShowDialog) showLoadingDialog();

        Call call = RetrofitUtils.createApi(MyApiServer.class).getMsgList("628205", StringUtils.getJsonToString(map));

        addCall(call);


        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<MessageModel>>(mActivity) {
            @Override
            protected void onSuccess(ResponseInListModel<MessageModel> data, String SucMessage) {
                mRefreshHelper.setData(data.getList(), getString(R.string.no_msg), R.drawable.no_dynamic);
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                super.onReqFailure(errorCode, errorMessage);
                mRefreshHelper.loadError(errorMessage, 0);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
                getBanner();
            }
        });

    }

    /**
     * 获取banner
     */
    private void getBanner() {
        Map<String, String> map = new HashMap<>();
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("companyCode", MyCdConfig.COMPANYCODE);

        Call call = RetrofitUtils.createApi(MyApiServer.class).getBanner("805806", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseListCallBack<BannerModel>(mActivity) {

            @Override
            protected void onSuccess(List<BannerModel> data, String SucMessage) {
                if (data != null) {
                    bannerData = data;
                    banner.clear();
                    bannerTitle.clear();
                    for (BannerModel model : data) {
                        banner.add(model.getPic());
                        bannerTitle.add(model.getName());
                    }
                }

                initBanner();

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    private void initBanner() {
        if (banner == null) return;

        //设置banner样式
        mHeaderBinding.banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片加载器
        mHeaderBinding.banner.setImageLoader(new BannerImageLoader());
        //设置图片集合
//        mHeaderBinding.banner.setImages(banner);
        mHeaderBinding.banner.update(banner, bannerTitle);
        //设置banner动画效果
        mHeaderBinding.banner.setBannerAnimation(com.youth.banner.Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
//        banner.setBannerTitles(Arrays.asList(titles));
        //设置自动轮播，默认为true
        mHeaderBinding.banner.isAutoPlay(true);
        //设置轮播时间
        mHeaderBinding.banner.setDelayTime(3500);
        //设置指示器位置（当banner模式中有指示器时）
        mHeaderBinding.banner.setIndicatorGravity(BannerConfig.CENTER);
        //设置banner点击事件
        mHeaderBinding.banner.setOnBannerClickListener(position -> {

            if (bannerData.get(position - 1).getContentType() == null)
                return;

            if (bannerData.get(position - 1).getUrl() == null) {
                return;
            }

            switch (bannerData.get(position - 1).getContentType()) {

                case "1":
                    if (bannerData.get(position - 1).getUrl().indexOf("http") != -1) {
                        CdRouteHelper.openWebViewActivityForUrl(bannerData.get(position - 1).getName(), bannerData.get(position - 1).getUrl());
                    }
                    break;

                case "2":
                    MessageDetailsActivity.open(mActivity, bannerData.get(position - 1).getUrl());
                    break;

                case "3":
                    ActiveDetailsActivity.open(mActivity, bannerData.get(position - 1).getUrl());
                    break;
            }

        });
        //banner设置方法全部调用完毕时最后调用
        mHeaderBinding.banner.start();

        // 设置在操作Banner时listView事件不触发
//        mHeaderBinding.banner.setOnPageChangeListener(new MyPageChangeListener());

    }


    @Override
    protected void lazyLoad() {

        if (mRefreshHelper == null) {
            return;
        }
        if (banner != null && banner.size() > 0) {
            mHeaderBinding.banner.startAutoPlay();
        }
        if (mIsFristRequest) {
            return;
        }

        mRefreshHelper.onDefaluteMLoadMore(true);
    }

    @Override
    protected void onInvisible() {
        if (mHeaderBinding != null) {
            mHeaderBinding.banner.stopAutoPlay();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (banner != null && banner.size() > 0 && mHeaderBinding != null) {
            mHeaderBinding.banner.startAutoPlay();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mHeaderBinding != null) {
            mHeaderBinding.banner.stopAutoPlay();
        }
    }


    @Override
    public void onDestroy() {
        if (mHeaderBinding != null) {
            mHeaderBinding.banner.stopAutoPlay();
        }
        super.onDestroy();
    }

    /**
     * @param bannerStop
     */
    @Subscribe
    public void bannerStopEvent(MessageBannerState bannerStop) {  //接收事件通知 开启和关闭banner

        if (mHeaderBinding == null) return;

        if (!bannerStop.isStop()) {
            if (banner != null && banner.size() > 0) {
                mHeaderBinding.banner.startAutoPlay();
            }
        } else {
            mHeaderBinding.banner.stopAutoPlay();
        }
    }


}
