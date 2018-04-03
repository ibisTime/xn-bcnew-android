package com.cdkj.link_community.module.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.bigkoo.pickerview.TimePickerView;
import com.cdkj.baselibrary.activitys.ImageSelectActivity;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.CameraHelper;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.QiNiuHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.databinding.ActivityUpdateUserInfoBinding;
import com.cdkj.link_community.model.GenderUpdateModel;
import com.cdkj.link_community.model.NickNameUpdateModel;
import com.cdkj.link_community.model.UserInfoModel;

import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.baselibrary.utils.DateUtil.DATE_YMD;

/**
 * 编辑资料
 * Created by cdkj on 2018/3/22.
 */

public class UserInfoUpdateActivity extends AbsBaseLoadActivity {

    private ActivityUpdateUserInfoBinding mBinding;

    private int PHOTOFLAG = 111;
    private UserInfoModel mUserInfo;

    /**
     * @param context
     * @param userInfoModel
     */
    public static void open(Context context, UserInfoModel userInfoModel) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, UserInfoUpdateActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, userInfoModel);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_update_user_info, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle(getString(R.string.edit_user_info));

        if (getIntent() != null) {
            mUserInfo = getIntent().getParcelableExtra(CdRouteHelper.DATASIGN);
        }

        setShowData();

        initListener();

    }

    /**
     * 设置显示数据
     */
    private void setShowData() {
        if (mUserInfo == null) {
            return;
        }

        ImgUtils.loadQiniuLogo(UserInfoUpdateActivity.this, mUserInfo.getPhoto(), mBinding.imgLogo);
        mBinding.rowNickName.setTvRight(mUserInfo.getNickname());
        mBinding.rowGender.setTvRight(getGenderStr(mUserInfo.getGender()));
        mBinding.rowBirthday.setTvRight(mUserInfo.getBirthday());

    }

    private void initListener() {

        /*头像*/
        mBinding.layoutLogo.setOnClickListener(view -> ImageSelectActivity.launch(this, PHOTOFLAG, false));

        /*昵称*/
        mBinding.rowNickName.setOnClickListener(view -> {

            if (mUserInfo != null) {
                NickNameUpdateActivity.open(this, mUserInfo.getNickname());
                return;
            }
            NickNameUpdateActivity.open(this, "");

        });

        /*性别*/
        mBinding.rowGender.setOnClickListener(view -> GenderChooseActivity.open(this));

        /*生日*/
        mBinding.rowBirthday.setOnClickListener(view -> {
            showTimePickerView();
        });


    }

    /**
     * 显示日期
     */
    private void showTimePickerView() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(1950, 0, 1);
        Calendar endDate = Calendar.getInstance();
        //时间选择器
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                updateBirthdayRequest(DateUtil.format(date, DATE_YMD));
            }
        })
                .isCyclic(true)
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .build();
        pvTime.setDate(selectedDate);//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
        pvTime.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        if (requestCode == PHOTOFLAG) {
            String path = data.getStringExtra(CameraHelper.staticPath);
            LogUtil.E("拍照获取路径" + path);
            showLoadingDialog();
            new QiNiuHelper(this).uploadSinglePic(new QiNiuHelper.QiNiuCallBack() {
                @Override
                public void onSuccess(String key) {
                    updateUserPhoto(key);
                }

                @Override
                public void onFal(String info) {
                    disMissLoading();
                }
            }, path);

        }
    }

    /**
     * 更新头像
     *
     * @param key
     */
    private void updateUserPhoto(final String key) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", SPUtilHelpr.getUserId());
        map.put("photo", key);
        map.put("token", SPUtilHelpr.getUserToken());
        Call call = RetrofitUtils.getBaseAPiService().successRequest("805080", StringUtils.getJsonToString(map));
        addCall(call);
        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(UserInfoUpdateActivity.this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                UITipDialog.showSuccess(UserInfoUpdateActivity.this, getString(R.string.update_logo_succ));
                ImgUtils.loadQiniuLogo(UserInfoUpdateActivity.this, key, mBinding.imgLogo);
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFail(UserInfoUpdateActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }


    /**
     * 更新性别
     *
     * @param gender
     */
    private void updateGenderRequest(String gender) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("gender", gender);
        map.put("token", SPUtilHelpr.getUserToken());
        Call call = RetrofitUtils.getBaseAPiService().successRequest("805085", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(UserInfoUpdateActivity.this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                UITipDialog.showSuccess(UserInfoUpdateActivity.this, getString(R.string.gender_update_succ));
                mBinding.rowGender.setTvRight(getGenderStr(gender));
                if (mUserInfo != null) {
                    mUserInfo.setGender(gender);
                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFail(UserInfoUpdateActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }


    /**
     * 更新生日
     *
     * @param birthday
     */
    private void updateBirthdayRequest(String birthday) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("birthday", birthday);
        map.put("token", SPUtilHelpr.getUserToken());
        Call call = RetrofitUtils.getBaseAPiService().successRequest("805086", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(UserInfoUpdateActivity.this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                UITipDialog.showSuccess(UserInfoUpdateActivity.this, getString(R.string.birthday_update_succ));
                mBinding.rowBirthday.setTvRight(birthday);
                if (mUserInfo != null) {
                    mUserInfo.setBirthday(birthday);
                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFail(UserInfoUpdateActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    /**
     * 获取性别
     *
     * @param gender
     * @return
     */
    @NonNull
    private String getGenderStr(String gender) {
        if (TextUtils.isEmpty(gender)) {
            return "";
        }
        return TextUtils.equals(GenderChooseActivity.MAN, gender) ? getString(R.string.man) : getString(R.string.woman);
    }

    /**
     * 昵称更新
     *
     * @param nickNameUpdateModel
     */
    @Subscribe
    public void nickUpdateSucc(NickNameUpdateModel nickNameUpdateModel) {
        if (nickNameUpdateModel == null) return;
        mBinding.rowNickName.setTvRight(nickNameUpdateModel.getName());
        if (mUserInfo != null) {
            mUserInfo.setNickname(nickNameUpdateModel.getName());
        }
    }

    /**
     * 性别更新
     *
     * @param genderUpdateModel
     */
    @Subscribe
    public void genderUpdateSucc(GenderUpdateModel genderUpdateModel) {
        if (genderUpdateModel == null) return;
        updateGenderRequest(genderUpdateModel.getGender());
    }


}
