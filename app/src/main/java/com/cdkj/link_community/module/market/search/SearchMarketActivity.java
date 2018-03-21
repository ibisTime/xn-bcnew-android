package com.cdkj.link_community.module.market.search;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.cdkj.baselibrary.adapters.ViewPagerAdapter;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.link_community.R;
import com.cdkj.link_community.databinding.ActivityMarketSearchBinding;
import com.cdkj.link_community.model.SearchHistoryModel;
import com.cdkj.link_community.module.maintab.FirstPageFragment;
import com.cdkj.link_community.module.maintab.MarketPageFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * 行情搜索
 * Created by cdkj on 2018/3/21.
 */

public class SearchMarketActivity extends AbsBaseLoadActivity {

    private ActivityMarketSearchBinding mBinding;


    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, SearchMarketActivity.class);
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

        initViewPager();
        initTabView();
        initListener();
        initEditKeyPoard();

    }

    private void initViewPager() {

        //设置fragment数据
        ArrayList fragments = new ArrayList<>();

        fragments.add(FirstPageFragment.getInstanse());
        fragments.add(SearchHistoryListFragment.getInstanse());

        mBinding.viewpager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));
        mBinding.viewpager.setOffscreenPageLimit(fragments.size());

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

        mBinding.searchLayout.editSerchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                final String str = v.getText().toString();

                if (TextUtils.isEmpty(str)) {
                    UITipDialog.showInfo(SearchMarketActivity.this, getString(R.string.please_input_search_info));
                    return false;
                }

                //通知SearchHistoryListFragnemt 保存搜索历史
                SearchHistoryModel searchHistoryModel = new SearchHistoryModel();
                searchHistoryModel.setHistory(str);
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


}
