package com.cdkj.link_community.model;

/**
 * 骗我的收藏
 * Created by cdkj on 2018/3/24.
 */

public class CollectionList {


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
     * commentCount : 0
     * collectCount : 0
     */

    private String code;
    private String type;
    private String title;
    private String content;
    private String status;
    private String source;
    private String auther;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
