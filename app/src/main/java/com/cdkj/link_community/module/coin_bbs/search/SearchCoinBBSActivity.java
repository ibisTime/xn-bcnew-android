package com.cdkj.link_community.module.coin_bbs.search;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.cdkj.baselibrary.adapters.ViewPagerAdapter;
import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.interfaces.RefreshHelper;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.adapters.CoinBBSListAdapter;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.databinding.ActivityMarketSearchBinding;
import com.cdkj.link_community.model.CoinBBSListModel;
import com.cdkj.link_community.model.SearchHistoryModel;
import com.cdkj.link_community.model.StartSearch;
import com.cdkj.link_community.module.coin_bbs.CoinBBSDetailsActivity;
import com.cdkj.link_community.module.coin_bbs.EmptyFragment;
import com.cdkj.link_community.module.market.search.SearchHistoryListFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.link_community.module.market.search.SearchHistoryListFragment.SAVEKEYFORBBS;

/**
 * 币吧搜索
 * Created by cdkj on 2018/3/21.
 */

public class SearchCoinBBSActivity extends AbsBaseLoadActivity {

    private ActivityMarketSearchBinding mBinding;

    private RefreshHelper mRefreshHelper;

    private String mSearchKey;//搜索关键字

    private CoinBBSListAdapter coinBBSListAdapter;


    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, SearchCoinBBSActivity.class);
        context.startActivity(intent);
    }

    /**
     * 不加载title
     *
     * @return false
     */

    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_market_search, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        mBinding.searchLayout.editSerchView.setHint(R.string.please_input_key);

        initRefreshHeper();
        initViewPager();
        initTabView();
        initListener();
        initEditKeyPoard();

    }

    private void initRefreshHeper() {
        mRefreshHelper = new RefreshHelper(this, new BaseRefreshCallBack(this) {
            @Override
            public View getRefreshLayout() {
                mBinding.refreshLayout.setEnableRefresh(false);
                return mBinding.refreshLayout;
            }

            @Override
            public RecyclerView getRecyclerView() {
                //防止局部刷新闪烁
                ((DefaultItemAnimator) mBinding.rv.getItemAnimator()).setSupportsChangeAnimations(false);
                return mBinding.rv;
            }

            @Override
            public RecyclerView.Adapter getAdapter(List listData) {
                coinBBSListAdapter = new CoinBBSListAdapter(listData,false);

                coinBBSListAdapter.setOnItemClickListener((adapter, view, position) -> {
                    if (coinBBSListAdapter.getItem(position) != null) {
                        CoinBBSDetailsActivity.open(SearchCoinBBSActivity.this, coinBBSListAdapter.getItem(position).getCode());
                    }
                });


                coinBBSListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
                    if (!SPUtilHelper.isLogin(SearchCoinBBSActivity.this, false)) {
                        return;
                    }
                    focuseonRequest(coinBBSListAdapter, position);

                });

                return coinBBSListAdapter;
            }

            @Override
            public void getListDataRequest(int pageindex, int limit, boolean isShowDialog) {
                getarketRequest(pageindex, limit, isShowDialog);
            }
        });

        mRefreshHelper.init(MyCdConfig.LISTLIMIT);

    }

    private void initViewPager() {

        //设置fragment数据
        ArrayList fragments = new ArrayList<>();

        fragments.add(EmptyFragment.getInstance());
        fragments.add(SearchHistoryListFragment.getInstance(SAVEKEYFORBBS));

        mBinding.viewpager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));
        mBinding.viewpager.setOffscreenPageLimit(fragments.size());

        mBinding.viewpager.setCurrentItem(1);

    }

    private void initListener() {

        mBinding.searchLayout.tvSearchCancel.setOnClickListener(view -> finish());

    }

    private void initTabView() {

        mBinding.linTabHot.setOnClickListener(view -> {
            setTabShowByIndex(0);
        });

        mBinding.linTabHistory.setOnClickListener(view -> {
            setTabShowByIndex(1);
        });
    }

    /**
     * 设置tab切换状态 0 热门搜索  1 搜索历史
     */
    private void setTabShowByIndex(int typeIndex) {

        if (typeIndex == 0) {
            mBinding.viewpager.setCurrentItem(0, true);
            mBinding.tabIndex1.setVisibility(View.VISIBLE);
            mBinding.tabIndex2.setVisibility(View.INVISIBLE);
            mBinding.tvHotSearch.setTextColor(ContextCompat.getColor(this, R.color.app_text_color));
            mBinding.tvSearchHistory.setTextColor(ContextCompat.getColor(this, R.color.text_black_cd));

        } else {
            mBinding.viewpager.setCurrentItem(1, true);
            mBinding.tabIndex2.setVisibility(View.VISIBLE);
            mBinding.tabIndex1.setVisibility(View.INVISIBLE);
            mBinding.tvSearchHistory.setTextColor(ContextCompat.getColor(this, R.color.app_text_color));
            mBinding.tvHotSearch.setTextColor(ContextCompat.getColor(this, R.color.text_black_cd));
        }

    }

    /**
     * 设置输入键盘
     */
    private void initEditKeyPoard() {
        mBinding.searchLayout.editSerchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(charSequence.toString())) {
                    coinBBSListAdapter.replaceData(new ArrayList<>()); //清空搜索数据
                    mBinding.viewpager.setVisibility(View.VISIBLE);
                    mBinding.refreshLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mBinding.searchLayout.editSerchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                mSearchKey = v.getText().toString();

                if (startSearchByKey()) return false;


                //通知SearchHistoryListFragnemt 保存搜索历史
                SearchHistoryModel searchHistoryModel = new SearchHistoryModel();
                searchHistoryModel.setHistory(mSearchKey);
                EventBus.getDefault().post(searchHistoryModel);


                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);//隐藏键盘
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 开始搜索
     *
     * @return
     */
    private boolean startSearchByKey() {
        if (TextUtils.isEmpty(mSearchKey)) {
            UITipDialog.showInfo(SearchCoinBBSActivity.this, getString(R.string.please_input_search_info));
            return true;
        }

        mBinding.viewpager.setVisibility(View.GONE);
        mBinding.refreshLayout.setVisibility(View.VISIBLE);
        mRefreshHelper.onDefaluteMRefresh(true);
        return false;
    }

    /**
     * 开始搜索
     *
     * @param
     */
    private void getarketRequest(int pageindex, int limit, boolean isShowDialog) {

        if (mSearchKey == null) return;

        Map<String, String> map = new HashMap<>();

        map.put("keywords", mSearchKey);
        map.put("userId", SPUtilHelper.getUserId());
        map.put("start", pageindex + "");
        map.put("limit", limit + "");

        if (isShowDialog) showLoadingDialog();

        Call call = RetrofitUtils.createApi(MyApiServer.class).getCoinBBSList("628237", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<CoinBBSListModel>>(this) {
            @Override
            protected void onSuccess(ResponseInListModel<CoinBBSListModel> data, String SucMessage) {
                mRefreshHelper.setData(data.getList(), getString(R.string.no_search_info), 0);
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


    /**
     * 关注/解除关注
     *
     * @param adapter
     * @param position
     */
    public void focuseonRequest(CoinBBSListAdapter adapter, int position) {

        if (!SPUtilHelper.isLogin(this, false)) {
            return;
        }

        CoinBBSListModel coinBBSListModel = adapter.getItem(position);

        if (coinBBSListModel == null || TextUtils.isEmpty(coinBBSListModel.getCode())) return;

        Map<String, String> map = new HashMap<>();
        map.put("code", coinBBSListModel.getCode());
        map.put("userId", SPUtilHelper.getUserId());

        Call call = RetrofitUtils.getBaseAPiService().successRequest("628240", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {

                if (data.isSuccess()) {

                    if (TextUtils.equals(coinBBSListModel.getIsKeep(), "1")) {
                        coinBBSListModel.setIsKeep("0");
                        UITipDialog.showSuccess(SearchCoinBBSActivity.this, getString(R.string.bbs_cancel_succ));
                    } else {
                        UITipDialog.showSuccess(SearchCoinBBSActivity.this, getString(R.string.bbs_focuse_on_succ));
                        coinBBSListModel.setIsKeep("1");
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
     * 接受搜索历史通知开始搜索
     *
     * @param startSearch
     */
    @Subscribe
    public void StartSearchEvent(StartSearch startSearch) {
        if (startSearch == null) return;
        mSearchKey = startSearch.getStarchKey();
        mBinding.searchLayout.editSerchView.setText(mSearchKey);
        startSearchByKey();
    }

}
