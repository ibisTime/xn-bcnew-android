package com.cdkj.link_community.module.active;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.link_community.R;
import com.cdkj.link_community.databinding.ActivityActiveApproveBinding;
import com.cdkj.link_community.model.ActiveApproveSuccess;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.baselibrary.appmanager.CdRouteHelper.DATA_SIGN;

/**
 * Created by cdkj on 2018/4/29.
 */

public class ActiveApproveActivity extends AbsBaseLoadActivity {

    private ActivityActiveApproveBinding mBinding;

    private String code;

    /**
     * @param context
     * @param code
     */
    public static void open(Context context, String code) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, ActiveApproveActivity.class);
        intent.putExtra(DATA_SIGN, code);
        context.startActivity(intent);
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_active_approve, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        if (getIntent() == null)
            return;

        code = getIntent().getStringExtra(DATA_SIGN);

        mBaseBinding.titleView.setMidTitle("填写报名表");
        mBaseBinding.titleView.setRightTitle("报名");

        mBaseBinding.titleView.setRightFraClickListener(view -> {
            if (check()) {
                approve();
            }

        });

    }

    private boolean check() {
        if (mBinding.edtName.getText().toString().equals("")) {
            ToastUtil.show(this, "请填写真实姓名");
            return false;
        }

        if (mBinding.edtMobile.getText().toString().equals("")) {
            ToastUtil.show(this, "请填写手机号码");
            return false;
        }

        return true;
    }

    private void approve() {

        if (!SPUtilHelper.isLogin(this, false)) {
            return;
        }

        Map<String, String> map = new HashMap<>();

        map.put("code", code);
        map.put("token", SPUtilHelper.getUserToken());
        map.put("realName", mBinding.edtName.getText().toString());
        map.put("mobile", mBinding.edtMobile.getText().toString());

        showLoadingDialog();
        Call call = RetrofitUtils.getBaseAPiService().successRequest("628520", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {

                if (data.isSuccess()) {
                    ToastUtil.show(ActiveApproveActivity.this, "报名成功");
                    EventBus.getDefault().post(new ActiveApproveSuccess()); //通知上级界面报名成功
                    finish();
                }
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }
}
