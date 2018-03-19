package com.cdkj.baselibrary.nets;

import android.content.Context;

import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelpr;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.ToastUtil;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;


/**
 * Created by cdkj on 2017/10/15.
 */

public class NetHelper {


    /*0=成功；1=权限错误；2=参数错误；3=业务错误；9=未知错误*/

    public static final String REQUESTOK = "0";   //请求后台成功

    public static final String REQUESTFECODE3 = "3";
    public static final String REQUESTFECODE2 = "2";

    public static final String REQUESTFECODE4 = "4";//重新登录

    public static final String REQUESTFECODE9 = "9";

    public static final String NET_ERROR = "-1";


    /**
     * 网络异常状态错误码
     */
    public static final String NETERRORCODE0 = "10";  //请求成功，但是服务器返回除1000外错误码
    public static final String NETERRORCODE1 = "11";  //网络异常
    public static final String NETERRORCODE2 = "12";  //响应超时
    public static final String NETERRORCODE3 = "13";  //连接超时
    public static final String NETERRORCODE4 = "14";  //其它错误


    public static String getThrowableStateString(Throwable t) {
        String errorString;
        if (t instanceof UnknownHostException) { // 网络错误
            errorString = "网络加载异常";
        } else if (t instanceof SocketTimeoutException) {//响应超时
            errorString = "服务器响应超时";
        } else if (t instanceof ConnectException) {//请求超时
            errorString = "网络请求超时";
        } else if (t instanceof HttpException) {
            errorString = "网络异常";
        } else {
            errorString = "未知错误";
        }

        if (LogUtil.isDeBug) {
            errorString += t.toString();
        }
        return errorString;
    }

    public static String getThrowableStateCode(Throwable t) {
        String errorCode;
        if (t instanceof UnknownHostException) { // 网络错误
            errorCode = NETERRORCODE1;
        } else if (t instanceof SocketTimeoutException) {//响应超时
            errorCode = NETERRORCODE2;
        } else if (t instanceof ConnectException) {//请求超时
            errorCode = NETERRORCODE3;
        } else if (t instanceof HttpException) {
            errorCode = NETERRORCODE1;
        } else {
            errorCode = NETERRORCODE4;
        }
        return errorCode;
    }


    /**
     * 获取数据为空
     *
     * @param context
     */
    public static void onNull(Context context) {
        if(context==null)return;
        ToastUtil.show(context, "请求失败,数据返回错误");
    }

    /**
     * 暂无网络
     *
     * @param context
     * @param msg
     */
    public static void onNoNet(Context context, String msg) {
        if(context==null)return;
        ToastUtil.show(context, msg);
    }

    /**
     * 请求失败
     *
     * @param context
     * @param errorCode
     * @param msg
     */
    public static void onReqFailure(Context context, String errorCode, String msg) {
        if(context==null)return;
        ToastUtil.show(context, msg);
        LogUtil.E("网络请求错误————————：" + msg);

    }

    /**
     * 重新登录
     *
     * @param context
     * @param errorMessage
     */
    public static void onLoginFailure(Context context, String errorMessage) {
        SPUtilHelpr.logOutClear();
        if(context!=null){
            ToastUtil.show(context, errorMessage);
        }
        // 路由跳转登录页面
        CdRouteHelper.openLogin(false);
    }


}
