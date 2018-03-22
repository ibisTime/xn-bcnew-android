package com.cdkj.link_community.module.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.cdkj.baselibrary.activitys.ImageSelectActivity;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.link_community.R;
import com.cdkj.link_community.databinding.ActivityUpdateUserInfoBinding;

import java.util.Calendar;
import java.util.Date;

/**
 * 编辑资料
 * Created by cdkj on 2018/3/22.
 */

public class UserInfoUpdateActivity extends AbsBaseLoadActivity {

    private ActivityUpdateUserInfoBinding mBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, UserInfoUpdateActivity.class);
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

        initListener();

    }

    private void initListener() {

        /*头像*/
        mBinding.layoutLogo.setOnClickListener(view -> ImageSelectActivity.launch(this, 0, false));

        /*昵称*/
        mBinding.rowNickName.setOnClickListener(view -> NickNameUpdateActivity.open(this, ""));

        /*性别*/
        mBinding.rowGender.setOnClickListener(view -> GenderChooseActivity.open(this));

        /**/

        mBinding.rowBirthday.setOnClickListener(view -> {
            Calendar selectedDate = Calendar.getInstance();
            Calendar startDate = Calendar.getInstance();
            startDate.set(1950, 0, 1);
            Calendar endDate = Calendar.getInstance();
            //时间选择器
            TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {//选中事件回调
                }
            })
                    .isCyclic(true)
                    .setType(new boolean[]{true, true, true, false, false, false})
                    .setLabel("年", "月", "日", "时", "分", "秒")
                    .setRangDate(startDate, endDate)//起始终止年月日设定
                    .build();
            pvTime.setDate(selectedDate);//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
            pvTime.show();
        });


    }


}
