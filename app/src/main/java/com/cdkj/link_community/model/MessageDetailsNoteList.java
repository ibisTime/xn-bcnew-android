package com.cdkj.link_community.model;

/**
 * 资讯详情推荐资讯列表
 * Created by cdkj on 2018/3/23.
 */

public class MessageDetailsNoteList {

    /**
     * code : NS201803821247121521
     * type : CA201803820628405408
     * toCoin : BB000001
     * title : 币圈
     * advPic : www.baidu.com
     * content : 小李简历
     * status : 1
     * source : 金色财经
     * auther : xieyj
     * showDatetime : Jan 1, 2018 12:00:00 AM
     * updater : admin
     * updateDatetime : Mar 23, 2018 1:32:38 PM
     * pointCount : 1.0
     * commentCount : 0.0
     * collectCount : 0.0
     */

    private String code;
    private String type;
    private String toCoin;
    private String title;
    private String advPic;
    private String content;
    private String status;
    private String source;
    private String auther;
    private String showDatetime;
    private String updater;
    private String updateDatetime;
    private int pointCount;
    private int commentCount;
    private int collectCount;
    private int readCount;

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

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

    public String getToCoin() {
        return toCoin;
    }

    public void setToCoin(String toCoin) {
        this.toCoin = toCoin;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAuther() {
        return auther;
    }

    public void setAuther(String auther) {
        this.auther = auther;
    }

    public String getShowDatetime() {
        return showDatetime;
    }

    public void setShowDatetime(String showDatetime) {
        this.showDatetime = showDatetime;
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

    public double getPointCount() {
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
}
