package com.cdkj.link_community.module.coin_bbs;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.databinding.EmptyViewBinding;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.adapters.ReplyCommentListAdapter;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.databinding.ActivityMessageCommentDetailsBinding;
import com.cdkj.link_community.dialog.CommentInputDialog;
import com.cdkj.link_community.model.CoinBBSHotCircular;
import com.cdkj.link_community.model.ReplyComment;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.baselibrary.utils.DateUtil.DEFAULT_DATE_FMT;
import static com.cdkj.link_community.module.message.MessageDetailsActivity.COMMENTCOMMENT;
import static com.cdkj.link_community.module.message.MessageDetailsActivity.MSGCOMMENT;

/**
 * 币吧回复详情
 * Created by cdkj on 2018/3/24.
 */

public class BBSCommentDetailsActivity extends AbsBaseLoadActivity {

    private ActivityMessageCommentDetailsBinding mBinding;

    private String mCommentCode;

    /**
     * @param context
     * @param commentCode 评论编号
     */
    public static void open(Context context, String commentCode) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, BBSCommentDetailsActivity.class);

        intent.putExtra(CdRouteHelper.APPLOGIN, commentCode);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_message_comment_details, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        mBaseBinding.titleView.setMidTitle("回复详情");

        if (getIntent() != null) {
            mCommentCode = getIntent().getStringExtra(CdRouteHelper.APPLOGIN);
        }

        mBinding.getRoot().setVisibility(View.GONE);

        //帖子评论
        mBinding.bottomLayout.linComment.setOnClickListener(view -> {
            commentPlayRequest(mCommentCode, "");
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getCommentDetailRequest();
    }

    /**
     * 获取评论详情
     */
    private void getCommentDetailRequest() {

        if (TextUtils.isEmpty(mCommentCode)) {
            mBinding.getRoot().setVisibility(View.VISIBLE);
            return;
        }

        Map<String, String> map = new HashMap<>();

        map.put("code", mCommentCode);
        map.put("userId", SPUtilHelpr.getUserId());

        Call call = RetrofitUtils.createApi(MyApiServer.class).getBBSCommentDetails("628663", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<CoinBBSHotCircular>(this) {

            @Override
            protected void onSuccess(CoinBBSHotCircular data, String SucMessage) {
                setShowData(data);
                mBinding.getRoot().setVisibility(View.VISIBLE);
            }


            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    private void setShowData(CoinBBSHotCircular messageDetails) {

        if (messageDetails == null) return;


        ImgUtils.loadQiniuLogo(this, messageDetails.getPhoto(), mBinding.replayCommentLayout.imgLogo);

        mBinding.replayCommentLayout.tvName.setText(messageDetails.getNickname());
        mBinding.replayCommentLayout.tvTime.setText(DateUtil.formatStringData(messageDetails.getPublishDatetime(), DEFAULT_DATE_FMT));
        mBinding.replayCommentLayout.tvContent.setText(messageDetails.getContent());

        if (TextUtils.equals(messageDetails.getIsPoint(), "1")) {
            mBinding.replayCommentLayout.imgIsLike.setImageResource(R.drawable.gave_a_like_2);
        } else {
            mBinding.replayCommentLayout.imgIsLike.setImageResource(R.drawable.gave_a_like_2_un);
        }


        if (messageDetails.getPointCount() > 999) {
            mBinding.replayCommentLayout.tvLikeNum.setText("999+");
        } else {
            mBinding.replayCommentLayout.tvLikeNum.setText(messageDetails.getPointCount() + "");
        }

        setReplyData(messageDetails.getCommentList());

    }

    /**
     * 设置回复数据
     *
     * @param
     * @param
     */
    private void setReplyData(List<ReplyComment> comments) {

        mBinding.replayCommentLayout.recyclerComment.setNestedScrollingEnabled(false);

        mBinding.replayCommentLayout.recyclerComment.setLayoutManager(getLinearLayoutManager());

        ReplyCommentListAdapter replyCommentListAdapter = new ReplyCommentListAdapter(comments);

        /*对评价进行回复*/
        replyCommentListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                commentPlayRequest(replyCommentListAdapter.getItem(position).getCode(), replyCommentListAdapter.getItem(position).getNickname());
            }
        });

        EmptyViewBinding emptyViewBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.empty_view, null, false);

        emptyViewBinding.tv.setVisibility(View.VISIBLE);
        emptyViewBinding.img.setVisibility(View.VISIBLE);

        emptyViewBinding.tv.setText(R.string.come_here_sit);
        emptyViewBinding.img.setImageResource(R.drawable.sofa);

        replyCommentListAdapter.setEmptyView(emptyViewBinding.getRoot());

        mBinding.replayCommentLayout.recyclerComment.setAdapter(replyCommentListAdapter);

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
     * 对帖子进行回复
     */
    private void commentPlayRequest(String code, String name) {
        if (!SPUtilHelpr.isLogin(BBSCommentDetailsActivity.this, false)) {
            return;
        }

        CommentInputDialog commentInputDialog = new CommentInputDialog(this, name);
        commentInputDialog.setmSureListener(comment -> {
            if (TextUtils.isEmpty(comment)) {
                UITipDialog.showFall(BBSCommentDetailsActivity.this, getString(R.string.please_input_replycomment_info));
                return;
            }

            if (TextUtils.isEmpty(name)) {
                toCommentRequest(code, comment, "1"); /*类型(Y 1 帖子 2 评论)*/
            } else {
                toCommentRequest(code, comment, "2");
            }

        });
        commentInputDialog.show();
    }


    /**
     * 评论
     */
    private void toCommentRequest(String code, String content, String type) {

        if (TextUtils.isEmpty(code)) {
            return;
        }

        Map<String, String> map = new HashMap<>(); /*类型(Y 1 帖子 2 评论)*/

        map.put("type", type);
        map.put("objectCode", code);
        map.put("content", content);
        map.put("userId", SPUtilHelpr.getUserId());

        showLoadingDialog();
        Call call = RetrofitUtils.getBaseAPiService().successRequest("628652", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {

                if (data.isSuccess()) {
                    if (TextUtils.equals(type, MSGCOMMENT)) {
                        UITipDialog.showSuccess(BBSCommentDetailsActivity.this, getString(R.string.comment_succ));
                    }
                    getCommentDetailRequest();
                } else {
                    if (TextUtils.equals(type, MSGCOMMENT)) {
                        UITipDialog.showSuccess(BBSCommentDetailsActivity.this, getString(R.string.comment_fall));
                    }

                }
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

}
