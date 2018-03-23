package com.cdkj.link_community.model;

/**
 * Created by cdkj on 2018/3/23.
 */

public class MsgDetailsComment {

    /**
     * code : PL201803820101174351
     * type : 2
     * content : 好有评论
     * userId : U2018038105315757
     * commentDatetime : Mar 23, 2018 1:01:17 PM
     * pointCount : 0.0
     * parentCode : PL201803820100253982
     * parentUserId : U2018038105315757
     * objectCode : NS201803821247121521
     * status : A
     * nickname : aa
     * photo : baidu.com
     * parentNickName : aa
     * parentPhoto : baidu.com
     */

    private String code;
    private String type;
    private String content;
    private String userId;
    private String commentDatetime;
    private int pointCount;
    private String parentCode;
    private String parentUserId;
    private String objectCode;
    private String status;
    private String nickname;
    private String photo;
    private String parentNickName;
    private String parentPhoto;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCommentDatetime() {
        return commentDatetime;
    }

    public void setCommentDatetime(String commentDatetime) {
        this.commentDatetime = commentDatetime;
    }

    public int getPointCount() {
        return pointCount;
    }

    public void setPointCount(int pointCount) {
        this.pointCount = pointCount;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getParentUserId() {
        return parentUserId;
    }

    public void setParentUserId(String parentUserId) {
        this.parentUserId = parentUserId;
    }

    public String getObjectCode() {
        return objectCode;
    }

    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getParentNickName() {
        return parentNickName;
    }

    public void setParentNickName(String parentNickName) {
        this.parentNickName = parentNickName;
    }

    public String getParentPhoto() {
        return parentPhoto;
    }

    public void setParentPhoto(String parentPhoto) {
        this.parentPhoto = parentPhoto;
    }
}
