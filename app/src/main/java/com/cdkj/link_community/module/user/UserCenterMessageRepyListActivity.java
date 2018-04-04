package com.cdkj.link_community.module.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.base.AbsRefreshListActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.adapters.UserCenterMessageRePlyListAdapter;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.databinding.LayoutUserCenterBinding;
import com.cdkj.link_community.model.UserMessageComment;
import com.cdkj.link_community.module.message.MessageDetailsActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 用户中心 币圈评论 回复
 * Created by cdkj on 2018/3/27.
 */

public class UserCenterMessageRepyListActivity extends AbsRefreshListActivity {

    private String mUserId;
    private String mUserName;
    private String mUserLogo;
    private LayoutUserCenterBinding mCenterBinding;

    /**
     * @param context
     * @param userId  要查询的用户ID
     */
    public static void open(Context context, String userId, String userName, String userLogo) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, UserCenterMessageRepyListActivity.class);
        intent.putExtra(CdRouteHelper.APPLOGIN, userId);
        intent.putExtra("userName", userName);
        intent.putExtra("userLogo", userLogo);
        context.startActivity(intent);
    }


    @Override
    public RecyclerView.Adapter getListAdapter(List listData) {
        UserCenterMessageRePlyListAdapter userCenterMessageRePlyListAdapter = new UserCenterMessageRePlyListAdapter(listData,this);

        userCenterMessageRePlyListAdapter.addHeaderView(mCenterBinding.getRoot());
        userCenterMessageRePlyListAdapter.setHeaderAndEmpty(true);
        mCenterBinding.tvUserName.setText(mUserName);
        ImgUtils.loadQiniuLogo(this, mUserLogo, mCenterBinding.imgUserLogo);

        userCenterMessageRePlyListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (userCenterMessageRePlyListAdapter.getItem(position) == null || userCenterMessageRePlyListAdapter.getItem(position).getNews() == null) {
                return;
            }
            MessageDetailsActivity.open(UserCenterMessageRepyListActivity.this, userCenterMessageRePlyListAdapter.getItem(position).getNews().getCode());
        });

        userCenterMessageRePlyListAdapter.setOnItemClickListener((adapter, view, position) -> {
            UserMessageComment userMyComment = userCenterMessageRePlyListAdapter.getItem(position);
            if (userMyComment == null || TextUtils.isEmpty(userMyComment.getCode())) return;
            String noteCode = "";
            if (userMyComment.getNews() != null) {
                noteCode = userMyComment.getNews().getCode();
            }
            MyCommentDetailsActivity.open(UserCenterMessageRepyListActivity.this, userMyComment.getCode(), noteCode);
        });


        return userCenterMessageRePlyListAdapter;
    }

    @Override
    public void getListRequest(int pageindex, int limit, boolean isShowDialog) {

        if (TextUtils.isEmpty(mUserId)) {
            showSureDialog(getString(R.string.no_user_info), view -> {
                finish();
            });
            return;
        }

        Map<String, String> map = new HashMap<>();

        map.put("userId", mUserId);
        map.put("start", pageindex + "");
        map.put("limit", limit + "");

        Call call = RetrofitUtils.createApi(MyApiServer.class).getUserMyCommentList("628210", StringUtils.getJsonToString(map));
        addCall(call);
        if (isShowDialog) showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<UserMessageComment>>(this) {
            @Override
            protected void onSuccess(ResponseInListModel<UserMessageComment> data, String SucMessage) {
                mRefreshHelper.setData(data.getList(), getString(R.string.no_comment), 0);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });


    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        if (getIntent() != null) {
            mUserId = getIntent().getStringExtra(CdRouteHelper.APPLOGIN);
            mUserLogo = getIntent().getStringExtra("userLogo");
            mUserName = getIntent().getStringExtra("userName");
        }

        mBaseBinding.titleView.setBackgroundColor(ContextCompat.getColor(this,R.color.user_center_bg));
        mBaseBinding.titleView.setMidTitle(getString(R.string.user_center));
        mBaseBinding.viewV.setVisibility(View.GONE);


        mCenterBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.layout_user_center, null, false);

        initRefreshHelper(MyCdConfig.LISTLIMIT);

        mRefreshHelper.onDefaluteMRefresh(true);

    }
}
