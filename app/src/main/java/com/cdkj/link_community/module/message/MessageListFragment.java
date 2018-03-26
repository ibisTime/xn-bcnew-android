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
import com.cdkj.baselibrary.base.AbsRefreshListFragment;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.adapters.MessageListAdapter;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.model.FastMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 资讯列表
 * Created by cdkj on 2018/3/19.
 */

public class MessageListFragment extends AbsRefreshListFragment {


    private String mMessageType;//资讯类型
    private boolean mIsFristRequest;//是否创建时就进行请求
    private String mTitle;//标题

    private final static String ISFRISTREQUEST = "isFristRequest";//是否创建时就进行请求
    public final static String TITLE = "title";//标题

    /**
     * @param type 资讯类型
     * @return
     */
    public static MessageListFragment getInstanse(String type, boolean isFristRequest, String title) {
        MessageListFragment fragment = new MessageListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CdRouteHelper.DATASIGN, type);
        bundle.putBoolean(ISFRISTREQUEST, isFristRequest);
        bundle.putString(TITLE, title);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void lazyLoad() {

        if (mRefreshBinding == null || mIsFristRequest) {
            return;
        }

        mRefreshHelper.onDefaluteMLoadMore(false);

    }

    @Override
    protected void onInvisible() {

    }

    @Override
    protected void afterCreate(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (getArguments() != null) {
            mMessageType = getArguments().getString(CdRouteHelper.DATASIGN);
            mIsFristRequest = getArguments().getBoolean(ISFRISTREQUEST);
            mTitle = getArguments().getString(TITLE);
        }

        initRefreshHelper(MyCdConfig.LISTLIMIT);

        if (mIsFristRequest) {
            mRefreshHelper.onDefaluteMRefresh(false);
        }
    }


    @Override
    public RecyclerView.Adapter getListAdapter(List listData) {

        MessageListAdapter msgAdapter = new MessageListAdapter(listData);

        msgAdapter.setOnItemClickListener((adapter, view, position) -> {
            MessageDetailsActivity.open(mActivity, msgAdapter.getItem(position).getCode(), mTitle);
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


        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<FastMessage>>(mActivity) {
            @Override
            protected void onSuccess(ResponseInListModel<FastMessage> data, String SucMessage) {
                mRefreshHelper.setData(data.getList(), getString(R.string.no_msg), 0);
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
