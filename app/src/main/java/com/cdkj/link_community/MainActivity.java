package com.cdkj.link_community;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cdkj.baselibrary.adapters.ViewPagerAdapter;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.link_community.databinding.ActivityMainBinding;
import com.cdkj.link_community.manager.MyRouteHelper;
import com.cdkj.link_community.module.maintab.FirstPageFragment;
import com.cdkj.link_community.module.maintab.MarketPageFragment;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

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


    @IntDef({SHOWFIRST, SHOWINFO, SHOWINVITATION, SHOWADVICE, SHOWMY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface showType {
    }


    private ActivityMainBinding mBinding;


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
    }

    private void initListener() {

        mBinding.layoutTab.radiogroup.setOnCheckedChangeListener((radioGroup, i) -> {

            switch (i) {
                case R.id.radio_main_tab_1:
                    mBinding.pagerMain.setCurrentItem(0);
                    break;
                case R.id.radio_main_tab_2:
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
     * 初始化ViewPager
     */
    private void initViewPager() {

        //设置fragment数据
        ArrayList fragments = new ArrayList<>();

        fragments.add(FirstPageFragment.getInstanse());
        fragments.add(MarketPageFragment.getInstanse());
        fragments.add(FirstPageFragment.getInstanse());
        fragments.add(FirstPageFragment.getInstanse());

        mBinding.pagerMain.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));
        mBinding.pagerMain.setOffscreenPageLimit(fragments.size());
    }

}
