package com.cdkj.link_community.utils;

import android.support.v4.content.ContextCompat;

import com.cdkj.baselibrary.utils.BigDecimalUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.link_community.BaseApplication;
import com.cdkj.link_community.R;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by cdkj on 2018/4/29.
 */

public class AccountUtil {

    public static String MONEY_SIGN = "¥";
    public static String MONEY_SIGN_USD = "$";

    public static BigDecimal UNIT_MILLION = new BigDecimal("1000000");
    public static BigDecimal UNIT_BILLION = new BigDecimal("1000000000");
    public static BigDecimal UNIT_TRILLION = new BigDecimal("1000000000000");

    public static String moneyFormat(double money) {
        DecimalFormat df = new DecimalFormat("#######0.000");
        String showMoney = df.format((money / 1000));

        return showMoney.substring(0, showMoney.length() - 1);
    }


    public static String formatPercent(double money) {
        DecimalFormat df = new DecimalFormat("#######0.000");
        String showMoney = df.format(money);

        return showMoney.substring(0, showMoney.length() - 1);
    }

    public static String marketDataFormat(BigDecimal decimal) {


        if (decimal == null)
            return "";

        if (decimal.compareTo(UNIT_TRILLION) == 0 || decimal.compareTo(UNIT_TRILLION) == 1) {
            // 大于等于万亿
            return scale(decimal.divide(UNIT_TRILLION) + "") + "t";
        } else {

            // 小于万亿
            if (decimal.compareTo(UNIT_BILLION) == 0 || decimal.compareTo(UNIT_BILLION) == 1) {
                // 大于等于十亿
                return scale(decimal.divide(UNIT_BILLION) + "") + "m";
            } else {

                // 小于十亿
                if (decimal.compareTo(UNIT_MILLION) == 0 || decimal.compareTo(UNIT_MILLION) == 1) { // 大于等于十亿
                    // 大于等于百万
                    return scale(decimal.divide(UNIT_MILLION) + "") + "b";
                } else {
                    return scale(decimal + "");
                }

            }
        }

    }

    /**
     * 格式化输出的金额格式，去掉小数点
     *
     * @param s
     * @return
     */
    public static String scale(String s) {
        if (s == null) {
            return "NULL";
        }

        String amount[] = s.split("\\.");

        if (amount[0] == null)
            return "?";

        return amount[0];
    }

    /**
     * 格式化输出的金额格式，最多8位小数
     *
     * @param s
     * @return
     */
    public static String scale(String s, int scale) {
        String amount[] = s.split("\\.");
        if (amount.length > 1) {
            if (amount[1].length() > scale) {
                return amount[0] + "." + amount[1].substring(0, scale);
            } else {
                return amount[0] + "." + amount[1];
            }
        } else {
            return amount[0];
        }
    }


    /**
     * 获取行情显示颜色
     *
     * @param changeValue
     * @return
     */
    public static int getShowColor(BigDecimal changeValue) {
        if (BigDecimalUtils.compareToZERO(changeValue)) {
            return ContextCompat.getColor(BaseApplication.getInstance(), R.color.market_green);
        }

        return ContextCompat.getColor(BaseApplication.getInstance(), R.color.market_red);
    }

    /**
     * 获取行情显示颜色
     *
     * @param changeValue
     * @return
     */
    public static int getShowBtnBg(BigDecimal changeValue) {
        if (BigDecimalUtils.compareToZERO(changeValue)) {
            return R.drawable.market_green_bg;
        }

        return R.drawable.market_red_bg;
    }

    /**
     * 获取行情显示格式
     *
     * @param bigDecimal
     * @return
     */
    public static String getShowString(BigDecimal bigDecimal) {

        if (bigDecimal.compareTo(BigDecimal.ZERO) == -1) {

            return StringUtils.formatNum2(bigDecimal) + "%";

        } else if (bigDecimal.compareTo(BigDecimal.ZERO) == 0) {

            return StringUtils.formatNum2(bigDecimal) + "%";

        } else if (bigDecimal.compareTo(BigDecimal.ZERO) == 1) {
            return "+" + StringUtils.formatNum2(bigDecimal) + "%";
        }

        return "";
    }

}
