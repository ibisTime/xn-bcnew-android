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
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.databinding.ActivityMainBinding;
import com.cdkj.link_community.manager.MyRouteHelper;
import com.cdkj.link_community.model.MarketInterval;
import com.cdkj.link_community.model.VersionModel;
import com.cdkj.link_community.module.maintab.CoinBBSFragment;
import com.cdkj.link_community.module.maintab.FirstPageFragment;
import com.cdkj.link_community.module.maintab.MarketPageFragment;
import com.cdkj.link_community.module.maintab.UserFragment;

import org.greenrobot.eventbus.EventBus;

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
        return Observable.interval(30, 30, TimeUnit.SECONDS, AndroidSchedulers.mainThread())    // 创建一个按照给定的时间间隔发射从0开始的整数序列
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                               @Override
                               public void accept(Long aLong) throws Exception {
                                   LogUtil.E("a" + aLong);
                                   EventBus.getDefault().post(new MarketInterval());

                               }
                           }, throwable -> {

                        }
                );
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {

        //设置fragment数据
        ArrayList fragments = new ArrayList<>();

        fragments.add(FirstPageFragment.getInstanse());
        fragments.add(MarketPageFragment.getInstanse());
        fragments.add(CoinBBSFragment.getInstanse());
        fragments.add(UserFragment.getInstanse());

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
