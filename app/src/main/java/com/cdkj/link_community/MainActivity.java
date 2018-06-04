package com.cdkj.link_community;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cdkj.baselibrary.adapters.ViewPagerAdapter;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.CommonDialog;
import com.cdkj.baselibrary.model.eventmodels.EventFinishAll;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.databinding.ActivityMainBinding;
import com.cdkj.link_community.manager.MyRouteHelper;
import com.cdkj.link_community.model.FastMessage;
import com.cdkj.link_community.model.MarketInterval;
import com.cdkj.link_community.model.TabCurrentModel;
import com.cdkj.link_community.model.VersionModel;
import com.cdkj.link_community.model.event.EventMarketIntervalPause;
import com.cdkj.link_community.module.maintab.ActiveFragment;
import com.cdkj.link_community.module.maintab.CoinBBSFragment;
import com.cdkj.link_community.module.maintab.FirstPageFragment;
import com.cdkj.link_community.module.maintab.MarketPageFragment;
import com.cdkj.link_community.module.maintab.UserFragment;
import com.cdkj.link_community.module.message.FastMessageToShareActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import retrofit2.Call;

import static com.cdkj.baselibrary.utils.AppUtils.startWeb;

/**
 * 我的
 */
@Route(path = MyRouteHelper.APPMAIN)
public class MainActivity extends AbsBaseLoadActivity {


    public static final int SHOWFIRST = 0;//显示首页
    public static final int SHOWINFO = 1;//显示行情
    public static final int SHOWINVITATION = 2;//显示贴吧
    public static final int SHOWADVICE = 3;//显示我的
    public static final int SHOWMY = 4;//显示我的界面

    private boolean isMarketInterval;//行情是否开始轮询

    private boolean isSume;//页面是否显示 用于轮询停止

    @IntDef({SHOWFIRST, SHOWINFO, SHOWINVITATION, SHOWADVICE, SHOWMY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface showType {
    }


    private ActivityMainBinding mBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }


    /**
     * 不加载标题
     *
     * @return
     */
    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_main, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        initViewPager();
        initListener();
        getVersion();

        initPush();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isSume = true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        isSume = false;
    }

    private void initPush() {
        if (getIntent() == null) {
            return;
        }

        if (getIntent().getStringExtra("extraValue") == null
                || getIntent().getStringExtra("extraValue").equals("")) {
            return;
        }

        getFastMessage(getIntent().getStringExtra("extraValue"));
    }

    private void getFastMessage(String code) {

        if (TextUtils.equals(code,"notOpen"))
            return;

        Map<String, String> map = new HashMap<>();

        map.put("companyCode", MyCdConfig.COMPANYCODE);
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("code", code);

        Call call = RetrofitUtils.createApi(MyApiServer.class).getFastMsg("628096", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<FastMessage>(this) {

            @Override
            protected void onSuccess(FastMessage data, String SucMessage) {

                int limit = 200;
                String content;
                if (data.getContent().length() > limit) { // 如果快讯内容长度大于limit，截取并加省略号
                    content = data.getContent().substring(0, limit) + "......";
                }else {
                    content = data.getContent();
                }

                //通知FastMessageFragment 将tab切换到热门
                TabCurrentModel tabCurrentModel = new TabCurrentModel();
                tabCurrentModel.setCurrent(1);
                EventBus.getDefault().post(tabCurrentModel);

                showPushListen("推送信息", content, view -> {
                    FastMessageToShareActivity.open(MainActivity.this, data);
                });

            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                super.onReqFailure(errorCode, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    private void initListener() {


        mBinding.layoutTab.radiogroup.setOnCheckedChangeListener((radioGroup, i) -> {

            switch (i) {
                case R.id.radio_main_tab_1:
                    mBinding.pagerMain.setCurrentItem(0);
                    break;
                case R.id.radio_main_tab_2:
                    if (!isMarketInterval) {
                        mSubscription.add(startMarketInterval());
                    }
                    mBinding.pagerMain.setCurrentItem(1);
                    break;
                case R.id.radio_main_tab_3:
                    mBinding.pagerMain.setCurrentItem(2);
                    break;
                case R.id.radio_main_tab_4:
                    mBinding.pagerMain.setCurrentItem(3);
                    break;
                case R.id.radio_main_tab_5:
                    mBinding.pagerMain.setCurrentItem(4);
                    break;
                default:
            }

        });
    }


    /**
     * 开始行情轮询
     *
     * @return
     */
    public Disposable startMarketInterval() {
        isMarketInterval = true;
        return Observable.interval(10, 10, TimeUnit.SECONDS, AndroidSchedulers.mainThread())    //
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                               @Override
                               public void accept(Long aLong) throws Exception {
                                   if (isSume) {
                                       EventBus.getDefault().post(new MarketInterval());
                                   }

                               }
                           }, throwable -> {

                        }
                );
    }

    @Subscribe
    public void marketToStopInterval(EventMarketIntervalPause pause){
        if (isSume){
            isSume = false;
            mSubscription.add(stopMarketInterval());

        }else {

        }

    }

    /**
     * 停止行情轮询： 20S
     *
     * @return
     */
    public Disposable stopMarketInterval() {

        return Observable.interval(0, 1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())    //
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .take(20)
                .subscribe(aLong -> {
                }, throwable -> isSume = true);
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {

        //设置fragment数据
        ArrayList fragments = new ArrayList<>();

        fragments.add(FirstPageFragment.getInstance());
        fragments.add(MarketPageFragment.getInstance());
        fragments.add(CoinBBSFragment.getInstance());
        fragments.add(ActiveFragment.getInstance());
        fragments.add(UserFragment.getInstance());

        mBinding.pagerMain.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));
        mBinding.pagerMain.setOffscreenPageLimit(fragments.size());
    }

    /**
     * 获取最新版本
     *
     * @return
     */
    private void getVersion() {
        Map<String, String> map = new HashMap<>();
        map.put("type", "android-c");
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("companyCode", MyCdConfig.COMPANYCODE);

        Call call = RetrofitUtils.createApi(MyApiServer.class).getVersion("628918", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<VersionModel>(this) {

            @Override
            protected void onSuccess(VersionModel data, String SucMessage) {
                if (data == null)
                    return;

                if (!TextUtils.equals(data.getVersion(), AppUtils.getAppVersionName(MainActivity.this))) {  //版本号不一致说明有更新
                    showUploadDialog(data);
                }
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    /**
     * 显示更新dialog
     *
     * @param versionModel
     */
    private void showUploadDialog(VersionModel versionModel) {

        if (isFinishing()) {
            return;
        }

        if (TextUtils.equals("1", versionModel.getForceUpdate())) { // 强制更新
            showSureDialog(versionModel.getNote(), view -> {
                startWeb(MainActivity.this, versionModel.getDownloadUrl());
                EventBus.getDefault().post(new EventFinishAll());
                finish();
            });
        } else {
            showDoubleWarnListen(versionModel.getNote(), view -> {
                startWeb(MainActivity.this, versionModel.getDownloadUrl());
            });
        }
    }

    protected void showSureDialog(String str, CommonDialog.OnPositiveListener onPositiveListener) {

        if (isFinishing()) {
            return;
        }
        CommonDialog commonDialog = new CommonDialog(this).builder()
                .setTitle(getString(R.string.update_tip)
                ).setContentMsg(str)
                .setPositiveBtn(getString(R.string.update_now), onPositiveListener);

        commonDialog.show();
    }

    protected void showDoubleWarnListen(String str, CommonDialog.OnPositiveListener onPositiveListener) {

        if (isFinishing()) {
            return;
        }

        CommonDialog commonDialog = new CommonDialog(this).builder()
                .setTitle(getString(R.string.update_tip)).setContentMsg(str)
                .setPositiveBtn(getString(R.string.update_now), onPositiveListener)
                .setNegativeBtn(getString(com.cdkj.baselibrary.R.string.cancel), null, false);

        commonDialog.show();
    }

}
