package com.cdkj.link_community.module.message;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsRefreshListActivity;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.adapters.MsgHotCommentListAdapter;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.model.MsgDetailsComment;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.link_community.module.message.MessageDetailsActivity.COMMENTCOMMENT;

/**
 * 资讯评论列表
 */
public class MessageCommentListActivity extends AbsRefreshListActivity {


    private String mCode;//资讯编号


    /**
     * @param context
     * @param msgCode 消息编号
     */
    public static void open(Context context, String msgCode) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, MessageCommentListActivity.class);
        intent.putExtra(CdRouteHelper.DATA_SIGN, msgCode);
        context.startActivity(intent);
    }

    @Override
    public RecyclerView.Adapter getListAdapter(List listData) {
        return getCommentListAdapter(listData);
    }

    @Override
    public void getListRequest(int pageIndex, int limit, boolean isShowDialog) {
        getMsgDetailsNewCommentListRequest(pageIndex, limit, isShowDialog);
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        if (getIntent() != null) {
            mCode = getIntent().getStringExtra(CdRouteHelper.DATA_SIGN);
        }

        mBaseBinding.titleView.setMidTitle("评论");

        initRefreshHelper(MyCdConfig.LISTLIMIT);

        mRefreshHelper.onDefaluteMRefresh(true);
    }

    /**
     * 获取评论适配器
     *
     * @param listData
     * @return
     */
    @NonNull
    private MsgHotCommentListAdapter getCommentListAdapter(List listData) {
        MsgHotCommentListAdapter msgHotCommentListAdapter = new MsgHotCommentListAdapter(listData);

        msgHotCommentListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MessageCommentDetailsActivity.open(MessageCommentListActivity.this, msgHotCommentListAdapter.getItem(position).getCode(),false);
//                commentPlayRequest(msgHotCommentListAdapter.getItem(position).getCode());
            }
        });

        /*点赞*/
        msgHotCommentListAdapter.setOnItemChildClickListener((adapter, view, position) -> {

            if (!SPUtilHelper.isLogin(MessageCommentListActivity.this, false)) {
                return;
            }

            toCommentLikeRequest(msgHotCommentListAdapter, position);
        });

        return msgHotCommentListAdapter;
    }

    /**
     * 评论点赞
     */
    private void toCommentLikeRequest(MsgHotCommentListAdapter adapter, int position) {

        MsgDetailsComment msgDetailsComment = adapter.getItem(position);

        if (TextUtils.isEmpty(msgDetailsComment.getCode())) {
            return;
        }

        Map<String, String> map = new HashMap<>(); /*类型(Y 1 资讯 2 评论)*/

        map.put("type", COMMENTCOMMENT);
        map.put("objectCode", msgDetailsComment.getCode());
        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        showLoadingDialog();
        Call call = RetrofitUtils.getBaseAPiService().successRequest("628201", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {

                if (data.isSuccess()) {
                    if (msgDetailsComment.getIsPoint() == 1) {
                        msgDetailsComment.setPointCount(msgDetailsComment.getPointCount() - 1);
                        msgDetailsComment.setIsPoint(0);
                    } else {
                        msgDetailsComment.setPointCount(msgDetailsComment.getPointCount() + 1);
                        msgDetailsComment.setIsPoint(1);
                    }
                    adapter.notifyItemChanged(position);
                }
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    /**
     * 获取最新评论
     */
    private void getMsgDetailsNewCommentListRequest(int pageindex, int limit, boolean isShowDialog) {

        if (mCode == null) {
            mRefreshHelper.setData(new ArrayList(), "暂无评论", 0);
            return;
        }

        Map<String, String> map = new HashMap<>();

        map.put("objectCode", mCode);
        map.put("start", pageindex + "");
        map.put("limit", limit + "");
        map.put("userId", SPUtilHelper.getUserId());

        if (isShowDialog) showLoadingDialog();

        Call<BaseResponseModel<ResponseInListModel<MsgDetailsComment>>> call = RetrofitUtils.createApi(MyApiServer.class).getMsgDetailsNewCommentList("628285", StringUtils.getJsonToString(map));

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<MsgDetailsComment>>(this) {
            @Override
            protected void onSuccess(ResponseInListModel<MsgDetailsComment> data, String SucMessage) {
                mRefreshHelper.setData(data.getList(), "暂无评论", 0);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });


    }

}
