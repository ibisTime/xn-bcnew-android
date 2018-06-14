package com.cdkj.link_community.module.coin_bbs;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;

import com.cdkj.baselibrary.adapters.ViewPagerAdapter;
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
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.CheckUtils;
import com.cdkj.baselibrary.utils.DisplayHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.adapters.BBSCoinMarketListAdapter;
import com.cdkj.link_community.adapters.BBSHotCommentListAdapter;
import com.cdkj.link_community.adapters.MessageListAdapter;
import com.cdkj.link_community.adapters.PlatformListAdapter;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.databinding.ActivityCoinBbsDetailsBinding;
import com.cdkj.link_community.dialog.CommentInputDialog;
import com.cdkj.link_community.model.CoinBBSDetails;
import com.cdkj.link_community.model.CoinBBSHotCircular;
import com.cdkj.link_community.model.CoinListModel;
import com.cdkj.link_community.model.FastMessage;
import com.cdkj.link_community.module.message.MessageDetailsActivity;
import com.cdkj.link_community.views.MyScrollView;
import com.cdkj.link_community.views.ViewPagerIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 币吧详情
 * Created by cdkj on 2018/3/21.
 */

public class CoinBBSDetailsIntoActivity extends AbsBaseLoadActivity {

    private ActivityCoinBbsDetailsBinding mBinding;


    private String mBBSCode;//币吧编号

    private String mToCoin;//用于获取相关连资讯 关联行情
    private String mRequestToCoin;//请求币吧详情参数

    private RefreshHelper mMessageRefreshHelper;//资讯刷新相关
    private RefreshHelper mMarketRefreshHelper;//行情刷新相关
    private RefreshHelper mNewBBSCirclularRefreshHelper;//最新圈子相关

    private int mTabShowIndex;//底部显示页面 0 圈子 1 资讯 2行情

    private CoinBBSDetails.CoinBean mCoinBean;//币种类型 为空时说明是平台

    private ViewTreeObserver.OnGlobalLayoutListener mMoveHeightListener; //用于获取要移动的高度实现悬浮效果
    private int moveHeight = 550;

    /**
     * @param context
     * @param bbsCode 币吧编号
     */
    public static void open(Context context, String bbsCode) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, CoinBBSDetailsIntoActivity.class);
        intent.putExtra(CdRouteHelper.APPLOGIN, bbsCode);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_coin_bbs_details, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        if (getIntent() != null) {
            mRequestToCoin = getIntent().getStringExtra(CdRouteHelper.APPLOGIN);
        }

        mBaseBinding.titleView.setMidTitle(getString(R.string.coin_bbs));


        /*防止局部刷新闪烁*/
        ((DefaultItemAnimator) mBinding.recyclerViewLeft.getItemAnimator()).setSupportsChangeAnimations(false);
        ((DefaultItemAnimator) mBinding.recyclerViewLeft2.getItemAnimator()).setSupportsChangeAnimations(false);

        initViewPagerAndIndicator();
        initRefreshLayout();
        initMoveListener();
        initBBSCirclularRefresh();
        initMessageRefresh();


        initListener();

    }

    private void initListener() {

        //资讯评论
        mBinding.bottomLayout.linComment.setOnClickListener(view -> {

            if (!SPUtilHelper.isLogin(this, false)) {
                return;
            }

            CommentInputDialog commentInputDialog = new CommentInputDialog(this, "");
            commentInputDialog.setmSureListener(comment -> {
                if (TextUtils.isEmpty(comment)) {
                    UITipDialog.showFail(CoinBBSDetailsIntoActivity.this, getString(R.string.please_input_info));
                    return;
                }

                toPostBBSRequest(mBBSCode, comment);

            });
            commentInputDialog.show();
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        getBBSDetails();
    }

    @Override
    protected void onDestroy() {
        mBinding.linTop.getViewTreeObserver().removeGlobalOnLayoutListener(mMoveHeightListener);
        super.onDestroy();
    }

    /**
     * 获取移动距离 监听ScrollView
     */
    private void initMoveListener() {

        mMoveHeightListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                moveHeight = mBinding.linTop.getHeight() + DisplayHelper.dpToPx(20);
            }
        };
        mBinding.linTop.getViewTreeObserver().addOnGlobalLayoutListener(mMoveHeightListener);

        //用于监听滚动实现 悬浮效果
        mBinding.scrollView.setOnScrollListener(new MyScrollView.MyOnScrollListener() {
            @Override
            public void onScroll(int y) {

            }

            @Override
            public void onCurrentY(int y) {
                if (y >= moveHeight) {
                    mBinding.viewindicatorTop.setVisibility(View.VISIBLE);
                } else {
                    mBinding.viewindicatorTop.setVisibility(View.GONE);
                }
            }
        });

    }

    private void initRefreshLayout() {
        mBinding.refreshLayout.setEnableRefresh(false);
        mBinding.refreshLayout.setEnableLoadmore(true);
        mBinding.refreshLayout.setEnableLoadmoreWhenContentNotFull(true);
        mBinding.refreshLayout.setEnableScrollContentWhenLoaded(false);

        mBinding.refreshLayout.setOnLoadmoreListener(refreshlayout -> {

            switch (mTabShowIndex) {
                case 0:
                    mNewBBSCirclularRefreshHelper.onDefaluteMLoadMore(false);
                    break;
                case 1:
                    mMessageRefreshHelper.onDefaluteMLoadMore(false);
                    break;
                case 2:
                    mMarketRefreshHelper.onDefaluteMLoadMore(false);
                    break;
            }

        });
    }

    /**
     * 初始化底部tab切换
     */
    private void initViewPagerAndIndicator() {
        //设置fragment数据
        ArrayList fragments = new ArrayList<>();

        fragments.add(EmptyFragment.getInstance());
        fragments.add(EmptyFragment.getInstance());
        fragments.add(EmptyFragment.getInstance());

        mBinding.viewpager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));
        mBinding.viewpager.setOffscreenPageLimit(fragments.size());

        mBinding.viewindicator.setmLinWidth(25);
        mBinding.viewindicator.setVisibleTabCount(fragments.size());
        mBinding.viewindicator.setTabItemTitles(Arrays.asList(getString(R.string.bbs_circular), getString(R.string.msg), getString(R.string.market)));
        mBinding.viewindicator.setViewPager(mBinding.viewpager, 0);


        mBinding.viewindicatorTop.setmLinWidth(25);
        mBinding.viewindicatorTop.setVisibleTabCount(fragments.size());
        mBinding.viewindicatorTop.setTabItemTitles(Arrays.asList(getString(R.string.bbs_circular), getString(R.string.msg), getString(R.string.market)));
        mBinding.viewindicatorTop.setViewPager(mBinding.viewpager, 0);
        mBinding.viewindicatorTop.post(() -> mBinding.viewindicatorTop.setVisibility(View.GONE));

        mBinding.viewindicator.setOnPageChangeListener(new ViewPagerIndicator.PageOnchangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mTabShowIndex = position;
                switch (position) {
                    case 0:
                        mNewBBSCirclularRefreshHelper.onDefaluteMRefresh(true);
                        mBinding.relaLeft.setVisibility(View.VISIBLE);
                        mBinding.recyclerViewMiddel.setVisibility(View.GONE);
                        mBinding.recyclerViewRight.setVisibility(View.GONE);
                        mBinding.bottomLayout.linComment.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        mMessageRefreshHelper.onDefaluteMRefresh(true);
                        mBinding.relaLeft.setVisibility(View.GONE);
                        mBinding.recyclerViewMiddel.setVisibility(View.VISIBLE);
                        mBinding.recyclerViewRight.setVisibility(View.GONE);
                        mBinding.bottomLayout.linComment.setVisibility(View.GONE);
                        break;
                    case 2:
                        if (mMarketRefreshHelper == null) {
                            initMarketRefresh();
                        }
                        mMarketRefreshHelper.onDefaluteMRefresh(true);
                        mBinding.relaLeft.setVisibility(View.GONE);
                        mBinding.recyclerViewMiddel.setVisibility(View.GONE);
                        mBinding.recyclerViewRight.setVisibility(View.VISIBLE);
                        mBinding.bottomLayout.linComment.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 初始化资讯刷新
     */
    private void initMessageRefresh() {

        mMessageRefreshHelper = new RefreshHelper(this, new BaseRefreshCallBack(this) {
            @Override
            public View getRefreshLayout() {
                return mBinding.refreshLayout;
            }

            @Override
            public RecyclerView getRecyclerView() {
                return mBinding.recyclerViewMiddel;
            }

            @Override
            public RecyclerView.Adapter getAdapter(List listData) {
                MessageListAdapter msgAdapter = new MessageListAdapter(listData, CoinBBSDetailsIntoActivity.this);

                msgAdapter.setOnItemClickListener((adapter, view, position) -> {
                    MessageDetailsActivity.open(CoinBBSDetailsIntoActivity.this, msgAdapter.getItem(position).getCode());
                });

                return msgAdapter;
            }

            @Override
            public void getListDataRequest(int pageindex, int limit, boolean isShowDialog) {
                getMsgListRequest(pageindex, limit, isShowDialog);
            }

            @Override
            public void reLoad() {
                mMessageRefreshHelper.onDefaluteMRefresh(true);
            }
        });
        mMessageRefreshHelper.init(MyCdConfig.LISTLIMIT);
        mBinding.recyclerViewMiddel.setNestedScrollingEnabled(false);
        mBinding.recyclerViewMiddel.setLayoutManager(getLinearLayoutManager());
    }

    /**
     * 初始化行情资讯刷新
     */
    private void initMarketRefresh() {

        mMarketRefreshHelper = new RefreshHelper(this, new BaseRefreshCallBack(this) {
            @Override
            public View getRefreshLayout() {
                return mBinding.refreshLayout;
            }

            @Override
            public RecyclerView getRecyclerView() {
                return mBinding.recyclerViewRight;
            }

            @Override
            public RecyclerView.Adapter getAdapter(List listData) {
                if (isCoinType()) {
                    return new BBSCoinMarketListAdapter(listData, mToCoin);
                }
                return new PlatformListAdapter(listData);
            }

            @Override
            public void getListDataRequest(int pageindex, int limit, boolean isShowDialog) {
                getMarketListRequest(pageindex, limit, isShowDialog);
            }

            @Override
            public void reLoad() {
                mMarketRefreshHelper.onDefaluteMRefresh(true);
            }
        });
        mMarketRefreshHelper.init(MyCdConfig.LISTLIMIT);
        mBinding.recyclerViewRight.setNestedScrollingEnabled(false);
        mBinding.recyclerViewRight.setLayoutManager(getLinearLayoutManager());
    }

    /**
     * 初始化圈子刷新
     */
    private void initBBSCirclularRefresh() {

        mNewBBSCirclularRefreshHelper = new RefreshHelper(this, new BaseRefreshCallBack(this) {
            @Override
            public View getRefreshLayout() {
                return mBinding.refreshLayout;
            }

            @Override
            public RecyclerView getRecyclerView() {
                return mBinding.recyclerViewLeft2;
            }

            @Override
            public RecyclerView.Adapter getAdapter(List listData) {
                return getCircularListAdapter(listData);
            }

            @Override
            public void getListDataRequest(int pageindex, int limit, boolean isShowDialog) {
                getNewCircularListRequest(pageindex, limit, isShowDialog);
            }

            @Override
            public void reLoad() {
                mNewBBSCirclularRefreshHelper.onDefaluteMRefresh(true);
            }
        });
        mNewBBSCirclularRefreshHelper.init(MyCdConfig.LISTLIMIT);
        mBinding.recyclerViewLeft2.setNestedScrollingEnabled(false);
        mBinding.recyclerViewLeft2.setLayoutManager(getLinearLayoutManager());
    }

    /**
     * 获取圈子评论
     *
     * @param listData
     * @return
     */
    @NonNull
    private BBSHotCommentListAdapter getCircularListAdapter(List listData) {
        BBSHotCommentListAdapter bbsHotCommentListAdapter = new BBSHotCommentListAdapter(listData);

        bbsHotCommentListAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (bbsHotCommentListAdapter.getItem(position) == null) return;
            BBSCommentDetailsActivity.open(CoinBBSDetailsIntoActivity.this, bbsHotCommentListAdapter.getItem(position).getCode());
        });

        bbsHotCommentListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            circularLikeRequest(bbsHotCommentListAdapter, position);
        });
        return bbsHotCommentListAdapter;
    }


    public void getBBSDetails() {

        if (TextUtils.isEmpty(mRequestToCoin)) return;

        Map<String, String> map = new HashMap<>();

        map.put("toCoin", mRequestToCoin);
        map.put("userId", SPUtilHelper.getUserId());

        showLoadingDialog();

        Call call = RetrofitUtils.createApi(MyApiServer.class).getCoinBBsDetails("628239", StringUtils.getJsonToString(map));

        call.enqueue(new BaseResponseModelCallBack<CoinBBSDetails>(this) {
            @Override
            protected void onSuccess(CoinBBSDetails data, String SucMessage) {
                setShowData(data);
                mNewBBSCirclularRefreshHelper.onDefaluteMRefresh(false);
                if (mBinding.linTop.getVisibility() != View.VISIBLE) {
                    mBinding.linTop.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });


    }

    /**
     * 设置显示数据
     *
     * @param data
     */
    private void setShowData(CoinBBSDetails data) {

        if (data == null) return;

        mToCoin = data.getToCoin();
        mBBSCode = data.getCode();
        mCoinBean = data.getCoin();

        if (isCoinType()) {
            mBinding.tvTodayChange.setText(getString(R.string.quote_change) + data.getCoin().getTodayChange() + "%");
            mBinding.tvTodayVol.setText(getString(R.string.todayvol) + StringUtils.formatNum(data.getCoin().getTodayVol()));

            mBinding.tvCirculation.setText(StringUtils.formatNum(data.getCoin().getTotalSupply()));
            mBinding.tvIssue.setText(StringUtils.formatNum(data.getCoin().getMaxSupply()));
            mBinding.tvIssueMarket.setText(StringUtils.formatNum(data.getCoin().getMarketCap()));

            mBinding.tvIssueRank.setText(data.getCoin().getRank());


        } else {
            mBinding.tvCirculation.setText("- -");
            mBinding.tvIssue.setText("- -");
            mBinding.tvIssueMarket.setText("- -");
            mBinding.tvIssueRank.setText("- -");

            mBinding.tvTodayChange.setText("");
            mBinding.tvTodayVol.setText("");

        }
        mBinding.tvName.setText("#" + data.getName() + "#");
        mBinding.tvFocusOnNum.setText(getString(R.string.focus_num) + StringUtils.formatNum(data.getKeepCount()));
        mBinding.tvPostNum.setText(getString(R.string.post_num) + StringUtils.formatNum(data.getPostCount()));

        mBinding.tvTodayNum.setText(StringUtils.formatNum(data.getDayCommentCount()));

        mBinding.expandTextView.setText(data.getIntroduce() + "");

        setHotCommentList(data.getHotPostList());
    }

    /**
     * 是否是币种币吧
     *
     * @return
     */
    private boolean isCoinType() {
        return mCoinBean != null;//如果为空说明是平台币吧 否则是币种币吧
    }


    /**
     * 设置热门评价
     *
     * @param
     */
    private void setHotCommentList(List<CoinBBSHotCircular> hotCommentList) {
        if (hotCommentList == null || hotCommentList.isEmpty()) {
            mBinding.recyclerViewLeft.setVisibility(View.GONE);
            mBinding.tvHotTitle.setVisibility(View.GONE);
            return;
        }

        mBinding.recyclerViewLeft.setVisibility(View.VISIBLE);
        mBinding.tvHotTitle.setVisibility(View.VISIBLE);

        BBSHotCommentListAdapter msgHotCommentListAdapter = getCircularListAdapter(hotCommentList);
        mBinding.recyclerViewLeft.setLayoutManager(getLinearLayoutManager());
        mBinding.recyclerViewLeft.setAdapter(msgHotCommentListAdapter);

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
     * 获取资讯请求
     *
     * @param pageindex
     * @param limit
     * @param isShowDialog
     */
    public void getMsgListRequest(int pageindex, int limit, boolean isShowDialog) {

        if (TextUtils.isEmpty(mBBSCode)) {
            mMessageRefreshHelper.setData(new ArrayList(), getString(R.string.no_msg), R.drawable.no_dynamic);
            return;
        }

        Map<String, String> map = new HashMap<>();

        map.put("start", pageindex + "");
        map.put("limit", limit + "");
        map.put("toCoin", mBBSCode);

        if (isShowDialog) showLoadingDialog();

        Call call = RetrofitUtils.createApi(MyApiServer.class).getMsgList("628205", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<FastMessage>>(this) {
            @Override
            protected void onSuccess(ResponseInListModel<FastMessage> data, String SucMessage) {
                mMessageRefreshHelper.setData(data.getList(), getString(R.string.no_msg), R.drawable.no_dynamic);
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                super.onReqFailure(errorCode, errorMessage);
                mMessageRefreshHelper.loadError(errorMessage, 0);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    /**
     * 获取行情列表请求
     *
     * @param pageindex
     * @param limit
     * @param isShowDialog
     */
    public void getMarketListRequest(int pageindex, int limit, boolean isShowDialog) {

        if (TextUtils.isEmpty(mToCoin)) {
            mMarketRefreshHelper.setData(new ArrayList(), getString(R.string.no_add_market), R.drawable.no_dynamic);
            return;
        }

        Map<String, String> map = new HashMap<>();

        if (isCoinType()) {
            map.put("toSymbol", mToCoin);
        } else {
            map.put("exchangeEname", mToCoin);
        }

        map.put("start", pageindex + "");
        map.put("limit", limit + "");
        map.put("percentPeriod", "24h");

        if (isShowDialog) showLoadingDialog();

        Call call = RetrofitUtils.createApi(MyApiServer.class).getCoinList("628350", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<CoinListModel>>(this) {
            @Override
            protected void onSuccess(ResponseInListModel<CoinListModel> data, String SucMessage) {
                mMarketRefreshHelper.setData(data.getList(), getString(R.string.no_add_market), R.drawable.no_dynamic);
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                super.onReqFailure(errorCode, errorMessage);
                mMarketRefreshHelper.loadError(errorMessage, 0);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    /**
     * 获取最新圈子
     *
     * @param pageindex
     * @param limit
     * @param isShowDialog
     */
    public void getNewCircularListRequest(int pageindex, int limit, boolean isShowDialog) {

        if (TextUtils.isEmpty(mBBSCode)) {
            mNewBBSCirclularRefreshHelper.setData(new ArrayList(), getString(R.string.no_bbs_circular), R.drawable.no_dynamic);
            return;
        }

        Map<String, String> map = new HashMap<>();

        map.put("plateCode", mBBSCode);
        map.put("start", pageindex + "");
        map.put("limit", limit + "");
        map.put("userId", SPUtilHelper.getUserId());

        if (isShowDialog) showLoadingDialog();

        Call call = RetrofitUtils.createApi(MyApiServer.class).getCoinBBSCircularList("628662", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<CoinBBSHotCircular>>(this) {
            @Override
            protected void onSuccess(ResponseInListModel<CoinBBSHotCircular> data, String SucMessage) {
                mNewBBSCirclularRefreshHelper.setData(data.getList(), getString(R.string.no_bbs_circular), R.drawable.no_dynamic);
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                super.onReqFailure(errorCode, errorMessage);
                mNewBBSCirclularRefreshHelper.loadError(errorMessage, 0);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }


    /**
     * 圈子点赞
     */
    private void circularLikeRequest(BBSHotCommentListAdapter adapter, int position) {
        if (!SPUtilHelper.isLogin(this, false)) {
            return;
        }

        CoinBBSHotCircular coinBBSHotCircular = adapter.getItem(position);

        if (TextUtils.isEmpty(coinBBSHotCircular.getCode())) {
            return;
        }

        Map<String, String> map = new HashMap<>(); /*类型(Y 1 资讯 2 评论)*/

        map.put("type", "1"); /*1 帖子，2评论*/
        map.put("objectCode", coinBBSHotCircular.getCode());
        map.put("userId", SPUtilHelper.getUserId());

        showLoadingDialog();
        Call call = RetrofitUtils.getBaseAPiService().successRequest("628653", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {

                if (data.isSuccess()) {
                    if (TextUtils.equals(coinBBSHotCircular.getIsPoint(), "1")) {
                        coinBBSHotCircular.setPointCount(coinBBSHotCircular.getPointCount() - 1);
                        coinBBSHotCircular.setIsPoint("0");
                    } else {
                        coinBBSHotCircular.setPointCount(coinBBSHotCircular.getPointCount() + 1);
                        coinBBSHotCircular.setIsPoint("1");
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
    private void toCommentRequest(String code, String content) {

        if (TextUtils.isEmpty(code)) {
            return;
        }

        Map<String, String> map = new HashMap<>(); /*类型(Y 1 帖子 2 评论)*/

        map.put("type", "1");
        map.put("objectCode", code);
        map.put("content", content);
        map.put("userId", SPUtilHelper.getUserId());

        showLoadingDialog();
        Call call = RetrofitUtils.getBaseAPiService().successRequest("628652", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {

                if (data.isSuccess()) {
                    UITipDialog.showSuccess(CoinBBSDetailsIntoActivity.this, getString(R.string.comment_succ));
                    mNewBBSCirclularRefreshHelper.onDefaluteMRefresh(false);
                } else {
                    UITipDialog.showSuccess(CoinBBSDetailsIntoActivity.this, getString(R.string.comment_fall));
                }
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    /**
     * 发帖
     */
    private void toPostBBSRequest(String code, String content) {

        if (TextUtils.isEmpty(code)) {
            return;
        }

        Map<String, String> map = new HashMap<>(); /*类型(Y 1 帖子 2 评论)*/

        map.put("plateCode", code);
        map.put("content", content);
        map.put("userId", SPUtilHelper.getUserId());

        showLoadingDialog();
        Call call = RetrofitUtils.getBaseAPiService().codeRequest("628650", StringUtils.getJsonToString(map));

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
                UITipDialog.showSuccess(CoinBBSDetailsIntoActivity.this, getString(R.string.release_succ));
                mNewBBSCirclularRefreshHelper.onDefaluteMRefresh(false);
            }

            @Override
            public void fail() {
                UITipDialog.showSuccess(CoinBBSDetailsIntoActivity.this, getString(R.string.release_fail));
            }

            @Override
            public void haveSensitive(String str) {
                UITipDialog.showInfo(CoinBBSDetailsIntoActivity.this, str);
            }
        });
    }

}
