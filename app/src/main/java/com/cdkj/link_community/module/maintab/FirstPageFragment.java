package com.cdkj.link_community.module.maintab;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.adapters.ViewPagerAdapter;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.link_community.R;
import com.cdkj.link_community.databinding.FragmentFirstPageBinding;
import com.cdkj.link_community.model.event.MessageBannerState;
import com.cdkj.link_community.module.message.FastMessageFragment;
import com.cdkj.link_community.module.message.MessageFragment;
import com.cdkj.link_community.module.search.SearchActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * 首页 资讯
 * Created by cdkj on 2018/3/13.
 */

public class FirstPageFragment extends BaseLazyFragment {

    private FragmentFirstPageBinding mBinding;


    public static FirstPageFragment getInstance() {
        FirstPageFragment fragment = new FirstPageFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_first_page, null, false);

        initTopTitle();

        initViewPager();

        return mBinding.getRoot();
    }

    private void initTopTitle() {

        //搜索
        mBinding.topLayout.fraToSearch.setOnClickListener(view -> SearchActivity.open(mActivity));

        mBinding.topLayout.radiogroup.setOnCheckedChangeListener((radioGroup, i) -> {

            switch (i) {
                case R.id.radio_left:
                    mBinding.viewpager.setCurrentItem(0, true);
                    break;
                case R.id.radio_right:
                    mBinding.viewpager.setCurrentItem(1, true);
                    break;
            }

        });

    }

    private void initViewPager() {
        //设置fragment数据
        ArrayList fragments = new ArrayList<>();

        fragments.add(MessageFragment.getInstance());
        fragments.add(FastMessageFragment.getInstance());

        mBinding.viewpager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(), fragments));
        mBinding.viewpager.setOffscreenPageLimit(fragments.size());

    }


    /**
     * 发生banner状态控制
     */
    private void postBannerStateEvent(boolean isStop) {
        mSubscription.add(Observable.just("")        //开启banner
                .throttleFirst(3, TimeUnit.SECONDS)  //防止用户过快点击
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    MessageBannerState bannerStop = new MessageBannerState();
                    bannerStop.setStop(isStop);
                    EventBus.getDefault().post(bannerStop);
                }));
    }

    @Override
    protected void lazyLoad() {
        if (mBinding == null) return;
        postBannerStateEvent(false);
    }


    @Override
    protected void onInvisible() {
        if (mBinding == null) return;
        postBannerStateEvent(true);
    }
}
