package com.cdkj.link_community.module.message;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.databinding.EmptyViewBinding;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.interfaces.CheckInfoReleaseStateListener;
import com.cdkj.baselibrary.model.CodeModel;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.CheckUtils;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.adapters.ReplyCommentListAdapter;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.databinding.ActivityMessageCommentDetailsBinding;
import com.cdkj.link_community.dialog.CommentInputDialog;
import com.cdkj.link_community.model.MsgDetailsComment;
import com.cdkj.link_community.model.ReplyComment;
import com.cdkj.link_community.module.user.UserCenterMessageRepyListActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.baselibrary.utils.DateUtil.DEFAULT_DATE_FMT;
import static com.cdkj.link_community.module.message.MessageDetailsActivity.COMMENTCOMMENT;
import static com.cdkj.link_community.module.message.MessageDetailsActivity.MSGCOMMENT;

/**
 * 我的评论详情
 * Created by cdkj on 2018/3/24.
 */

public class MessageCommentDetailsActivity extends AbsBaseLoadActivity {

    private ActivityMessageCommentDetailsBinding mBinding;

    private String mCommentCode;
    private boolean isActiveComment;
    private MsgDetailsComment msgDetailsComment;

    /**
     * @param context
     * @param commentCode 评论编号
     */
    public static void open(Context context, String commentCode, boolean isActiveComment) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, MessageCommentDetailsActivity.class);

        intent.putExtra(CdRouteHelper.APPLOGIN, commentCode);
        intent.putExtra("isActiveComment", isActiveComment);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_message_comment_details, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        mBaseBinding.titleView.setMidTitle(getString(R.string.comment_detail));

        if (getIntent() != null) {
            mCommentCode = getIntent().getStringExtra(CdRouteHelper.APPLOGIN);
            isActiveComment = getIntent().getBooleanExtra("isActiveComment" ,false);
        }

        mBinding.getRoot().setVisibility(View.GONE);

        initListener();


    }

    private void initListener() {
        //评论
        mBinding.bottomLayout.linComment.setOnClickListener(view -> {
            commentPlayRequest(mCommentCode, "");
        });

        //点赞
        mBinding.replayCommentLayout.linLike.setOnClickListener(view -> {
            if (!SPUtilHelper.isLogin(this, false)) {
                return;
            }
            toMsgLikeRequest();
        });

        mBinding.replayCommentLayout.imgLogo.setOnClickListener(view -> {
            if (msgDetailsComment == null) return;

            UserCenterMessageRepyListActivity.open(this, msgDetailsComment.getUserId(), msgDetailsComment.getNickname(), msgDetailsComment.getPhoto());
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
        map.put("userId", SPUtilHelper.getUserId());

        Call call = RetrofitUtils.createApi(MyApiServer.class).getMessageCommentDetails("628286", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<MsgDetailsComment>(this) {

            @Override
            protected void onSuccess(MsgDetailsComment data, String SucMessage) {
                msgDetailsComment = data;
                setShowData(data);
                mBinding.getRoot().setVisibility(View.VISIBLE);
            }


            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    private void setShowData(MsgDetailsComment messageDetails) {

        if (messageDetails == null) return;


        ImgUtils.loadQiniuLogo(this, messageDetails.getPhoto(), mBinding.replayCommentLayout.imgLogo);

        mBinding.replayCommentLayout.tvName.setText(messageDetails.getNickname());
        mBinding.replayCommentLayout.tvTime.setText(DateUtil.formatStringData(messageDetails.getCommentDatetime(), DEFAULT_DATE_FMT));
        mBinding.replayCommentLayout.tvContent.setText(messageDetails.getContent());

        if (messageDetails.getIsPoint() == 1) {
            mBinding.replayCommentLayout.imgIsLike.setImageResource(R.drawable.gave_a_like_2);
        } else {
            mBinding.replayCommentLayout.imgIsLike.setImageResource(R.drawable.gave_a_like_2_un);
        }


        mBinding.replayCommentLayout.tvLikeNum.setText(StringUtils.formatNum(new BigDecimal(messageDetails.getPointCount())));

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

        replyCommentListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                if (!SPUtilHelper.isLogin(MessageCommentDetailsActivity.this, false)) {
                    return;
                }

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
     * 对评论进行回复
     */
    private void commentPlayRequest(String code, String name) {
        if (!SPUtilHelper.isLogin(MessageCommentDetailsActivity.this, false)) {
            return;
        }
        CommentInputDialog commentInputDialog = new CommentInputDialog(this, name);
        commentInputDialog.setmSureListener(comment -> {
            if (TextUtils.isEmpty(comment)) {
                UITipDialog.showFail(MessageCommentDetailsActivity.this, getString(R.string.please_input_replycomment_info));
                return;
            }
            toCommentRequest(code, comment, COMMENTCOMMENT);
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

        Map<String, String> map = new HashMap<>(); /*类型(Y 1 资讯 2 评论)*/

        map.put("type", type);
        map.put("objectCode", code);
        map.put("content", content);
        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        showLoadingDialog();

        Call call = RetrofitUtils.getBaseAPiService().codeRequest(isActiveComment ? "628511" : "628200", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<CodeModel>(this) {
            @Override
            protected void onSuccess(CodeModel data, String SucMessage) {
                checkRealseState(data, type);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    /**
     * 检测发布状态
     *
     * @param data
     * @param type
     */
    private void checkRealseState(CodeModel data, String type) {
        CheckUtils.checkReleaseState(data.getCode(), new CheckInfoReleaseStateListener() {
            @Override
            public void succ() {
                if (TextUtils.equals(type, MSGCOMMENT)) {
                    UITipDialog.showSuccess(MessageCommentDetailsActivity.this, getString(R.string.comment_succ));
                }
                getCommentDetailRequest();
            }

            @Override
            public void fail() {
                if (TextUtils.equals(type, MSGCOMMENT)) {
                    UITipDialog.showSuccess(MessageCommentDetailsActivity.this, getString(R.string.comment_fall));
                }
            }

            @Override
            public void haveSensitive(String str) {
                UITipDialog.showInfo(MessageCommentDetailsActivity.this, str);
            }
        });
    }

    /**
     * 资讯点赞
     */
    private void toMsgLikeRequest() {

        if (msgDetailsComment == null || TextUtils.isEmpty(msgDetailsComment.getCode())) {
            return;
        }

        Map<String, String> map = new HashMap<>(); /*类型(Y 1 资讯 2 评论)*/

        map.put("type", COMMENTCOMMENT);
        map.put("objectCode", msgDetailsComment.getCode());
        map.put("userId", SPUtilHelper.getUserId());

        showLoadingDialog();
        Call call = RetrofitUtils.getBaseAPiService().successRequest("628201", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {

                if (data.isSuccess()) {

                    if (msgDetailsComment != null) {
                        if (msgDetailsComment.getIsPoint() == 0) {
                            msgDetailsComment.setIsPoint(1);
                            msgDetailsComment.setPointCount(msgDetailsComment.getPointCount() + 1);
                        } else {
                            msgDetailsComment.setIsPoint(0);
                            msgDetailsComment.setPointCount(msgDetailsComment.getPointCount() - 1);
                        }
                    }
                    setLikeInfo(msgDetailsComment);

                }
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    /**
     * 设置点赞信息
     *
     * @param data
     */
    private void setLikeInfo(MsgDetailsComment data) {
        /*是否点赞*/

        if (data.getIsPoint() == 1) {
            mBinding.replayCommentLayout.imgIsLike.setImageResource(R.drawable.gave_a_like);
        } else {
            mBinding.replayCommentLayout.imgIsLike.setImageResource(R.drawable.gave_a_like_un);
        }


        /*点赞数量*/
        mBinding.replayCommentLayout.tvLikeNum.setText(StringUtils.formatNum(new BigDecimal(data.getPointCount())));
    }

}
