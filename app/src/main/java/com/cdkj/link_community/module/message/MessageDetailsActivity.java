package com.cdkj.link_community.module.message;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.interfaces.CheckInfoReleaseStateListener;
import com.cdkj.baselibrary.interfaces.RefreshHelper;
import com.cdkj.baselibrary.model.CodeModel;
import com.cdkj.baselibrary.model.IntroductionInfoModel;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.CheckUtils;
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
import com.cdkj.link_community.module.user.ShareActivity;
import com.cdkj.link_community.utils.WxUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.just.agentweb.AgentWeb;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.baselibrary.utils.DateUtil.DEFAULT_DATE_FMT;

/**
 * 资讯详情
 * Created by cdkj on 2018/3/23.
 */

public class MessageDetailsActivity extends AbsBaseLoadActivity {

    private ActivityMessageDetailBinding mBinding;

    private String mCode; //资讯编号

    private RefreshHelper mNewCommentRefreshHelper; //最新评论刷新


    public static final String MSGCOMMENT = "1";/*类型MSGCOMMENT(Y 1 资讯 2 评论)*/
    public static final String COMMENTCOMMENT = "2";/*类型(Y 1 资讯 2 评论)*/

    private MessageDetails messageDetails;

    private String mSharePhotoUrl;//要分分享的图片url
    private String mShareContent;//要分享的内容

    private AgentWeb mAgentWeb;

    /**
     * @param context
     * @param msgCode 消息编号
     */
    public static void open(Context context, String msgCode) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, MessageDetailsActivity.class);
        intent.putExtra(CdRouteHelper.DATA_SIGN, msgCode);
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
            mCode = getIntent().getStringExtra(CdRouteHelper.DATA_SIGN);
        }

        mBaseBinding.titleView.setRightTitle("投稿");
        mBaseBinding.titleView.setRightFraClickListener(view -> {
            if (!SPUtilHelper.isLogin(this, false)) {
                return;
            }

            //java调用JS方法
            MessageReleaseActivity2.open(this);

//            CdRouteHelper.openWebViewActivityForUrl("编辑资讯","http://47.96.161.183:2903/news/addNews.html?ownerId=U201804291940579421200");
        });

        mBinding.refreshLayout.setEnableRefresh(false);

        mBinding.getRoot().setVisibility(View.GONE);

        mBinding.contentLayout.recyclerViewNewComment.setNestedScrollingEnabled(false);
        mBinding.contentLayout.recyclerViewHotComment.setNestedScrollingEnabled(false);
        mBinding.contentLayout.recyclerViewRecommended.setNestedScrollingEnabled(false);

        /*防止局部刷新闪烁*/
        ((DefaultItemAnimator) mBinding.contentLayout.recyclerViewNewComment.getItemAnimator()).setSupportsChangeAnimations(false);
        ((DefaultItemAnimator) mBinding.contentLayout.recyclerViewHotComment.getItemAnimator()).setSupportsChangeAnimations(false);
        ((DefaultItemAnimator) mBinding.contentLayout.recyclerViewRecommended.getItemAnimator()).setSupportsChangeAnimations(false);

        mBinding.contentLayout.webView.setVerticalScrollBarEnabled(false); //垂直不显示滚动条

        mBinding.contentLayout.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mBinding.getRoot().setVisibility(View.VISIBLE);   //当web内容加载完成时显示页面
                disMissLoading();
            }
        });

        initRefreshHelper();
        initListener();
    }


    @Override
    protected void onResume() {
        getMessageDetailRequest();
        if (mAgentWeb != null)
            mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (mAgentWeb != null)
            mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }


    private void initListener() {

        mBinding.fraComment.setOnClickListener(view -> {
            MessageCommentListActivity.open(this, mCode);
        });


        //资讯评论
        mBinding.tvToComment.setOnClickListener(view -> {

            if (!SPUtilHelper.isLogin(this, false)) {
                return;
            }

            CommentInputDialog commentInputDialog = new CommentInputDialog(this, "");
            commentInputDialog.setmSureListener(comment -> {
                if (TextUtils.isEmpty(comment)) {
                    UITipDialog.showFail(MessageDetailsActivity.this, getString(R.string.please_input_comment_info));
                    return;
                }
                toCommentRequest(mCode, comment, MSGCOMMENT);
            });
            commentInputDialog.show();
        });

        //点赞
        mBinding.contentLayout.imgLike.setOnClickListener(view -> {
            if (!SPUtilHelper.isLogin(this, false)) {
                return;
            }
            toMsgLikeRequest();
        });

        //收藏
        mBinding.imgCollection.setOnClickListener(view -> {
            if (!SPUtilHelper.isLogin(this, false)) {
                return;
            }
            toMsgCollectionRequest();
        });

        //分享
        mBinding.contentLayout.linWx.setOnClickListener(view -> getUrlToShare(0));
        mBinding.contentLayout.linPyq.setOnClickListener(view -> getUrlToShare(1));
        mBinding.imgShare.setOnClickListener(view -> getUrlToShare(3));

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

                MsgHotCommentListAdapter msgHotCommentListAdapter = getCommentListAdapter(listData);

                return msgHotCommentListAdapter;
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
                MessageCommentDetailsActivity.open(MessageDetailsActivity.this, msgHotCommentListAdapter.getItem(position).getCode(), false);
//                commentPlayRequest(msgHotCommentListAdapter.getItem(position).getCode());
            }
        });

        /*点赞*/
        msgHotCommentListAdapter.setOnItemChildClickListener((adapter, view, position) -> {

            if (!SPUtilHelper.isLogin(MessageDetailsActivity.this, false)) {
                return;
            }

            toCommentLikeRequest(msgHotCommentListAdapter, position);
        });

        return msgHotCommentListAdapter;
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
        map.put("userId", SPUtilHelper.getUserId());

        Call call = RetrofitUtils.createApi(MyApiServer.class).getMessageDetails("628206", StringUtils.getJsonToString(map));

        addCall(call);

        if (messageDetails == null) {
//            showLoadingDialog();
        }

        call.enqueue(new BaseResponseModelCallBack<MessageDetails>(this) {

            @Override
            protected void onSuccess(MessageDetails data, String SucMessage) {
                messageDetails = data;
                setShowData(data);

                toReadActiveRequest();
            }


            @Override
            protected void onFinish() {

            }
        });
    }

    /**
     * @param data
     */
    private void setShowData(MessageDetails data) {

        if (data == null) {
            disMissLoading();
            mBinding.getRoot().setVisibility(View.VISIBLE);
            return;
        }

        mSharePhotoUrl = StringUtils.getAsPicListIndexOne(data.getAdvPic());
        mShareContent = subShareContent(StringUtils.delHTMLTag(data.getContent()));

        mBaseBinding.titleView.setMidTitle(data.getTypeName());

        WebSettings webSettings = mBinding.contentLayout.webView.getSettings();
        // 设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);// 可以使用插件
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mBinding.contentLayout.webView.loadData("<style>\n" +           //设置图片自适应
                "img{\n" +
                " max-width:100%;\n" +
                " height:auto;\n" +
                "}\n" +
                "</style>" + data.getContent(), "text/html; charset=UTF-8", "utf-8");
//
//        mAgentWeb = AgentWeb.with(this)
//                .setAgentWebParent(mBinding.contentLayout.llRoot, new LinearLayout.LayoutParams(-1, -1))
//                .useDefaultIndicator()
//                .createAgentWeb()
//                .ready()
//                .go(data.getContent());


        mBinding.contentLayout.tvTime.setText(DateUtil.formatStringData(data.getShowDatetime(), DEFAULT_DATE_FMT));
        mBinding.contentLayout.tvFrom.setText(getString(R.string.message_frome) + data.getSource());
        mBinding.contentLayout.tvTitle.setText(data.getTitle());

        mBinding.contentLayout.tvCollection.setText(data.getReadCount()+"");

        if (TextUtils.isEmpty(data.getAuther())) {
            mBinding.contentLayout.tvAuthor.setText(getString(R.string.author) + "--");
        } else {
            mBinding.contentLayout.tvAuthor.setText(getString(R.string.author) + data.getAuther());
        }



        /*收藏按钮*/
        if (TextUtils.equals(data.getIsCollect(), "0")) {
            mBinding.imgCollection.setImageResource(R.drawable.callection_un);
        } else {
            mBinding.imgCollection.setImageResource(R.drawable.user_collection);
        }

        setLikeInfo(data);

        /*评论数量*/
        if (data.getCommentCount() > 999) {
            mBinding.tvCommentNum.setText("999+");
        } else {
            mBinding.tvCommentNum.setText(data.getCommentCount() + "");
        }


        setRecommendedList(data.getRefNewList());
        setHotCommentList(data.getHotCommentList());

        mNewCommentRefreshHelper.onDefaluteMRefresh(false);
    }

    /**
     * 设置点赞信息
     *
     * @param data
     */
    private void setLikeInfo(MessageDetails data) {
        /*是否点赞*/

        if (data.getIsPoint() == 1) {
            mBinding.contentLayout.imgLike.setImageResource(R.drawable.gave_a_like);
            mBinding.contentLayout.tvLikeNum.setTextColor(ContextCompat.getColor(this, R.color.red));
        } else {
            mBinding.contentLayout.imgLike.setImageResource(R.drawable.gave_a_like_un);
            mBinding.contentLayout.tvLikeNum.setTextColor(ContextCompat.getColor(this, R.color.app_text_gray));
        }


        /*点赞数量*/
        mBinding.contentLayout.tvLikeNum.setText(StringUtils.formatNum(new BigDecimal(data.getPointCount())));
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

        MessageDetailRecomListAdapter messageDetailRecomListAdapter = new MessageDetailRecomListAdapter(refNewList,this);

        messageDetailRecomListAdapter.setOnItemClickListener((adapter, view, position) -> {
            MessageDetailsActivity.open(this, messageDetailRecomListAdapter.getItem(position).getCode());
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
        MsgHotCommentListAdapter msgHotCommentListAdapter = getCommentListAdapter(hotCommentList);
        mBinding.contentLayout.recyclerViewHotComment.setLayoutManager(getLinearLayoutManager());
        mBinding.contentLayout.recyclerViewHotComment.setAdapter(msgHotCommentListAdapter);

    }


    /**
     * 获取布局管理器
     *
     * @return
     */
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
     * 阅读资讯
     */
    private void toReadActiveRequest() {

        if (TextUtils.isEmpty(mCode)) {
            return;
        }

        Map<String, String> map = RetrofitUtils.getRequestMap();

        map.put("objectCode", mCode);

        showLoadingDialog();
        Call call = RetrofitUtils.getBaseAPiService().successRequest("628203", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {


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

        if (mCode == null) return;

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
                if (isShowDialog) {
                    disMissLoading();
                }
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
        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        showLoadingDialog();
        Call call = RetrofitUtils.getBaseAPiService().codeRequest("628200", StringUtils.getJsonToString(map));

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
                UITipDialog.showSuccess(MessageDetailsActivity.this, getString(R.string.comment_succ));
                getMessageDetailRequest();
            }

            @Override
            public void fail() {
                UITipDialog.showSuccess(MessageDetailsActivity.this, getString(R.string.comment_fall));
            }

            @Override
            public void haveSensitive(String str) {
                UITipDialog.showInfo(MessageDetailsActivity.this, str);
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
        map.put("userId", SPUtilHelper.getUserId());

        showLoadingDialog();
        Call call = RetrofitUtils.getBaseAPiService().successRequest("628201", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {

                if (data.isSuccess()) {

                    if (messageDetails != null) {
                        if (messageDetails.getIsPoint() == 0) {
                            messageDetails.setIsPoint(1);
                            messageDetails.setPointCount(messageDetails.getPointCount() + 1);
                        } else {
                            messageDetails.setIsPoint(0);
                            messageDetails.setPointCount(messageDetails.getPointCount() - 1);
                        }
                    }
                    setLikeInfo(messageDetails);

                }
            }

            @Override
            protected void onFinish() {
                disMissLoading();
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
     * 资讯收藏
     */
    private void toMsgCollectionRequest() {

        if (TextUtils.isEmpty(mCode)) {
            return;
        }

        Map<String, String> map = new HashMap<>(); /*类型(Y 1 资讯 2 评论)*/

        map.put("objectCode", mCode);
        map.put("userId", SPUtilHelper.getUserId());

        showLoadingDialog();
        Call call = RetrofitUtils.getBaseAPiService().successRequest("628202", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {

                if (data.isSuccess()) {
                    if (messageDetails != null) {
                        if (TextUtils.equals(messageDetails.getIsCollect(), "0")) {
                            UITipDialog.showSuccess(MessageDetailsActivity.this, getString(R.string.collect_succ));
                            messageDetails.setIsCollect("1");
                            mBinding.imgCollection.setImageResource(R.drawable.user_collection);
                        } else {
                            UITipDialog.showSuccess(MessageDetailsActivity.this, getString(R.string.collect_cancel_succ));
                            messageDetails.setIsCollect("0");
                            mBinding.imgCollection.setImageResource(R.drawable.callection_un);
                        }
                    }
                }
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }


    /**
     * 获取链接并分享
     *
     * @param type 0 微信 1 朋友圈 3分享界面
     */
    public void getUrlToShare(int type) {

        Map<String, String> map = new HashMap<>();
        map.put("ckey", "h5Url");
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("companyCode", MyCdConfig.COMPANYCODE);

        Call call = RetrofitUtils.getBaseAPiService().getKeySystemInfo("628917", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IntroductionInfoModel>(this) {
            @Override
            protected void onSuccess(IntroductionInfoModel data, String SucMessage) {

                if (TextUtils.isEmpty(data.getCvalue())) {
                    return;
                }

                String shareTitle = mBinding.contentLayout.tvTitle.getText().toString();

                if (type == 0) {
                    WxUtil.shareToWX(MessageDetailsActivity.this, data.getCvalue() + "/index.html?code=" + mCode, shareTitle, mShareContent);
                } else if (type == 1) {
                    WxUtil.shareToPYQ(MessageDetailsActivity.this, data.getCvalue() + "/index.html?code=" + mCode, shareTitle, mShareContent);
                } else {
                    ShareActivity.open(MessageDetailsActivity.this, data.getCvalue() + "/index.html?code=" + mCode, shareTitle, mShareContent, mSharePhotoUrl);
                }

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    /**
     * 截取要分享的内容
     *
     * @param shareContent
     * @return
     */
    public String subShareContent(String shareContent) {

        if (TextUtils.isEmpty(shareContent) || shareContent.length() < 60) {
            return "";
        }
        return shareContent.substring(0, 60);
    }

    @Override
    protected void onDestroy() {
        mBinding.contentLayout.webView.clearHistory();
        ((ViewGroup) mBinding.contentLayout.webView.getParent()).removeView(mBinding.contentLayout.webView);
        mBinding.contentLayout.webView.loadUrl("about:blank");
        mBinding.contentLayout.webView.stopLoading();
        mBinding.contentLayout.webView.setWebChromeClient(null);
        mBinding.contentLayout.webView.setWebViewClient(null);
        mBinding.contentLayout.webView.destroy();

        if (mAgentWeb != null)
            mAgentWeb.getWebLifeCycle().onDestroy();

        super.onDestroy();
    }
}
