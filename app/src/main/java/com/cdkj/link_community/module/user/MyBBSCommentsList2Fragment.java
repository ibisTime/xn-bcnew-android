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
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsRefreshListFragment;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.adapters.UserBBSCommentListAdapter2;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.model.UserBBSComment;
import com.cdkj.link_community.module.coin_bbs.BBSCommentDetailsActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 我的评论列表 (我评论的)
 * Created by cdkj on 2018/3/22.
 */

public class MyBBSCommentsList2Fragment extends AbsRefreshListFragment {

    public boolean isFirstRequest;


    public static MyBBSCommentsList2Fragment getInstance() {
        MyBBSCommentsList2Fragment fragment = new MyBBSCommentsList2Fragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void lazyLoad() {
        if (mRefreshBinding == null || isFirstRequest) {
            return;
        }
        isFirstRequest = true;
        mRefreshHelper.onDefaluteMRefresh(true);
    }

    @Override
    protected void onInvisible() {
    }

    @Override
    protected void afterCreate(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initRefreshHelper(MyCdConfig.LISTLIMIT);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            mRefreshHelper.onDefaluteMRefresh(true);
        }
    }


    @Override
    public RecyclerView.Adapter getListAdapter(List listData) {
        UserBBSCommentListAdapter2 userMyCommentListAdapter = new UserBBSCommentListAdapter2(listData);

        userMyCommentListAdapter.setOnItemClickListener((adapter, view, position) -> {
            UserBBSComment userMyComment = userMyCommentListAdapter.getItem(position);
            if (userMyComment == null || userMyComment.getPost() == null || TextUtils.isEmpty(userMyComment.getPost().getCode()))
                return;
            BBSCommentDetailsActivity.open(mActivity, userMyComment.getPost().getCode());
        });

        return userMyCommentListAdapter;
    }

    @Override
    public void getListRequest(int pageindex, int limit, boolean isShowDialog) {


        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelper.getUserId());
        map.put("start", pageindex + "");
        map.put("limit", limit + "");

        if (isShowDialog) showLoadingDialog();

        Call call = RetrofitUtils.createApi(MyApiServer.class).getUserBBSCommentList("628665", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<UserBBSComment>>(mActivity) {
            @Override
            protected void onSuccess(ResponseInListModel<UserBBSComment> data, String SucMessage) {
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
