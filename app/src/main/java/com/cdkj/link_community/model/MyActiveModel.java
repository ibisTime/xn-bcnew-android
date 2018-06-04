package com.cdkj.link_community.model;

import java.io.Serializable;

/**
 * Created by cdkj on 2018/4/29.
 */

public class MyActiveModel implements Serializable {


    /**
     * id : 3
     * actCode : AC201804301433538225317
     * userId : U201804301623334699199
     * mobile : 15761663457
     * realName : 胡莎莎
     * status : 1
     * applyDatetime : Apr 30, 2018 4:23:46 PM
     * approver : admin
     * approveDatetime : Apr 30, 2018 4:24:27 PM
     * approveNote : 1
     * activity : {"code":"AC201804301433538225317","title":"西溪印象城","advPic":"B11F6A08-0763-4861-A754-0B4279C126CB_1525070051582.jpg","content":"<p>西溪印象城，<\/p><p>西溪印象城<\/p><p>西溪印象城<\/p><p><br><\/p>","address":"西溪印象城","longitude":"120.052005","latitude":"30.247732","startDatetime":"May 1, 2018 2:34:20 PM","endDatetime":"May 2, 2018 12:00:00 AM","price":100000,"maxCount":10,"status":"1","contactMobile":"15761663457","applyType":"1","applyUser":"UINFO201800000000000001","applyDatetime":"Apr 30, 2018 2:33:53 PM","approveDatetime":"Apr 30, 2018 3:04:14 PM","approveNote":"通过","readCount":0,"pointCount":0,"commentCount":0,"collectCount":1,"toApproveCount":0,"approveCount":0}
     */

    private String id;
    private String actCode;
    private String userId;
    private String mobile;
    private String realName;
    private String status;
    private String applyDatetime;
    private String approver;
    private String approveDatetime;
    private String approveNote;
    private ActiveModel activity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActCode() {
        return actCode;
    }

    public void setActCode(String actCode) {
        this.actCode = actCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApplyDatetime() {
        return applyDatetime;
    }

    public void setApplyDatetime(String applyDatetime) {
        this.applyDatetime = applyDatetime;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getApproveDatetime() {
        return approveDatetime;
    }

    public void setApproveDatetime(String approveDatetime) {
        this.approveDatetime = approveDatetime;
    }

    public String getApproveNote() {
        return approveNote;
    }

    public void setApproveNote(String approveNote) {
        this.approveNote = approveNote;
    }

    public ActiveModel getActivity() {
        return activity;
    }

    public void setActivity(ActiveModel activity) {
        this.activity = activity;
    }

}
