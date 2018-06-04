package com.cdkj.link_community.module.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.databinding.ActivityNicknameUpdateBinding;
import com.cdkj.link_community.model.NickNameUpdateModel;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;

/**
 * 昵称更新
 * Created by cdkj on 2018/3/22.
 */

public class NickNameUpdateActivity extends AbsBaseLoadActivity {

    private ActivityNicknameUpdateBinding mBinding;

    /**
     * @param context
     * @param nickName
     */
    public static void open(Context context, String nickName) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, NickNameUpdateActivity.class);
        intent.putExtra(CdRouteHelper.DATA_SIGN, nickName);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_nickname_update, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void topTitleViewRightClick() {
        if (TextUtils.isEmpty(mBinding.edit.getText().toString())) {
            UITipDialog.showInfo(this, getString(R.string.please_input_nick_name));
            return;
        }

        updateNickNameRequest(mBinding.edit.getText().toString());

    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        mBinding.edit.setFilters(new NameLengthFilter[]{new NameLengthFilter(16)});

        mBaseBinding.titleView.setMidTitle(getString(R.string.nick_name));

        mBaseBinding.titleView.setRightTitle(getString(R.string.sure));

        if (getIntent() != null) {
            mBinding.edit.setText(getIntent().getStringExtra(CdRouteHelper.DATA_SIGN));
        }

    }


    /**
     * 更新昵称
     *
     * @param string
     */
    private void updateNickNameRequest(String string) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("nickname", string);
        map.put("token", SPUtilHelper.getUserToken());
        Call call = RetrofitUtils.getBaseAPiService().successRequest("805084", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(NickNameUpdateActivity.this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {

                NickNameUpdateModel nickNameUpdateModel = new NickNameUpdateModel(); //通知上一页
                nickNameUpdateModel.setName(string);
                EventBus.getDefault().post(nickNameUpdateModel);

                UITipDialog.showSuccess(NickNameUpdateActivity.this, getString(R.string.update_nick_name_succ), dialogInterface -> {
                    finish();
                });
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                UITipDialog.showFail(NickNameUpdateActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    public class NameLengthFilter implements InputFilter {
        int MAX_EN;
        String regEx = "[\\u4e00-\\u9fa5]";

        public NameLengthFilter(int mAX_EN) {
            super();
            MAX_EN = mAX_EN;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            int destCount = dest.toString().length()
                    + getChineseCount(dest.toString());
            int sourceCount = source.toString().length()
                    + getChineseCount(source.toString());
            if (destCount + sourceCount > MAX_EN) {
                int surplusCount = MAX_EN - destCount;
                String result = "";
                int index = 0;
                while (surplusCount > 0) {
                    char c = source.charAt(index);
                    if (isChinest(c + "")) {
                        if (sourceCount >= 2) {
                            result += c;
                        }
                        surplusCount = surplusCount - 2;
                    } else {
                        result += c;
                        surplusCount = surplusCount - 1;
                    }
                    index++;
                }
                return result;
            } else {
                return source;
            }
        }

        private int getChineseCount(String str) {
            int count = 0;
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(str);
            while (m.find()) {
                for (int i = 0; i <= m.groupCount(); i++) {
                    count = count + 1;
                }
            }
            return count;
        }

        private boolean isChinest(String source) {
            return Pattern.matches(regEx, source);
        }
    }


}
