package com.cdkj.link_community.module.maintab;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.adapters.ViewPagerAdapter;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.link_community.R;
import com.cdkj.link_community.databinding.FragmentMarketBinding;
import com.cdkj.link_community.module.market.AddMarketActivity;
import com.cdkj.link_community.module.market.CoinTypeFragment2;
import com.cdkj.link_community.module.market.MyChooseFragment;
import com.cdkj.link_community.module.market.PlatformFragment;
import com.cdkj.link_community.module.search.SearchActivity;

import java.util.ArrayList;

/**
 * 平台   自选   币种
 * Created by cdkj on 2018/3/13.
 */

public class MarketPageFragment extends BaseLazyFragment {

    private FragmentMarketBinding mBinding;


    public static MarketPageFragment getInstance() {
        MarketPageFragment fragment = new MarketPageFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_market, null, false);

        initTopTitle();

        initViewPager();

        return mBinding.getRoot();
    }

    private void initTopTitle() {

        //行情添加
        mBinding.titleLayout.fraAddMarket.setOnClickListener(view -> addClick());

        //搜索
        mBinding.titleLayout.fraToSearch.setOnClickListener(view -> SearchActivity.open(mActivity));

        mBinding.titleLayout.radiogroup.setOnCheckedChangeListener((radioGroup, i) -> {

            switch (i) {
                case R.id.radio_left:
                    mBinding.viewpager.setCurrentItem(0, true);
                    break;
                case R.id.radio_middle:
                    mBinding.viewpager.setCurrentItem(1, true);
                    break;
                case R.id.radio_right:
                    mBinding.viewpager.setCurrentItem(2, true);
                    break;
            }

        });

    }

    /**
     * 行情添加
     */
    private void addClick() {
        if (!SPUtilHelper.isLogin(mActivity, false)) {
            return;
        }
        AddMarketActivity.open(mActivity);
    }

    private void initViewPager() {
        //设置fragment数据
        ArrayList fragments = new ArrayList<>();

        fragments.add(PlatformFragment.getInstance());
        fragments.add(CoinTypeFragment2.getInstance());
        fragments.add(MyChooseFragment.getInstance());

        mBinding.viewpager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(), fragments));
        mBinding.viewpager.setOffscreenPageLimit(fragments.size());


    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void onInvisible() {

    }
}
