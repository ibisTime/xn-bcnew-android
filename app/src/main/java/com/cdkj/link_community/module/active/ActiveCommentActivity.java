package com.cdkj.link_community.module.active;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.interfaces.CheckInfoReleaseStateListener;
import com.cdkj.baselibrary.interfaces.RefreshHelper;
import com.cdkj.baselibrary.model.CodeModel;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.CheckUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.adapters.MsgHotCommentListAdapter;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.databinding.ActivityMyActiveBinding;
import com.cdkj.link_community.dialog.CommentInputDialog;
import com.cdkj.link_community.model.MsgDetailsComment;
import com.cdkj.link_community.module.message.MessageCommentDetailsActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.baselibrary.appmanager.CdRouteHelper.DATA_SIGN;

/**
 * Created by cdkj on 2018/4/30.
 */

public class ActiveCommentActivity extends AbsBaseLoadActivity {

    private ActivityMyActiveBinding mBinding;

    private String code;

    private RefreshHelper mActiveRefreshHelper; //最新评论刷新

    /**
     * @param context
     * @param code
     */
    public static void open(Context context, String code) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, ActiveCommentActivity.class);
        intent.putExtra(DATA_SIGN, code);
        context.startActivity(intent);
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_my_active, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle("活动留言");
        if (getIntent() == null) {
            return;
        }

        code = getIntent().getStringExtra(DATA_SIGN);
        initListener();

        initRefreshHelper();

    }

    private void initListener() {
        //资讯评论
        mBinding.tvToComment.setOnClickListener(view -> {

            if (!SPUtilHelper.isLogin(this, false)) {
                return;
            }

            CommentInputDialog commentInputDialog = new CommentInputDialog(this, "");
            commentInputDialog.setmSureListener(comment -> {
                if (TextUtils.isEmpty(comment)) {
                    UITipDialog.showFail(ActiveCommentActivity.this, getString(R.string.please_input_comment_info));
                    return;
                }
                toCommentRequest(code, comment, "1");
            });
            commentInputDialog.show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        mActiveRefreshHelper.onDefaluteMRefresh(true);
    }

    private void initRefreshHelper() {
        mActiveRefreshHelper = new RefreshHelper(this, new BaseRefreshCallBack(this) {
            @Override
            public View getRefreshLayout() {
                return mBinding.refreshLayout;
            }

            @Override
            public RecyclerView getRecyclerView() {

                /*防止局部刷新闪烁*/
                ((DefaultItemAnimator) mBinding.rvActive.getItemAnimator()).setSupportsChangeAnimations(false);

                return mBinding.rvActive;
            }

            @Override
            public RecyclerView.Adapter getAdapter(List listData) {

                MsgHotCommentListAdapter mAdapter = getListAdapter(listData);

                return mAdapter;
            }

            @Override
            public void getListDataRequest(int pageindex, int limit, boolean isShowDialog) {
                getListRequest(pageindex, limit, isShowDialog);
            }
        });

        mActiveRefreshHelper.init(MyCdConfig.LISTLIMIT);

    }

    public MsgHotCommentListAdapter getListAdapter(List listData) {
        MsgHotCommentListAdapter msgHotCommentListAdapter = new MsgHotCommentListAdapter(listData);

        msgHotCommentListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MessageCommentDetailsActivity.open(ActiveCommentActivity.this, msgHotCommentListAdapter.getItem(position).getCode(),true);
            }
        });

        /*点赞*/
        msgHotCommentListAdapter.setOnItemChildClickListener((adapter, view, position) -> {

            if (!SPUtilHelper.isLogin(ActiveCommentActivity.this, false)) {
                return;
            }
            toCommentLikeRequest(msgHotCommentListAdapter, position);
        });

        return msgHotCommentListAdapter;
    }

    public void getListRequest(int pageIndex, int limit, boolean isShowDialog) {
        Map<String, String> map = new HashMap<>();

        map.put("objectCode", code);
        map.put("start", pageIndex + "");
        map.put("limit", limit + "");
        map.put("userId", SPUtilHelper.getUserId());

        if (isShowDialog) showLoadingDialog();

        Call<BaseResponseModel<ResponseInListModel<MsgDetailsComment>>> call = RetrofitUtils.createApi(MyApiServer.class).getMsgDetailsNewCommentList("628285", StringUtils.getJsonToString(map));

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<MsgDetailsComment>>(this) {
            @Override
            protected void onSuccess(ResponseInListModel<MsgDetailsComment> data, String SucMessage) {
                mActiveRefreshHelper.setData(data.getList(), "暂无评论", 0);
            }

            @Override
            protected void onFinish() {
                if (isShowDialog) {
                    disMissLoading();
                }
            }
        });
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

        map.put("type", "2");
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
     * 评论
     */
    private void toCommentRequest(String code, String content, String type) {

        if (TextUtils.isEmpty(code)) {
            return;
        }

        Map<String, String> map = RetrofitUtils.getRequestMap(); /*类型(Y 1 资讯 2 评论)*/

        map.put("type", type);
        map.put("objectCode", code);
        map.put("content", content);
        map.put("userId", SPUtilHelper.getUserId());

        showLoadingDialog();
        Call call = RetrofitUtils.getBaseAPiService().codeRequest("628511", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<CodeModel>(this) {
            @Override
            protected void onSuccess(CodeModel data, String SucMessage) {
                checkRealseState(data);
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
     * @param
     */
    private void checkRealseState(CodeModel data) {

        CheckUtils.checkReleaseState(data.getCode(), new CheckInfoReleaseStateListener() {
            @Override
            public void succ() {
                UITipDialog.showSuccess(ActiveCommentActivity.this, getString(R.string.comment_succ));
                mActiveRefreshHelper.onDefaluteMRefresh(true);
            }

            @Override
            public void fail() {
                UITipDialog.showSuccess(ActiveCommentActivity.this, getString(R.string.comment_fall));
            }

            @Override
            public void haveSensitive(String str) {
                UITipDialog.showInfo(ActiveCommentActivity.this, str);
            }
        });
    }

}
