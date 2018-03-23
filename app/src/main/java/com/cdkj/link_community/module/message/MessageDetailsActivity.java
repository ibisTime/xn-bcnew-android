package com.cdkj.link_community.module.message;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.interfaces.RefreshHelper;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.adapters.MessageDetailRecomListAdapter;
import com.cdkj.link_community.adapters.MsgHotCommentListAdapter;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.databinding.ActivityMessageDetailBinding;
import com.cdkj.link_community.dialog.CommentInputDialog;
import com.cdkj.link_community.model.MessageDetails;
import com.cdkj.link_community.model.MessageDetailsNoteList;
import com.cdkj.link_community.model.MsgDetailsComment;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.baselibrary.utils.DateUtil.DEFAULT_DATE_FMT;
import static com.cdkj.link_community.module.message.MessageListFragment.TITLE;

/**
 * 资讯详情
 * Created by cdkj on 2018/3/23.
 */

public class MessageDetailsActivity extends AbsBaseLoadActivity {

    private ActivityMessageDetailBinding mBinding;

    private String mCode;

    private String mTitle;

    private RefreshHelper mNewCommentRefreshHelper; //最新评论刷新


    private String MSGCOMMENT = "1";/*类型(Y 1 资讯 2 评论)*/
    private String COMMENTCOMMENT = "2";/*类型(Y 1 资讯 2 评论)*/

    /**
     * @param context
     * @param msgCode 消息编号
     */
    public static void open(Context context, String msgCode, String title) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, MessageDetailsActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, msgCode);
        intent.putExtra(TITLE, title);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_message_detail, null, false);

        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        if (getIntent() != null) {
            mCode = getIntent().getStringExtra(CdRouteHelper.DATASIGN);
            mTitle = getIntent().getStringExtra(TITLE);
            mBaseBinding.titleView.setMidTitle(mTitle);
        }

        mBinding.refreshLayout.setEnableRefresh(false);

        mBinding.getRoot().setVisibility(View.GONE);


        mBinding.contentLayout.recyclerViewNewComment.setNestedScrollingEnabled(false);
        mBinding.contentLayout.recyclerViewHotComment.setNestedScrollingEnabled(false);
        mBinding.contentLayout.recyclerViewRecommended.setNestedScrollingEnabled(false);

        initRefreshHelper();

        initListener();
    }

    private void initListener() {

        //资讯评论
        mBinding.tvToComment.setOnClickListener(view -> {

            if (!SPUtilHelpr.isLogin(this, false)) {
                return;
            }

            CommentInputDialog commentInputDialog = new CommentInputDialog(this);
            commentInputDialog.setmSureListener(comment -> {
                if (TextUtils.isEmpty(comment)) {
                    UITipDialog.showFall(MessageDetailsActivity.this, getString(R.string.please_input_comment_info));
                    return;
                }
                toCommentRequest(mCode, comment, MSGCOMMENT);
            });
            commentInputDialog.show();
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getMessageDetailRequest();
    }

    private void initRefreshHelper() {
        mNewCommentRefreshHelper = new RefreshHelper(this, new BaseRefreshCallBack(this) {
            @Override
            public View getRefreshLayout() {
                return mBinding.refreshLayout;
            }

            @Override
            public RecyclerView getRecyclerView() {
                return mBinding.contentLayout.recyclerViewNewComment;
            }

            @Override
            public RecyclerView.Adapter getAdapter(List listData) {
                return new MsgHotCommentListAdapter(listData);
            }

            @Override
            public void getListDataRequest(int pageindex, int limit, boolean isShowDialog) {
                getMsgDetailsNewCommentListRequest(pageindex, limit, isShowDialog);
            }
        });

        mNewCommentRefreshHelper.init(MyCdConfig.LISTLIMIT);

        mBinding.contentLayout.recyclerViewNewComment.setLayoutManager(getLinearLayoutManager());
    }

    /**
     * 获取资讯详情
     */
    private void getMessageDetailRequest() {

        if (TextUtils.isEmpty(mCode)) {
            return;
        }

        Map<String, String> map = new HashMap<>();

        map.put("code", mCode);

        Call call = RetrofitUtils.createApi(MyApiServer.class).getMessageDetails("628206", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<MessageDetails>(this) {

            @Override
            protected void onSuccess(MessageDetails data, String SucMessage) {

                setShowData(data);

                mBinding.getRoot().setVisibility(View.VISIBLE);
            }


            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    /**
     * @param data
     */
    private void setShowData(MessageDetails data) {

        if (data == null) return;

        mBinding.contentLayout.webView.loadData(data.getContent(), "text/html; charset=UTF-8", null);
        mBinding.contentLayout.tvAuthor.setText(getString(R.string.author) + data.getAuther());
        mBinding.contentLayout.tvTime.setText(DateUtil.formatStringData(data.getShowDatetime(), DEFAULT_DATE_FMT));
        mBinding.contentLayout.tvFrom.setText(getString(R.string.message_frome) + data.getSource());
        mBinding.contentLayout.tvTitle.setText(data.getTitle());

        if (data.getCommentCount() > 0) {
            if (data.getCommentCount() > 999) {
                mBinding.tvCommentNum.setText("999+");
            } else {
                mBinding.tvCommentNum.setText(data.getCommentCount() + "");
            }
        } else {
            mBinding.tvCommentNum.setText("");
        }

        setRecommendedList(data.getRefNewList());
        setHotCommentList(data.getHotCommentList());

        mNewCommentRefreshHelper.onDefaluteMRefresh(true);
    }

    /**
     * 设置推荐列表
     *
     * @param refNewList
     */
    private void setRecommendedList(List<MessageDetailsNoteList> refNewList) {

        if (refNewList == null || refNewList.isEmpty()) {
            mBinding.contentLayout.recyclerViewRecommended.setVisibility(View.GONE);
            mBinding.contentLayout.tvMsgListTitle.setVisibility(View.GONE);
            return;
        }

        MessageDetailRecomListAdapter messageDetailRecomListAdapter = new MessageDetailRecomListAdapter(refNewList);

        messageDetailRecomListAdapter.setOnItemClickListener((adapter, view, position) -> {
            MessageDetailsActivity.open(this, messageDetailRecomListAdapter.getItem(position).getCode(), mTitle);
            finish();
        });

        mBinding.contentLayout.recyclerViewRecommended.setLayoutManager(getLinearLayoutManager());
        mBinding.contentLayout.recyclerViewRecommended.setAdapter(messageDetailRecomListAdapter);

    }

    /**
     * 设置热门评价
     *
     * @param
     */
    private void setHotCommentList(List<MsgDetailsComment> hotCommentList) {
        if (hotCommentList == null || hotCommentList.isEmpty()) {
            mBinding.contentLayout.recyclerViewHotComment.setVisibility(View.GONE);
            mBinding.contentLayout.tvHotCommentTitle.setVisibility(View.GONE);
            return;
        }
        MsgHotCommentListAdapter msgHotCommentListAdapter = new MsgHotCommentListAdapter(hotCommentList);

        msgHotCommentListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                toCommentRequest(msgHotCommentListAdapter.getItem(position).getCode(), "xxx123", COMMENTCOMMENT);
            }
        });

        mBinding.contentLayout.recyclerViewHotComment.setLayoutManager(getLinearLayoutManager());
        mBinding.contentLayout.recyclerViewHotComment.setAdapter(msgHotCommentListAdapter);

    }


    @NonNull
    private LinearLayoutManager getLinearLayoutManager() {
        return new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {  //禁止自滚动
                return false;
            }
        };
    }

    /**
     * 获取最新评论
     */
    private void getMsgDetailsNewCommentListRequest(int pageindex, int limit, boolean isShowDialog) {

        if (mCode == null) return;

        Map<String, String> map = new HashMap<>();

        map.put("objectCode", mCode);
        map.put("start", pageindex + "");
        map.put("limit", limit + "");

        if (isShowDialog) showLoadingDialog();

        Call<BaseResponseModel<ResponseInListModel<MsgDetailsComment>>> call = RetrofitUtils.createApi(MyApiServer.class).getMsgDetailsNewCommentList("628285", StringUtils.getJsonToString(map));

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<MsgDetailsComment>>(this) {
            @Override
            protected void onSuccess(ResponseInListModel<MsgDetailsComment> data, String SucMessage) {
                if (data.getList() == null || data.getList().isEmpty()) {
                    if (pageindex == 1) {
                        mBinding.contentLayout.tvNewCommentTitle.setVisibility(View.GONE);
                        mBinding.contentLayout.recyclerViewNewComment.setVisibility(View.GONE);
                    }
                } else {
                    mBinding.contentLayout.tvNewCommentTitle.setVisibility(View.VISIBLE);
                    mBinding.contentLayout.recyclerViewNewComment.setVisibility(View.VISIBLE);
                }
                mNewCommentRefreshHelper.setData(data.getList(), "", 0);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });


    }


    /**
     * 评论
     */
    private void toCommentRequest(String code, String content, String type) {

        if (TextUtils.isEmpty(code)) {
            return;
        }

        Map<String, String> map = new HashMap<>(); /*类型(Y 1 资讯 2 评论)*/

        map.put("type", type);
        map.put("objectCode", code);
        map.put("content", content);
        map.put("userId", SPUtilHelpr.getUserId());

        showLoadingDialog();
        Call call = RetrofitUtils.getBaseAPiService().successRequest("628200", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {

                if (data.isSuccess()) {
                    UITipDialog.showSuccess(MessageDetailsActivity.this, getString(R.string.comment_succ));
                    getMessageDetailRequest();
                } else {
                    UITipDialog.showSuccess(MessageDetailsActivity.this, getString(R.string.comment_fall));
                }
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    /**
     * 资讯点赞
     */
    private void toMsgLikeRequest() {

        if (TextUtils.isEmpty(mCode)) {
            return;
        }

        Map<String, String> map = new HashMap<>(); /*类型(Y 1 资讯 2 评论)*/

        map.put("type", MSGCOMMENT);
        map.put("objectCode", mCode);
        map.put("userId", SPUtilHelpr.getUserId());

        showLoadingDialog();
        Call call = RetrofitUtils.getBaseAPiService().successRequest("628201", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {

                if (data.isSuccess()) {
                    mBinding.contentLayout.imgLike.setImageResource(R.drawable.gave_a_like);
                } else {
                    mBinding.contentLayout.imgLike.setImageResource(R.drawable.gave_a_like_un);
                }
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }


}
