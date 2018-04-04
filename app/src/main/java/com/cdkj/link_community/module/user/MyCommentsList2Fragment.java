package com.cdkj.link_community.module.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.AbsRefreshListFragment;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.adapters.UserMyCommentReplayListAdapter;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.model.UserMessageComment;
import com.cdkj.link_community.module.message.MessageDetailsActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 我的评论列表 (回复我的)
 * Created by cdkj on 2018/3/22.
 */

public class MyCommentsList2Fragment extends AbsRefreshListFragment {


    public static MyCommentsList2Fragment getInstanse() {
        MyCommentsList2Fragment fragment = new MyCommentsList2Fragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void lazyLoad() {
        if (mRefreshBinding == null || mRefreshHelper == null) return;

        mRefreshHelper.onDefaluteMRefresh(true);

    }

    @Override
    protected void onInvisible() {
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            mRefreshHelper.onDefaluteMRefresh(true);
        }
    }

    @Override
    protected void afterCreate(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initRefreshHelper(MyCdConfig.LISTLIMIT);
    }


    @Override
    public RecyclerView.Adapter getListAdapter(List listData) {
        UserMyCommentReplayListAdapter userMyCommentListAdapter = new UserMyCommentReplayListAdapter(listData,mActivity);
        userMyCommentListAdapter.setOnItemClickListener((adapter, view, position) -> {
            UserMessageComment userMyComment = userMyCommentListAdapter.getItem(position);
            if (userMyComment == null || TextUtils.isEmpty(userMyComment.getCode())) return;
            String noteCode = "";
            if (userMyComment.getNews() != null) {
                noteCode = userMyComment.getNews().getCode();
            }
            MyCommentDetailsActivity.open(mActivity, userMyComment.getCode(), noteCode);
        });

        userMyCommentListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            UserMessageComment userMyComment = userMyCommentListAdapter.getItem(position);
            if (userMyComment == null || userMyComment.getNews() == null || TextUtils.isEmpty(userMyComment.getNews().getCode()))
                return;
            MessageDetailsActivity.open(mActivity, userMyComment.getNews().getCode());
        });

        return userMyCommentListAdapter;
    }

    @Override
    public void getListRequest(int pageindex, int limit, boolean isShowDialog) {


        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelpr.getUserId());
        map.put("start", pageindex + "");
        map.put("limit", limit + "");

        if (isShowDialog) showLoadingDialog();

        Call call = RetrofitUtils.createApi(MyApiServer.class).getUserMyCommentList("628208", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<UserMessageComment>>(mActivity) {
            @Override
            protected void onSuccess(ResponseInListModel<UserMessageComment> data, String SucMessage) {
                mRefreshHelper.setData(data.getList(), getString(R.string.no_comment), 0);
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
