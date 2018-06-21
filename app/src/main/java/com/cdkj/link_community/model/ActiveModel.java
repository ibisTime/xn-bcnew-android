package com.cdkj.link_community.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cdkj on 2018/4/29.
 */

public class ActiveModel implements Serializable {

    /**
     * code : AC201804291452173446315
     * title : 活动第一次
     * advPic : www.baidu.com
     * content : 出去玩咯
     * address : 杭州大西西
     * longitude : 100
     * latitude : 100
     * startDatetime : May 20, 2018 12:00:00 AM
     * endDatetime : May 21, 2018 12:00:00 AM
     * price : 0
     * maxCount : 10
     * status : 0
     * contactMobile : 18767101909
     * applyType : 1
     * applyUser : admin
     * applyDatetime : Apr 29, 2018 2:52:17 PM
     * readCount : 0
     * pointCount : 0
     * commentCount : 0
     * collectCount : 0
     * isEnroll : 0
     * toApproveCount : 0
     * approveCount : 0
     * approvedList : []
     */

    private String code;
    private String title;
    private String advPic;
    private String content;
    private String address;
    private String meetAddress;
    private String longitude;
    private String latitude;
    private String startDatetime;
    private String endDatetime;
    private String price;
    private int maxCount;
    private String isTop;
    private String status;
    private String contactMobile;
    private String applyType;
    private String applyUser;
    private String applyDatetime;
    private int readCount;
    private int pointCount;
    private int commentCount;
    private int collectCount;
    private String isEnroll;
    private String isCollect;
    private int enrollCount; // 总报名人数（已经报名人数）
    private int toApproveCount; // 已报名待审核;
    private int approveCount; // 审核通过数量
    private List<ApprovedBean> approvedList;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAdvPic() {
        return advPic;
    }

    public void setAdvPic(String advPic) {
        this.advPic = advPic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMeetAddress() {
        return meetAddress;
    }

    public void setMeetAddress(String meetAddress) {
        this.meetAddress = meetAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(String startDatetime) {
        this.startDatetime = startDatetime;
    }

    public String getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(String endDatetime) {
        this.endDatetime = endDatetime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public String getIsTop() {
        return isTop;
    }

    public void setIsTop(String isTop) {
        this.isTop = isTop;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public String getApplyType() {
        return applyType;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

    public String getApplyUser() {
        return applyUser;
    }

    public void setApplyUser(String applyUser) {
        this.applyUser = applyUser;
    }

    public String getApplyDatetime() {
        return applyDatetime;
    }

    public void setApplyDatetime(String applyDatetime) {
        this.applyDatetime = applyDatetime;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public int getPointCount() {
        return pointCount;
    }

    public void setPointCount(int pointCount) {
        this.pointCount = pointCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(int collectCount) {
        this.collectCount = collectCount;
    }

    public String getIsEnroll() {
        return isEnroll;
    }

    public void setIsEnroll(String isEnroll) {
        this.isEnroll = isEnroll;
    }

    public String getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(String isCollect) {
        this.isCollect = isCollect;
    }

    public int getEnrollCount() {
        return enrollCount;
    }

    public void setEnrollCount(int enrollCount) {
        this.enrollCount = enrollCount;
    }

    public int getToApproveCount() {
        return toApproveCount;
    }

    public void setToApproveCount(int toApproveCount) {
        this.toApproveCount = toApproveCount;
    }

    public int getApproveCount() {
        return approveCount;
    }

    public void setApproveCount(int approveCount) {
        this.approveCount = approveCount;
    }

    public List<ApprovedBean> getApprovedList() {
        return approvedList;
    }

    public void setApprovedList(List<ApprovedBean> approvedList) {
        this.approvedList = approvedList;
    }

    public class ApprovedBean implements Serializable{
        /**
         * id : 2
         * actCode : AC201804301433538225317
         * userId : U201804291940579421200
         * mobile : 18984955240
         * realName : 雷黔
         * approver : admin
         * approveDatetime : Apr 30, 2018 4:02:36 PM
         * approveNote : 1
         * nickname : 5240
         */

        private String id;
        private String actCode;
        private String userId;
        private String mobile;
        private String realName;
        private String approver;
        private String approveDatetime;
        private String approveNote;
        private String nickname;
        private String photo;

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

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }
}
