package com.cdkj.link_community.module.plate;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.BigDecimalUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.adapters.PlateDetailAdapter;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.databinding.ActivityPlateDetailsBinding;
import com.cdkj.link_community.model.PlateDetailsModel;
import com.cdkj.link_community.utils.AccountUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * 版块详情
 * Created by cdkj on 2018/6/13.
 */

public class PlateDetailsActivity extends AbsBaseLoadActivity {

    private ActivityPlateDetailsBinding mBinding;

    private String mPlateCode;

    private PlateDetailsModel mPlateDetailsModel;


    /**
     * @param context
     * @param plateCode 版块Id
     */
    public static void open(Context context, String plateCode) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, PlateDetailsActivity.class);
        intent.putExtra(CdRouteHelper.DATA_SIGN, plateCode);
        context.startActivity(intent);
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_plate_details, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void topTitleViewRightClick() {
        if (mPlateDetailsModel != null) {
            PlateIntroduceActivity.open(this, mPlateDetailsModel.getDescription(), mPlateDetailsModel.getName());
        }
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mPlateCode = getIntent().getStringExtra(CdRouteHelper.DATA_SIGN);
        mBaseBinding.titleView.setRightTitle(getString(R.string.introduce));

        mBinding.getRoot().setVisibility(View.GONE);

        getPlateDetailsRequest();
    }


    /**
     * 获取版块详情
     */
    public void getPlateDetailsRequest() {

        if (TextUtils.isEmpty(mPlateCode)) {
            return;
        }

        Map<String, String> map = new HashMap<>();

        map.put("code", mPlateCode);

        Call<BaseResponseModel<PlateDetailsModel>> call = RetrofitUtils.createApi(MyApiServer.class).getPlateDetails("628616", StringUtils.getJsonToString(map));

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<PlateDetailsModel>(this) {
            @Override
            protected void onSuccess(PlateDetailsModel data, String SucMessage) {
                mPlateDetailsModel = data;
                mBaseBinding.titleView.setMidTitle(data.getName());
                setShowData(data);
            }

            @Override
            protected void onFinish() {
                mBinding.getRoot().setVisibility(View.VISIBLE);
                disMissLoading();
            }
        });
    }

    /**
     * 设置显示数据
     *
     * @param data
     */
    private void setShowData(PlateDetailsModel data) {

        if (data == null) return;

        mBinding.tvName.setText(data.getName());

        if (TextUtils.isEmpty(data.getBestSymbol())) {
            mBinding.tvBestName.setText("--");
        } else {
            mBinding.tvBestName.setText(data.getBestSymbol());
        }

        mBinding.tvBestChange.setText(AccountUtil.getShowString(BigDecimalUtils.multiply(data.getBestChange(), new BigDecimal(100))));
        mBinding.tvBestChange.setTextColor(AccountUtil.getShowColor(data.getBestChange()));

        if (TextUtils.isEmpty(data.getWorstSymbol())) {
            mBinding.tvWorstName.setText("--");
        } else {
            mBinding.tvWorstName.setText(data.getWorstSymbol());
        }

        mBinding.tvWorstChange.setText(AccountUtil.getShowString(BigDecimalUtils.multiply(new BigDecimal(100), data.getWorstChange())));
        mBinding.tvWorstChange.setTextColor(AccountUtil.getShowColor(data.getWorstChange()));

        mBinding.tvUpCount.setText(getString(R.string.up_coin) + ":" + data.getUpCount());
        mBinding.tvDownCount.setText(getString(R.string.down_coin) + ":" + data.getDownCount());
        mBinding.tvTotalCount.setText(getString(R.string.total_coin_) + ":" + data.getTotalCount());

        mBinding.tvAcgChange.setText(AccountUtil.getShowString(BigDecimalUtils.multiply(data.getAvgChange(), new BigDecimal(100))));

        mBinding.tvAcgChange.setTextColor(AccountUtil.getShowColor(data.getAvgChange()));

        /*币种信息*/
        PlateDetailAdapter plateDetailAdapter = new PlateDetailAdapter(data.getList());

        mBinding.recyclerAll.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        mBinding.recyclerAll.setAdapter(plateDetailAdapter);
    }

}
