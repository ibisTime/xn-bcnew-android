package com.cdkj.link_community.model;

import java.util.List;

/**
 * 用户 我的评论 回复
 * Created by cdkj on 2018/3/24.
 */

public class UserMyComment {

    /**
     * code : XQ20151029602582
     * type : 1
     * content : 币圈
     * userId : 币圈
     * nickname : 四叶草
     * commentDatetime : 2016-09-18 19:11:41
     * remark :
     * news : {"code":"XQ20151029602582","type":"1","toCoin":"BTC","title":"币圈","advPic":"www.baidu.com","content":"小李简历","source":"金色财经","auther":"xieyj","updater":"admin","updateDatetime":"2016-09-18 19:11:41","remark":""}
     */

    private String code;
    private String type;
    private String content;
    private String userId;
    private String nickname;
    private String commentDatetime;
    private String remark;
    private String photo;
    private NewsBean news;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCommentDatetime() {
        return commentDatetime;
    }

    public void setCommentDatetime(String commentDatetime) {
        this.commentDatetime = commentDatetime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public NewsBean getNews() {
        return news;
    }

    public void setNews(NewsBean news) {
        this.news = news;
    }

    public static class NewsBean {
        /**
         * code : XQ20151029602582
         * type : 1
         * toCoin : BTC
         * title : 币圈
         * advPic : www.baidu.com
         * content : 小李简历
         * source : 金色财经
         * auther : xieyj
         * updater : admin
         * updateDatetime : 2016-09-18 19:11:41
         * remark :
         */

        private String code;
        private String type;
        private String toCoin;
        private String title;
        private String advPic;
        private String content;
        private String source;
        private String auther;
        private String updater;
        private String updateDatetime;
        private String remark;

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

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }

}
