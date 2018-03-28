package com.cdkj.link_community.model;

/**
 * Created by cdkj on 2018/3/25.
 */

public class CoinBBSListModel {


    /**
     * code : PL201803841149197840
     * name : btc吧
     * introduce : 巴拉巴拉
     * location : 0
     * status : 1
     * updater : admin
     * updateDatetime : Mar 25, 2018 11:56:28 AM
     * keepCount : 0
     * postCount : 0
     * dayCommentCount : 0
     * isKeep : 0
     */

    private String code;
    private String name;
    private String introduce;
    private String location;
    private String status;
    private String updater;
    private String updateDatetime;

    private int keepCount;
    private int postCount;
    private int dayCommentCount;

    private String isKeep;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
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

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public String getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(String updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public int getKeepCount() {
        return keepCount;
    }

    public void setKeepCount(int keepCount) {
        this.keepCount = keepCount;
    }

    public int getPostCount() {
        return postCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }

    public int getDayCommentCount() {
        return dayCommentCount;
    }

    public void setDayCommentCount(int dayCommentCount) {
        this.dayCommentCount = dayCommentCount;
    }

    public String getIsKeep() {
        return isKeep;
    }

    public void setIsKeep(String isKeep) {
        this.isKeep = isKeep;
    }
}
