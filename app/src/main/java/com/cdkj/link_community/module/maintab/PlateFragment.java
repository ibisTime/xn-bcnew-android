package com.cdkj.link_community.module.maintab;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.interfaces.RefreshHelper;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.databinding.FragmentPlateBinding;
import com.cdkj.link_community.model.PlateLlistModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 板块
 * Created by cdkj on 2018/6/12.
 */

public class PlateFragment extends BaseLazyFragment {

    private FragmentPlateBinding mBinding;


    public static PlateFragment getInstance() {
        PlateFragment fragment = new PlateFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_plate, null, false);

        iniRefreshHelper();
        getPlateListDataRequest();
        return mBinding.getRoot();
    }

    /**
     * 初始化刷新操作
     */
    private void iniRefreshHelper() {


    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void onInvisible() {

    }

    public void getPlateListDataRequest() {

        Map<String, String> map = new HashMap<>();

        map.put("start", "1");
        map.put("limit", "15");

        Call<BaseResponseModel<ResponseInListModel<PlateLlistModel>>> call = RetrofitUtils.createApi(MyApiServer.class).getPlateList("628615", StringUtils.getJsonToString(map));

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<PlateLlistModel>>(mActivity) {
            @Override
            protected void onSuccess(ResponseInListModel<PlateLlistModel> data, String SucMessage) {

            }

            @Override
            protected void onFinish() {

            }
        });


    }

}
