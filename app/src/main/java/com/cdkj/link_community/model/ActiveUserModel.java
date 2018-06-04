package com.cdkj.link_community.model;

import java.io.Serializable;

/**
 * Created by cdkj on 2018/4/29.
 */

public class ActiveUserModel implements Serializable {


    /**
     * id : 3
     * actCode : AC201805161546491343432
     * userId : UINFO201800000000000002
     * mobile : 18767101909
     * realName : 谢眼睛
     * status : 1
     * applyDatetime : May 16, 2018 9:42:17 PM
     * approver : admin
     * approveDatetime : May 17, 2018 6:39:35 PM
     */

    private String id;
    private String actCode;
    private String userId;
    private String mobile;
    private String realName;
    private String photo;
    private String status;
    private String applyDatetime;
    private String approver;
    private String approveDatetime;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

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
}
