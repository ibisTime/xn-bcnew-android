package com.cdkj.link_community.interfaces;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.view.View;

import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.databinding.EmptyViewBinding;
import com.cdkj.baselibrary.interfaces.RefreshInterface;

/**
 * 刷新方法回调
 * Created by cdkj on 2017/10/17.
 */

public abstract class DataEmptyToPhotoCallBack<T> implements RefreshInterface<T> {


    public DataEmptyToPhotoCallBack() {
    }


    @Override
    public void showErrorState(String errorMsg, int img) {

    }

    @Override
    public void showEmptyState(String errorMsg, int errorImg) {

    }

    @Override
    public void onRefresh(int pageindex, int limit) {

    }

    @Override
    public void onLoadMore(int pageindex, int limit) {

    }

    @Override
    public void onDestroy() {
    }
}
