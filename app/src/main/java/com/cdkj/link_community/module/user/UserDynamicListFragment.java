package com.cdkj.link_community.module.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
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
import com.cdkj.link_community.adapters.UserCenterBBSRePlyListAdapter;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.model.UserBBSComment;
import com.cdkj.link_community.module.coin_bbs.BBSCommentDetailsActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by cdkj on 2018/4/30.
 */

public class UserDynamicListFragment extends AbsRefreshListFragment {

    public static UserDynamicListFragment getInstance() {
        UserDynamicListFragment fragment = new UserDynamicListFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void lazyLoad() {
        if (mRefreshBinding != null) {
            mRefreshHelper.onDefaluteMRefresh(true);
        }
    }

    @Override
    protected void onInvisible() {

    }

    @Override
    protected void afterCreate(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initRefreshHelper(MyCdConfig.LISTLIMIT);

        mRefreshHelper.onDefaluteMRefresh(true);
    }

    @Override
    public RecyclerView.Adapter getListAdapter(List listData) {
        UserCenterBBSRePlyListAdapter userCenterMessageRePlyListAdapter = new UserCenterBBSRePlyListAdapter(listData);

        userCenterMessageRePlyListAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (userCenterMessageRePlyListAdapter.getItem(position) == null || userCenterMessageRePlyListAdapter.getItem(position).getPost() == null) {
                return;
            }
            BBSCommentDetailsActivity.open(mActivity, userCenterMessageRePlyListAdapter.getItem(position).getPost().getCode());
        });


        return userCenterMessageRePlyListAdapter;
    }

    @Override
    public void getListRequest(int pageIndex, int limit, boolean isShowDialog) {

        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());
        map.put("start", pageIndex + "");
        map.put("limit", limit + "");

        Call call = RetrofitUtils.createApi(MyApiServer.class).getUserBBSCommentList("628211", StringUtils.getJsonToString(map));

        addCall(call);

        if (isShowDialog) showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<UserBBSComment>>(mActivity) {
            @Override
            protected void onSuccess(ResponseInListModel<UserBBSComment> data, String SucMessage) {
                mRefreshHelper.setData(data.getList(), getString(R.string.no_dynamic), R.drawable.no_dynamic);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }
}
