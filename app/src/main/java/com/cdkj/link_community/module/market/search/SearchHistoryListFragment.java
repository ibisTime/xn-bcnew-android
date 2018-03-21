package com.cdkj.link_community.module.market.search;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.baselibrary.databinding.EmptyViewBinding;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.views.MyDividerItemDecoration;
import com.cdkj.link_community.R;
import com.cdkj.link_community.adapters.SearchHistoryListAdapter;
import com.cdkj.link_community.databinding.FragmentShearchHistoryBinding;
import com.cdkj.link_community.databinding.LayoutSearchClearBinding;
import com.cdkj.link_community.model.SearchHistoryModel;
import com.cdkj.link_community.utils.SearchSaveUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 搜索历史列表
 * Created by cdkj on 2018/3/21.
 */

public class SearchHistoryListFragment extends BaseLazyFragment {

    private FragmentShearchHistoryBinding mBinding;

    private final static int SAVESIZE = 30;
    private final static String SAVEKEY = "coinSearch";

    private List<String> searchStrings;

    private SearchHistoryListAdapter mSearchHistoryListAdapter;


    public static SearchHistoryListFragment getInstanse() {
        SearchHistoryListFragment fragment = new SearchHistoryListFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_shearch_history, null, false);
        initAdapter();
        initEmptyView();
        initAdapterFooter();
        initHistoryData();
        return mBinding.getRoot();
    }

    /**
     * 初始化适配器
     */
    private void initAdapter() {
        searchStrings = new ArrayList<>();
        mSearchHistoryListAdapter = new SearchHistoryListAdapter(searchStrings);
        mBinding.reycclerView.setAdapter(mSearchHistoryListAdapter);
        mBinding.reycclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayout.VERTICAL, false));
        mBinding.reycclerView.addItemDecoration(new MyDividerItemDecoration(mActivity, LinearLayout.VERTICAL));
    }

    /**
     * 初始化空提醒
     */
    private void initEmptyView() {
        EmptyViewBinding emptyViewBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.empty_view, null, false);
        mSearchHistoryListAdapter.setEmptyView(emptyViewBinding.getRoot());
        emptyViewBinding.tv.setText(R.string.no_search_history);
        emptyViewBinding.tv.setVisibility(View.VISIBLE);
    }

    /**
     * 设置清除历史按钮
     */
    private void initAdapterFooter() {
        LayoutSearchClearBinding mClearBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.layout_search_clear, null, false);
        mSearchHistoryListAdapter.addFooterView(mClearBinding.getRoot());
        mClearBinding.getRoot().setOnClickListener(view -> clearHistory());
    }

    /**
     * 清除搜索历史
     */
    private void clearHistory() {
        searchStrings.clear();
        mSearchHistoryListAdapter.notifyDataSetChanged();
        SearchSaveUtils.clearSearchInfo(SAVEKEY);
    }

    /**
     * 初始化搜索历史数据
     */
    private void initHistoryData() {
        mSubscription.add(Observable.just(SAVEKEY)
                .observeOn(Schedulers.newThread())
                .map(new Function<String, List<String>>() {
                    @Override
                    public List<String> apply(@io.reactivex.annotations.NonNull String s) throws Exception {
                        return JSON.parseArray(SearchSaveUtils.getSaveSearchInfo(s), String.class);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(strings -> {
                    searchStrings.clear();
                    searchStrings.addAll(strings);
                    mSearchHistoryListAdapter.notifyDataSetChanged();
                }, throwable -> {

                }));
    }

    @Override
    protected void lazyLoad() {
    }

    @Override
    protected void onInvisible() {

    }


    /**
     * 接收通知到的搜索记录
     *
     * @param sm
     */
    @Subscribe
    public void addSearchInfo(SearchHistoryModel sm) {
        if (sm == null) return;
        addSearchInfo(sm.getHistory());
    }

    /**
     * 最多保存getSaveSize个搜索记录
     *
     * @param str
     */
    private void addSearchInfo(String str) {
        if (searchStrings == null || searchStrings.contains(str)) {
            return;
        }
        if (searchStrings.size() >= SAVESIZE) {
            searchStrings.remove(searchStrings.size() - 1);
        }
        searchStrings.add(0, str);
        mSearchHistoryListAdapter.notifyDataSetChanged();
        SearchSaveUtils.saveSearchInfo(SAVEKEY, StringUtils.getJsonToString(searchStrings));
    }


}
