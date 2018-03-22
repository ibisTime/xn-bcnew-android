package com.cdkj.link_community.module.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.cdkj.link_community.R;
import com.cdkj.link_community.databinding.ActivityLoginBinding;
import com.cdkj.link_community.manager.MyRouteHelper;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.cdkj.baselibrary.appmanager.CdRouteHelper.DATASIGN;

/**
 * 登录
 * Created by cdkj on 2017/8/8.
 */
@Route(path = CdRouteHelper.APPLOGIN)
public class LoginActivity extends AbsBaseLoadActivity implements LoginInterface, SendCodeInterface {

    private LoginPresenter mPresenter;
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
        mPresenter = new LoginPresenter(this);

        if (getIntent() != null) {
            canOpenMain = getIntent().getBooleanExtra(DATASIGN, false);
        }

        initListener();

    }

    private void initListener() {
        //登录
        mBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.login(mBinding.editUsername.getText().toString(), mBinding.edtiUserpass.getText().toString(), LoginActivity.this);
            }
        });

        //验证码
        mBinding.tvCode.setOnClickListener(view -> checkPhoneNumAndSendCode());

        mBinding.fraFinish.setOnClickListener(view -> {
            finish();
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
        mSendCOdePresenter.sendCodeRequest(mBinding.editUsername.getText().toString(), "805041", MyCdConfig.USERTYPE, LoginActivity.this);
    }


    @Override
    public void LoginSuccess(UserLoginModel user, String msg) {
        SPUtilHelpr.saveUserId(user.getUserId());
        SPUtilHelpr.saveUserToken(user.getToken());
        SPUtilHelpr.saveUserPhoneNum(mBinding.editUsername.getText().toString());
        startNext();
    }

    @Override
    public void LoginFailed(String code, String msg) {
        disMissLoading();
        UITipDialog.showFall(LoginActivity.this, msg);
    }

    @Override
    public void StartLogin() {
        showLoadingDialog();
    }

    @Override
    public void EndLogin() {
        disMissLoading();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.clear();
            mPresenter = null;
        }
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
