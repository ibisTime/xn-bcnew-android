package com.cdkj.link_community.module.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.base.AbsTablayoutFragment;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.model.MessageType;
import com.cdkj.link_community.model.event.MessageBannerState;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import retrofit2.Call;

/**
 * 资讯
 * Created by cdkj on 2018/3/19.
 */

public class MessageFragment extends AbsTablayoutFragment {

    private List<String> mTitleList;
    private List<Fragment> mFragmentList;

    public static MessageFragment getInstance() {
        MessageFragment fragment = new MessageFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mTitleList = new ArrayList<>();
        mFragmentList = new ArrayList<>();

        getMessageTypeRequest();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void lazyLoad() {

        if (mTabLayoutBinding == null || !mTitleList.isEmpty()) {
            return;
        }

        getMessageTypeRequest();
        postBannerStateEvent(true);
    }

    @Override
    protected void onInvisible() {
        if (mTitleList == null) return;
        postBannerStateEvent(false);
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
    public List<Fragment> getFragments() {
        return mFragmentList;
    }

    @Override
    public List<String> getFragmentTitles() {
        return mTitleList;
    }

    /**
     * 获取消息类型
     */
    public void getMessageTypeRequest() {

        Map<String, String> map = new HashMap<>();

        Call call = RetrofitUtils.createApi(MyApiServer.class).getMessageType("628007", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseListCallBack<MessageType>(mActivity) {
            @Override
            protected void onSuccess(List<MessageType> data, String SucMessage) {

                initViewPagerData(data);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    /**
     * 初始化ViewPagaer数据
     *
     * @param data
     */
    private void initViewPagerData(List<MessageType> data) {

        int i = 0;

        for (MessageType datum : data) {
            if (datum == null) continue;
            mTitleList.add(datum.getName());
            mFragmentList.add(MessageListFragment.getInstance(datum.getCode(), i == 0, datum.getName()));
            i++;
        }
//        setIndicator(mTabLayoutBinding.tablayout,25,25);
//        mTabLayoutBinding.tablayout.post(  new Runnable() {
//            @Override
//            public void run() {
//                setIndicator(mTabLayoutBinding.tablayout,60,60);
//            }
//        });

//        int linWhint = DisplayHelper.dpToPx(25);
//        for (int j = 0; j <  mTabLayoutBinding.tablayout.getChildCount(); j++) {
//
//            View child = mTabLayoutBinding.tablayout.getChildAt(j);
//            child.setPadding(0, 0, 0, 0);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
//            params.leftMargin = 20;
//            params.rightMargin = 20;
////            params.bottomMargin = bottom;
////            params.weight=linWhint;
//            child.setLayoutParams(params);
//            child.invalidate();
//        }

        initViewPager();

        mTabLayoutBinding.viewpager.setOffscreenPageLimit(4);
        if (mTitleList.size() > 4) {
            mTabLayoutBinding.tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        } else {
            mTabLayoutBinding.tablayout.setTabMode(TabLayout.MODE_FIXED);
        }
    }

}
