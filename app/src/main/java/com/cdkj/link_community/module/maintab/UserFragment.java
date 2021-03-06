package com.cdkj.link_community.module.maintab;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.baselibrary.dialog.CommonDialog;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.databinding.FragmentUserBinding;
import com.cdkj.link_community.manager.DataCleanManager;
import com.cdkj.link_community.model.UserInfoModel;
import com.cdkj.link_community.module.active.MyActiveActivity;
import com.cdkj.link_community.module.user.LoginActivity;
import com.cdkj.link_community.module.user.MyMessageActivity;
import com.cdkj.link_community.module.user.MyMessageCommentsActivity;
import com.cdkj.link_community.module.user.UserCollectListActivity;
import com.cdkj.link_community.module.user.UserInfoUpdateActivity;
import com.cdkj.link_community.module.user.UserPersonActivity;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;

/**
 * 我的
 * Created by cdkj on 2018/3/22.
 */

public class UserFragment extends BaseLazyFragment {


    private FragmentUserBinding mBinding;

    private UserInfoModel mUserInfo;

    public static UserFragment getInstance() {
        UserFragment fragment = new UserFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_user, null, false);

        initListener();

        return mBinding.getRoot();
    }

    private void initListener() {

        /**
         * 用户登录
         */
        mBinding.linUserHead.setOnClickListener(view -> {
            if (!SPUtilHelper.isLogin(mActivity, false)) {
                return;
            }
            UserPersonActivity.open(mActivity, mUserInfo);
        });

        /**
         * 编辑
         */
        mBinding.tvEdit.setOnClickListener(view -> {
            if (!SPUtilHelper.isLogin(mActivity, false)) {
                return;
            }
            UserInfoUpdateActivity.open(mActivity, mUserInfo);
        });

        //收藏
        mBinding.rowCollection.setOnClickListener(view -> {
            if (!SPUtilHelper.isLogin(mActivity, false)) {
                return;
            }
            UserCollectListActivity.open(mActivity);
        });

//        /*币圈评论*/
//        mBinding.rowCommentBbs.setOnClickListener(view -> {
//            if (!SPUtilHelper.isLogin(mActivity, false)) {
//                return;
//            }
//            MyCoinBBSCommentsActivity.open(mActivity);
//        });

        mBinding.rowActive.setOnClickListener(view -> {
            if (!SPUtilHelper.isLogin(mActivity, false)) {
                return;
            }
            MyActiveActivity.open(mActivity);
        });

       /*资讯评论*/
        mBinding.rowCommentMessage.setOnClickListener(view -> {
            if (!SPUtilHelper.isLogin(mActivity, false)) {
                return;
            }
            MyMessageCommentsActivity.open(mActivity);
        });

        mBinding.rowMessage.setOnClickListener(view -> {
            if (!SPUtilHelper.isLogin(mActivity, false)) {
                return;
            }
            MyMessageActivity.open(mActivity);
        });

        mBinding.rowAbout.setOnClickListener(view -> {
            CdRouteHelper.openWebViewActivityForkey(getString(R.string.about_us), "about_us");
        });

        /*清除缓存*/
        mBinding.fraClearCache.setOnClickListener(view -> clearCache());


        /*退出登录*/
        mBinding.tvLogout.setOnClickListener(view -> logOut());

    }

    /**
     * 退出登录
     */
    private void logOut() {
        showDoubleWarnListen(getString(R.string.sure_logout), view -> {
            SPUtilHelper.logOutClear();
            setShowState();
            UITipDialog.showSuccess(mActivity, getString(R.string.logout_succ),dialogInterface -> {
                LoginActivity.open(mActivity,false);
            });
        });
    }

    /**
     * 清除缓存
     */
    private void clearCache() {
        showDoubleWarnListen(getString(R.string.sure_clear_cache), view1 -> {
            showLoadingDialog();
            mSubscription.add(Observable.just("")
                    .subscribeOn(Schedulers.io())
                    .map(new Function<String, String>() {
                        @Override
                        public String apply(String s) throws Exception {
                            DataCleanManager.clearAllCache2(mActivity);
                            return DataCleanManager.getTotalCacheSize2(mActivity);
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(s -> {
                        mBinding.tvCache.setText(s);
                        disMissLoading();
                        UITipDialog.showInfo(mActivity, getString(R.string.clear_cache_succ));
                    }, throwable -> {
                        disMissLoading();
                    }));

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            setShowState();
            if (SPUtilHelper.isLoginNoStart()) {
                getUserInfoRequest(true);
            }
        }
    }

    /**
     * 根据登录显示状态
     */
    private void setShowState() {
        if (!SPUtilHelper.isLoginNoStart()) {   //没有登录
            mBinding.tvLogout.setVisibility(View.GONE);
            mBinding.tvEdit.setVisibility(View.GONE);
            mBinding.tvUserName.setText(R.string.fast_to_login);
            mBinding.imgUserLogo.setImageResource(R.drawable.photo_default);
        } else {
            mBinding.tvLogout.setVisibility(View.VISIBLE);
            mBinding.tvEdit.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void lazyLoad() {
        if (mBinding == null) {
            return;
        }
        setShowState();
        setShowCache();
        if (SPUtilHelper.isLoginNoStart()) {
            getUserInfoRequest(false);
        }

    }

    /**
     * 设置缓存显示
     */
    private void setShowCache() {
        try {
            mBinding.tvCache.setText(DataCleanManager.getTotalCacheSize2(mActivity));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onInvisible() {

    }


    /**
     * 获取用户信息
     */
    public void getUserInfoRequest(final boolean isShowdialog) {

        if (!SPUtilHelper.isLoginNoStart()) {  //没有登录不用请求
            return;
        }

        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        Call call = RetrofitUtils.createApi(MyApiServer.class).getUserInfoDetails("805121", StringUtils.getJsonToString(map));

        addCall(call);

        if (isShowdialog) showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<UserInfoModel>(mActivity) {
            @Override
            protected void onSuccess(UserInfoModel data, String SucMessage) {
                mUserInfo = data;
                mBinding.tvUserName.setText(data.getNickname());
                ImgUtils.loadQiniuLogo(UserFragment.this, data.getPhoto(), mBinding.imgUserLogo);
                SPUtilHelper.saveUserPhoneNum(data.getMobile());
                SPUtilHelper.saveUserName(data.getRealName());
                SPUtilHelper.saveUserNickName(data.getNickname());
                SPUtilHelper.saveUserPhoto(data.getPhoto());

            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFail(mActivity, errorMessage);
            }

            @Override
            protected void onFinish() {
                if (isShowdialog) disMissLoading();
            }
        });
    }

    protected void showDoubleWarnListen(String str, CommonDialog.OnPositiveListener onPositiveListener) {

        if (mActivity == null || mActivity.isFinishing()) {
            return;
        }

        CommonDialog commonDialog = new CommonDialog(mActivity).builder()
                .setTitle("提示").setContentMsg(str)
                .setPositiveBtn("确定", onPositiveListener)
                .setNegativeBtn("取消", null, false);

        commonDialog.show();
    }

}
