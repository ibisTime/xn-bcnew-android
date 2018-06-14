package com.cdkj.link_community.module.market;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.MyCdConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.interfaces.RefreshHelper;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.MoneyUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.link_community.R;
import com.cdkj.link_community.adapters.UserWarnAdapter;
import com.cdkj.link_community.api.MyApiServer;
import com.cdkj.link_community.databinding.ActivityMarketWarn2Binding;
import com.cdkj.link_community.databinding.ActivityMarketWarnBinding;
import com.cdkj.link_community.model.CoinListModel;
import com.cdkj.link_community.model.UserWarnModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.link_community.utils.AccountUtil.scale;

/**
 * Created by cdkj on 2018/5/7.
 */

public class MarketWarnActivity extends AbsBaseLoadActivity {

    private ActivityMarketWarn2Binding mBinding;

    private RefreshHelper mRefreshHelper;

    private CoinListModel model;

    private String warnCurrency = "USD";

    /**
     * @param context
     */
    public static void open(Context context, CoinListModel model) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, MarketWarnActivity.class);
        intent.putExtra("model", model);
        context.startActivity(intent);
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_market_warn2, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        init();
        initListener();
    }

    private void init() {
        mBaseBinding.titleView.setMidTitle("预警");
        mBaseBinding.viewV.setVisibility(View.GONE);

        if (getIntent() == null)
            return;

        model = (CoinListModel) getIntent().getSerializableExtra("model");

        setView();
    }

    private void setView() {
        mBinding.tvExchange.setText(model.getExchangeCname());
        mBinding.tvSymbol.setText(model.getSymbol());

        if (model.getLastCnyPrice() != null)
            mBinding.tvPriceCny.setText(MoneyUtils.MONEYSING + scale(model.getLastCnyPrice(), 2));

        if (model.getLastUsdPrice() != null)
            mBinding.tvPriceUsd.setText(MoneyUtils.MONEYSING_USD + scale(model.getLastUsdPrice(), 2));

        intEditTextView(mBinding.edtUp);
        intEditTextView(mBinding.edtDown);

        initRefreshHelper();
        mRefreshHelper.onDefaluteMRefresh(true);
    }

    private void initListener() {

        mBinding.radiogroup.setOnCheckedChangeListener((radioGroup, i) -> {

            switch (i) {
                case R.id.radio_usd:
                    warnCurrency = "USD";

                    mBinding.edtUp.setText("");
                    mBinding.edtDown.setText("");

                    mBinding.tvUnitUp.setText(MoneyUtils.MONEYSING_USD);
                    mBinding.tvUnitDown.setText(MoneyUtils.MONEYSING_USD);
                    break;

                case R.id.radio_cny:
                    warnCurrency = "CNY";

                    mBinding.edtUp.setText("");
                    mBinding.edtDown.setText("");

                    mBinding.tvUnitUp.setText(MoneyUtils.MONEYSING);
                    mBinding.tvUnitDown.setText(MoneyUtils.MONEYSING);
                    break;
            }

        });

        mBinding.ivUp.setOnClickListener(view -> {
            if (TextUtils.isEmpty(mBinding.edtUp.getText().toString())) {
                ToastUtil.show(this, "请输入上涨预警价格");
                return;
            }

            if (check("up")) {
                setWarn(mBinding.edtUp.getText().toString(), "1");
            }

        });

        mBinding.ivDown.setOnClickListener(view -> {
            if (TextUtils.isEmpty(mBinding.edtDown.getText().toString())) {
                ToastUtil.show(this, "请输入下跌预警价格");
                return;
            }

            if (check("down")) {
                setWarn(mBinding.edtDown.getText().toString(), "0");
            }

        });

        mBinding.edtDown.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                //判断是否有内容  有内容  黄色  没有内容灰色

                if (TextUtils.isEmpty(mBinding.edtDown.getText().toString())) {
                    //没有内容
                    mBinding.tvUnitDown.setTextColor(getResources().getColor(R.color.bg_gray));
                    mBinding.ivDown.setImageResource(R.drawable.market_warn_add_gray);

                } else {
//                    有内容
                    mBinding.ivDown.setImageResource(R.drawable.market_warn_add);
                    mBinding.tvUnitDown.setTextColor(getResources().getColor(R.color.text_black_cd));
                }

            }
        });

        mBinding.edtUp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //判断是否有内容  有内容  黄色  没有内容灰色

                if (TextUtils.isEmpty(mBinding.edtUp.getText().toString())) {
                    //没有内容
                    mBinding.tvUnitUp.setTextColor(getResources().getColor(R.color.bg_gray));
                    mBinding.ivUp.setImageResource(R.drawable.market_warn_add_gray);
                } else {
//                    有内容
                    mBinding.ivUp.setImageResource(R.drawable.market_warn_add);
                    mBinding.tvUnitUp.setTextColor(getResources().getColor(R.color.text_black_cd));

                }
            }
        });
    }

    private void intEditTextView(EditText edt) {
        edt.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        //设置字符过滤
        edt.setFilters(new InputFilter[]{(source, start, end, dest, dstart, dend) -> {
            if (source.equals(".") && dest.toString().length() == 0) {
                return "0.";
            }
            if (dest.toString().contains(".")) {
                int index = dest.toString().indexOf(".");
                int mlength = dest.toString().substring(index).length();
                if (mlength == 3) {
                    return "";
                }
            }
            return null;
        }});
    }

    /**
     * 初始化刷新相关
     */
    protected void initRefreshHelper() {

        mRefreshHelper = new RefreshHelper(this, new BaseRefreshCallBack(this) {
            @Override
            public View getRefreshLayout() {
                return mBinding.refreshLayout;
            }

            @Override
            public RecyclerView getRecyclerView() {

                return mBinding.rv;
            }

            @Override
            public RecyclerView.Adapter getAdapter(List listData) {

                UserWarnAdapter userWarnAdapter = new UserWarnAdapter(listData);
                userWarnAdapter.setOnItemChildClickListener((adapter, view, position) -> {

                    showDoubleWarnListen("您确定要删除该条预警提醒吗？", view1 -> {
                        UserWarnModel userWarnModel = userWarnAdapter.getItem(position);
                        delete(userWarnModel.getId());
                    });

                });

                return userWarnAdapter;
            }

            @Override
            public void getListDataRequest(int pageindex, int limit, boolean isShowDialog) {
                getListRequest(pageindex, limit, isShowDialog);
            }

            @Override
            public void reLoad() {
                mRefreshHelper.onDefaluteMRefresh(true);
            }
        });

        mRefreshHelper.init(MyCdConfig.LISTLIMIT);

    }

    public void getListRequest(int pageIndex, int limit, boolean isShowDialog) {
        Map<String, String> map = new HashMap<>();

        map.put("toSymbol", model.getToSymbol());
        map.put("symbol", model.getSymbol());
        map.put("userId", SPUtilHelper.getUserId());
        map.put("status", "0");
        map.put("start", pageIndex + "");
        map.put("limit", limit + "");

        if (isShowDialog) showLoadingDialog();

        Call call = RetrofitUtils.createApi(MyApiServer.class).getUserWarnList("628395", StringUtils.getJsonToString(map));
        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<UserWarnModel>>(this) {
            @Override
            protected void onSuccess(ResponseInListModel<UserWarnModel> data, String SucMessage) {
                mRefreshHelper.setData(data.getList(), "暂无预警", R.drawable.no_dynamic);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    private boolean check(String type) {
        double current;
        if (TextUtils.equals(warnCurrency, "CNY")) {
            current = Double.parseDouble(model.getLastCnyPrice());
        } else {
            current = Double.parseDouble(model.getLastUsdPrice());
        }

        if (TextUtils.equals(type, "up")) {

            if (current >= Double.parseDouble(mBinding.edtUp.getText().toString())) {
                ToastUtil.show(this, "上涨预警价格必须大于限价");
                return false;
            }

        } else {

            if (current <= Double.parseDouble(mBinding.edtDown.getText().toString())) {
                ToastUtil.show(this, "下跌预警价格必须小于限价");
                return false;
            }

        }

        return true;

    }

    private void setWarn(String warnPrice, String warnDirection) {

        Map<String, String> map = new HashMap<>();
        map.put("userId", SPUtilHelper.getUserId());
        map.put("exchangeEname", model.getExchangeEname());
        map.put("toSymbol", model.getToSymbol());
        map.put("symbol", model.getSymbol());
        map.put("warnCurrency", warnCurrency);
        map.put("warnPrice", warnPrice);
        map.put("warnContent", "");
        map.put("warnDirection", warnDirection);

        Call call = RetrofitUtils.getBaseAPiService().successRequest("628390", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {

            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {
                    UITipDialog.showSuccess(MarketWarnActivity.this, getString(R.string.do_succ), dialogInterface -> {
                        mRefreshHelper.onDefaluteMRefresh(true);
                    });
                } else {
                    UITipDialog.showFail(MarketWarnActivity.this, getString(R.string.do_fall));
                }
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    private void delete(String id) {

        Map<String, String> map = new HashMap<>();

        map.put("id", id);

        showLoadingDialog();

        Call call = RetrofitUtils.getBaseAPiService().successRequest("628391", StringUtils.getJsonToString(map));
        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {
                    UITipDialog.showSuccess(MarketWarnActivity.this, getString(R.string.do_succ), dialogInterface -> {
                        mRefreshHelper.onDefaluteMRefresh(true);
                    });
                } else {
                    UITipDialog.showFail(MarketWarnActivity.this, getString(R.string.do_fall));
                }

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

}
