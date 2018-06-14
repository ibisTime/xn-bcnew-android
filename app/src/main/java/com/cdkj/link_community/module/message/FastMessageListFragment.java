package com.cdkj.link_community.module.message;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsRefreshListFragment;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.adapters.FastMessageListAdapter;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.model.FastMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 快讯列表
 * Created by cdkj on 2018/3/13.
 */

public class FastMessageListFragment extends AbsRefreshListFragment {

    public static final int HOTMESSAGE = 1;  //热门消息
    public static final int ALLMESSAGE = 2;  //所有消息

    public static final String ISFIRSTREQUEST = "isFirstRequest";  //所有消息

    private int messageType;         //请求数据类型
    private boolean isFirstRequest; //是否在创建时就请求数据

    /**
     * @param messageType    快讯类型
     * @param isFirstRequest 是否在创建时就请求数据
     * @return
     */
    public static FastMessageListFragment getInstance(int messageType, boolean isFirstRequest) {
        FastMessageListFragment fragment = new FastMessageListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(CdRouteHelper.DATA_SIGN, messageType);
        bundle.putBoolean(ISFIRSTREQUEST, isFirstRequest);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void lazyLoad() {
        if (mRefreshBinding == null || isFirstRequest) return;
        isFirstRequest = true;                                          //第一次进行懒加载时进行数据请求
        mRefreshHelper.onDefaluteMRefresh(true);
    }

    @Override
    protected void onInvisible() {

    }

    @Override
    protected void afterCreate(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (getArguments() != null) {
            messageType = getArguments().getInt(CdRouteHelper.DATA_SIGN);
            isFirstRequest = getArguments().getBoolean(ISFIRSTREQUEST);
        }

        initRefreshHelper(MyCdConfig.LISTLIMIT);

        if (isFirstRequest) {
            mRefreshHelper.onDefaluteMRefresh(true);
        }
    }

    @Override
    public RecyclerView.Adapter getListAdapter(List listData) {

        FastMessageListAdapter fastMessageListAdapter = new FastMessageListAdapter(listData, messageType == HOTMESSAGE);

        fastMessageListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            FastMessageToShareActivity.open(mActivity, fastMessageListAdapter.getItem(position));
        });

        return fastMessageListAdapter;
    }

    @Override
    public void getListRequest(int pageindex, int limit, boolean isShowDialog) {

        Map<String, String> map = new HashMap<>();

        if (messageType == HOTMESSAGE) {       //热门快讯
            map.put("type", "1");
        }

        map.put("userId", SPUtilHelper.getUserId());
        map.put("start", pageindex + "");
        map.put("limit", limit + "");

        if (isShowDialog) showLoadingDialog();

        Call call = RetrofitUtils.createApi(MyApiServer.class).getFastMsgList("628097", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<FastMessage>>(mActivity) {
            @Override
            protected void onSuccess(ResponseInListModel<FastMessage> data, String SucMessage) {
                mRefreshHelper.setData(data.getList(), getString(R.string.no_fast_msg), R.drawable.no_dynamic);
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
}
