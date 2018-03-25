package com.cdkj.link_community.module.market;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import com.cdkj.baselibrary.base.AbsTablayoutActivity;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.model.AddMarketModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 行情自选 Activity
 * Created by cdkj on 2018/3/24.
 */

public class AddMarketActivity extends AbsTablayoutActivity {


    private List<String> mTitleList;
    private List<Fragment> mFragmentList;

    /**
     * @param context
     */
    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, AddMarketActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle(getString(R.string.add_market_title));
        mTitleList = new ArrayList<>();
        mFragmentList = new ArrayList<>();
        getTypeRequest();
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
     * 获取平台请求
     */
    private void getTypeRequest() {

        Map<String, String> map = new HashMap<>();

        Call call = RetrofitUtils.createApi(MyApiServer.class).getAddMarketList("628335", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseListCallBack<AddMarketModel>(this) {

            @Override
            protected void onSuccess(List<AddMarketModel> data, String SucMessage) {

                initViewPagerData(data);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    private void initViewPagerData(List<AddMarketModel> data) {
        int i = 0;

        for (AddMarketModel type : data) {
            if (type == null) continue;
            mTitleList.add(type.getSname());
            mFragmentList.add(AddMarketListFragment.getInstanse(type, i == 0));
            i++;
        }

        initViewPager();
        mTabLayoutBinding.viewpager.setOffscreenPageLimit(4);
        mTabLayoutBinding.tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

}
