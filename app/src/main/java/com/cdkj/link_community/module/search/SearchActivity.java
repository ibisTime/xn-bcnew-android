package com.cdkj.link_community.module.search;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import com.cdkj.baselibrary.adapters.ViewPagerAdapter;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.link_community.R;
import com.cdkj.link_community.databinding.ActivitySearchBinding;
import com.cdkj.link_community.model.event.EventSearch;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by cdkj on 2018/5/17.
 */

public class SearchActivity extends AbsBaseLoadActivity {

    private ActivitySearchBinding mBinding;

    private String mSearchKey;//搜索关键字
    private int mSearchLocation = 0;//搜索位置

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_search, null, false);
        return mBinding.getRoot();
    }
    @Override
    public void afterCreate(Bundle savedInstanceState) {

        initViewPager();

        initTabView();
        initListener();

        initEditKeyBoard();
    }

    private void initListener() {
        mBinding.searchLayout.tvSearchCancel.setOnClickListener(view -> finish());
    }

    private void initTabView() {

        mBinding.linCoin.setOnClickListener(view -> {
            setTabShowByIndex(0);
        });

        mBinding.linExchange.setOnClickListener(view -> {
            setTabShowByIndex(1);
        });

        mBinding.linMsg.setOnClickListener(view -> {
            setTabShowByIndex(2);
        });

        mBinding.linFast.setOnClickListener(view -> {
            setTabShowByIndex(3);
        });

        mBinding.linAct.setOnClickListener(view -> {
            setTabShowByIndex(4);
        });
    }

    /**
     * 设置tab切换状态 0 热门搜索  1 搜索历史
     */
    private void setTabShowByIndex(int typeIndex) {
        mBinding.tabIndex1.setVisibility(View.INVISIBLE);
        mBinding.tvCoin.setTextColor(ContextCompat.getColor(this, R.color.text_black_cd));

        mBinding.tabIndex2.setVisibility(View.INVISIBLE);
        mBinding.tvExchange.setTextColor(ContextCompat.getColor(this, R.color.text_black_cd));

        mBinding.tabIndex3.setVisibility(View.INVISIBLE);
        mBinding.tvMsg.setTextColor(ContextCompat.getColor(this, R.color.text_black_cd));

        mBinding.tabIndex4.setVisibility(View.INVISIBLE);
        mBinding.tvFast.setTextColor(ContextCompat.getColor(this, R.color.text_black_cd));

        mBinding.tabIndex5.setVisibility(View.INVISIBLE);
        mBinding.tvAct.setTextColor(ContextCompat.getColor(this, R.color.text_black_cd));

        switch (typeIndex){
            case 0:
                mBinding.tabIndex1.setVisibility(View.VISIBLE);
                mBinding.tvCoin.setTextColor(ContextCompat.getColor(this, R.color.app_text_color));
                break;

            case 1:
                mBinding.tabIndex2.setVisibility(View.VISIBLE);
                mBinding.tvExchange.setTextColor(ContextCompat.getColor(this, R.color.app_text_color));
                break;

            case 2:
                mBinding.tabIndex3.setVisibility(View.VISIBLE);
                mBinding.tvMsg.setTextColor(ContextCompat.getColor(this, R.color.app_text_color));
                break;

            case 3:
                mBinding.tabIndex4.setVisibility(View.VISIBLE);
                mBinding.tvFast.setTextColor(ContextCompat.getColor(this, R.color.app_text_color));
                break;

            case 4:
                mBinding.tabIndex5.setVisibility(View.VISIBLE);
                mBinding.tvAct.setTextColor(ContextCompat.getColor(this, R.color.app_text_color));
                break;

        }

        mSearchLocation = typeIndex;
        mBinding.viewpager.setCurrentItem(typeIndex, true);

    }

    /**
     * 设置输入键盘
     */
    private void initEditKeyBoard() {
//        mBinding.searchLayout.editSerchView.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (TextUtils.isEmpty(charSequence.toString())) {
//                    searchMarketListAdapter.replaceData(new ArrayList<>()); //清空搜索数据
//                    mBinding.viewpager.setVisibility(View.VISIBLE);
//                    mBinding.linTab.setVisibility(View.VISIBLE);
//                    mBinding.refreshLayout.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });

        mBinding.searchLayout.editSerchView.setOnEditorActionListener((v, actionId, event) -> {
            mSearchKey = v.getText().toString();

            if (TextUtils.isEmpty(mSearchKey)) {
                UITipDialog.showInfo(SearchActivity.this, getString(R.string.please_input_search_info));
                return false;
            }

            EventSearch eventSearch = new EventSearch();
            eventSearch.setContent(mSearchKey);
            eventSearch.setLocation(mSearchLocation);
            EventBus.getDefault().post(eventSearch);


            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);//隐藏键盘
                return true;
            }
            return false;
        });
    }


    protected void initViewPager() {

        //设置fragment数据
        ArrayList fragments = new ArrayList<>();

        fragments.add(SearchCoinFragment.getInstance());
        fragments.add(SearchExchangeFragment.getInstance());
        fragments.add(SearchMsgFragment.getInstance());
        fragments.add(SearchFastFragment.getInstance());
        fragments.add(SearchActFragment.getInstance());

        mBinding.viewpager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));
        mBinding.viewpager.setOffscreenPageLimit(fragments.size());

    }
}
