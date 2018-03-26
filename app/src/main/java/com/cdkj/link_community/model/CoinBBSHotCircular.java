package com.cdkj.link_community.model;

import java.util.List;

/**
 * Created by 李先俊 on 2018/3/26.
 */

public class CoinBBSHotCircular {


    /**
     * code : PS201803840505399299
     * content : xxx，服了你了
     * location : 1
     * status : B
     * userId : U2018038105315757
     * publishDatetime : Mar 25, 2018 5:05:39 PM
     * updater : admin
     * plateCode : PL201803840306262596
     * commentCount : 3.0
     * pointCount : 0.0
     */

    private String code;
    private String content;
    private String location;
    private String status;
    private String userId;
    private String publishDatetime;
    private String updater;
    private String plateCode;
    private String isPoint;
    private String nickname;
    private String photo;
    private int commentCount;
    private int pointCount;
    private List<ReplyComment> commentList;

    public String getIsPoint() {
        return isPoint;
    }

    public void setIsPoint(String isPoint) {
        this.isPoint = isPoint;
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



    public List<ReplyComment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<ReplyComment> commentList) {
        this.commentList = commentList;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPublishDatetime() {
        return publishDatetime;
    }

    public void setPublishDatetime(String publishDatetime) {
        this.publishDatetime = publishDatetime;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public String getPlateCode() {
        return plateCode;
    }

    public void setPlateCode(String plateCode) {
        this.plateCode = plateCode;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getPointCount() {
        return pointCount;
    }

    public void setPointCount(int pointCount) {
        this.pointCount = pointCount;
    }
}
