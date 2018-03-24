package com.cdkj.link_community.dialog;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.cdkj.baselibrary.utils.DisplayHelper;
import com.cdkj.link_community.R;
import com.cdkj.link_community.databinding.DialogCommentInputBinding;

/**
 * Created by 李先俊 on 2018/3/20.
 */

public class CommentInputDialog extends Dialog {

    private DialogCommentInputBinding mBinding;

    private sureClickListener mSureListener;

    public void setmSureListener(sureClickListener mSureListener) {
        this.mSureListener = mSureListener;
    }

    public CommentInputDialog(@NonNull Context context, String name) {
        super(context, R.style.comment_input_dialog);
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_comment_input, null, false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(mBinding.getRoot());
        if (!TextUtils.isEmpty(name)) {
            mBinding.editComment.setHint("对" + name + "进行回复");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int screenWidth = DisplayHelper.getScreenWidth(getContext());
        WindowManager.LayoutParams lp = getWindow().getAttributes();

        lp.width = (int) (screenWidth); //设置宽度
        getWindow().setAttributes(lp);
        setCancelable(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        getWindow().setGravity(Gravity.BOTTOM);

        mBinding.tvCancel.setOnClickListener(view -> dismiss());

        mBinding.tvRelease.setOnClickListener(view -> {
            if (mSureListener != null) {
                dismiss();
                mSureListener.sure(mBinding.editComment.getText().toString());
            }
        });

    }


    public interface sureClickListener {
        void sure(String comment);
    }

}
