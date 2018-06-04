package com.cdkj.link_community.module.message;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.LinearLayout;

import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.IntroductionInfoModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.R;
import com.cdkj.link_community.databinding.ActivityMessageReleaseBinding;
import com.just.agentweb.AgentWeb;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by cdkj on 2018/5/1.
 */

public class MessageReleaseActivity2 extends AbsBaseLoadActivity {

    private ActivityMessageReleaseBinding mBinding;

    private AgentWeb mAgentWeb;

    /**
     * @param context
     */
    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, MessageReleaseActivity2.class);
        context.startActivity(intent);
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_message_release, null, false);

        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle("编辑资讯");
        mBaseBinding.titleView.setRightTitle("发布");
        mBaseBinding.titleView.setRightFraClickListener(view -> {
            //java调用JS方法

            mAgentWeb.getJsAccessEntrace().quickCallJs("doSubmit");
        });

        getUrlToShare();
    }

    /**
     * 获取链接并分享
     *
     */
    public void getUrlToShare() {

        Map<String, String> map = new HashMap<>();
        map.put("ckey", "h5Url");
        map.put("systemCode", MyCdConfig.SYSTEMCODE);
        map.put("companyCode", MyCdConfig.COMPANYCODE);

        Call call = RetrofitUtils.getBaseAPiService().getKeySystemInfo("628917", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IntroductionInfoModel>(this) {
            @Override
            protected void onSuccess(IntroductionInfoModel data, String SucMessage) {

                if (TextUtils.isEmpty(data.getCvalue())) {
                    return;
                }

                initWebView(data.getCvalue());

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    private void initWebView(String domain) {

        mBinding.wvRelease.setVisibility(View.GONE);

        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(mBinding.llRoot, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go(domain + "/news/addNews.html?ownerId="+ SPUtilHelper.getUserId());

        mAgentWeb.getJsInterfaceHolder().addJavaObject("js",new AndroidInterface(mAgentWeb,this));

    }

    public class AndroidInterface {


        private Handler deliver = new Handler(Looper.getMainLooper());
        private AgentWeb agent;
        private Context context;

        public AndroidInterface(AgentWeb agent, Context context) {
            this.agent = agent;
            this.context = context;
        }


        @JavascriptInterface
        public void doFinish() {
            deliver.post(new Runnable() {
                @Override
                public void run() {
                    UITipDialog.showSuccess(MessageReleaseActivity2.this, "发布成功,请等待审核", dialogInterface -> {
                        MessageReleaseActivity2.this.finish();
                    });
                }
            });

        }
    }


    @Override
    protected void onPause() {
        if (mAgentWeb != null)
            mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    protected void onResume() {
        if (mAgentWeb != null)
            mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        if (mAgentWeb != null)
            mAgentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();
    }

}
