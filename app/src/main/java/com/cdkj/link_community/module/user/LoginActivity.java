package com.cdkj.link_community.module.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.interfaces.LoginInterface;
import com.cdkj.baselibrary.interfaces.LoginPresenter;
import com.cdkj.baselibrary.interfaces.SendCodeInterface;
import com.cdkj.baselibrary.interfaces.SendPhoneCoodePresenter;
import com.cdkj.baselibrary.model.UserLoginModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.databinding.ActivityLoginBinding;
import com.cdkj.link_community.manager.MyRouteHelper;
import com.cdkj.link_community.model.LoinSucc;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import retrofit2.Call;

import static com.cdkj.baselibrary.appmanager.CdRouteHelper.DATASIGN;

/**
 * 登录
 * Created by cdkj on 2017/8/8.
 */
@Route(path = CdRouteHelper.APPLOGIN)
public class LoginActivity extends AbsBaseLoadActivity implements SendCodeInterface {

    private ActivityLoginBinding mBinding;
    private boolean canOpenMain;

    private SendPhoneCoodePresenter mSendCOdePresenter;


    /**
     * 打开当前页面
     *
     * @param context
     */
    public static void open(Context context, boolean canOpenMain) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(DATASIGN, canOpenMain);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_login, null, false);
        return mBinding.getRoot();
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        mSendCOdePresenter = new SendPhoneCoodePresenter(this);

        if (getIntent() != null) {
            canOpenMain = getIntent().getBooleanExtra(DATASIGN, false);
        }

        initListener();

    }

    private void initListener() {
        //登录
        mBinding.btnLogin.setOnClickListener(view -> login());

        //验证码
        mBinding.tvCode.setOnClickListener(view -> checkPhoneNumAndSendCode());

        mBinding.fraFinish.setOnClickListener(view -> {
            finish();
        });


    }

    /**
     * 登录
     */
    private void login() {

        if (TextUtils.isEmpty(mBinding.editUsername.getText().toString())) {
            UITipDialog.showInfo(LoginActivity.this, getString(R.string.please_input_phone));
            return;
        }
        if (TextUtils.isEmpty(mBinding.edtiUserpass.getText().toString())) {
            UITipDialog.showInfo(LoginActivity.this, getString(R.string.please_input_verification_code));
            return;
        }

        Map<String, String> map = RetrofitUtils.getRequestMap();

        map.put("smsCaptcha", mBinding.edtiUserpass.getText().toString());
        map.put("mobile", mBinding.editUsername.getText().toString());
        map.put("kind", MyCdConfig.USERTYPE);

        Call call = RetrofitUtils.getBaseAPiService().userLogin("805173", StringUtils.getJsonToString(map));

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<UserLoginModel>(this) {
            @Override
            protected void onSuccess(UserLoginModel data, String SucMessage) {
                SPUtilHelpr.saveUserId(data.getUserId());
                SPUtilHelpr.saveUserToken(data.getToken());
                SPUtilHelpr.saveUserPhoneNum(mBinding.editUsername.getText().toString());
                startNext();
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    /**
     * 检车手机号是然后发送验证码
     */
    private void checkPhoneNumAndSendCode() {
        if (TextUtils.isEmpty(mBinding.editUsername.getText().toString())) {
            UITipDialog.showFall(LoginActivity.this, getString(R.string.please_input_phone));
            return;
        }
        mSendCOdePresenter.sendCodeRequest(mBinding.editUsername.getText().toString(), "805173", MyCdConfig.USERTYPE, LoginActivity.this);
    }


    @Override
    public void onBackPressed() {
        if (canOpenMain) {
            finish();
        } else {
            super.onBackPressed();
        }
    }


    /**
     * 登录后操作
     */
    private void startNext() {

        EventBus.getDefault().post(new LoinSucc());

        if (canOpenMain) {
            MyRouteHelper.openMain();
        }
        finish();
    }

    @Override
    public void CodeSuccess(String msg) {
        UITipDialog.showSuccess(LoginActivity.this, msg);
        mSubscription.add(startCodeDown(60, mBinding.tvCode));//启动倒计时
    }

    @Override
    public void CodeFailed(String code, String msg) {
        UITipDialog.showFall(LoginActivity.this, msg);
    }

    @Override
    public void StartSend() {
        showLoadingDialog();
    }

    @Override
    public void EndSend() {
        disMissLoading();
    }


    /**
     * 验证码倒计时
     *
     * @param count 秒数
     * @param btn   按钮
     * @return
     */
    public Disposable startCodeDown(final int count, final TextView btn) {
        return Observable.interval(0, 1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())    // 创建一个按照给定的时间间隔发射从0开始的整数序列
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .take(count)//只发射开始的N项数据或者一定时间内的数据
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        RxView.enabled(btn).accept(false);
                        RxTextView.text(btn).accept(count + "");
                    }
                })
                .subscribe(new Consumer<Long>() {
                               @Override
                               public void accept(Long aLong) throws Exception {
                                   RxView.enabled(btn).accept(false);
                                   RxTextView.text(btn).accept((count - aLong) + "秒");
                               }
                           }, throwable -> {
                            RxView.enabled(btn).accept(true);
                            RxTextView.text(btn).accept("验证码");
                        }, () -> {
                            RxView.enabled(btn).accept(true);
                            RxTextView.text(btn).accept("验证码");
                        }
                );
    }


}
