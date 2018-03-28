package com.cdkj.link_community.model;

/**
 * Created by cdkj on 2018/3/26.
 */

public class UserBBSComment {


    /**
     * code : PL201803840613195137
     * type : 4
     * content : 好有道理
     * userId : U2018038105315757
     * commentDatetime : Mar 25, 2018 6:13:19 PM
     * pointCount : 0
     * parentCode : PL201803840514482314
     * parentUserId : U2018038105315757
     * objectCode : PS201803840505399299
     * status : D
     * nickname : aa
     * photo : baidu.com
     * post : {"code":"PS201803840505399299","content":"xxx","location":"1","status":"B","userId":"U2018038105315757","publishDatetime":"Mar 25, 2018 5:05:39 PM","updater":"admin","plateCode":"PL201803840306262596","commentCount":3,"pointCount":0,"plateName":"btc吧","nickname":"aa","photo":"baidu.com","mobile":"18767101909"}
     */

    private String code;
    private String type;
    private String content;
    private String userId;
    private String commentDatetime;
    private int pointCount;
    private String parentCode;
    private String parentUserId;
    private String parentNickName;
    private String objectCode;
    private String status;
    private String nickname;
    private String photo;
    private String isMyComment; //1 我回复别人
    private PostBean post;

    public String getParentNickName() {
        return parentNickName;
    }

    public void setParentNickName(String parentNickName) {
        this.parentNickName = parentNickName;
    }

    public String getIsMyComment() {
        return isMyComment;
    }

    public void setIsMyComment(String isMyComment) {
        this.isMyComment = isMyComment;
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

    public PostBean getPost() {
        return post;
    }

    public void setPost(PostBean post) {
        this.post = post;
    }

    public static class PostBean {
        /**
         * code : PS201803840505399299
         * content : xxx
         * location : 1
         * status : B
         * userId : U2018038105315757
         * publishDatetime : Mar 25, 2018 5:05:39 PM
         * updater : admin
         * plateCode : PL201803840306262596
         * commentCount : 3
         * pointCount : 0
         * plateName : btc吧
         * nickname : aa
         * photo : baidu.com
         * mobile : 18767101909
         */

        private String code;
        private String content;
        private String location;
        private String status;
        private String userId;
        private String publishDatetime;
        private String updater;
        private String plateCode;
        private int commentCount;
        private int pointCount;
        private String plateName;
        private String nickname;
        private String photo;
        private String mobile;

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

        public String getPlateName() {
            return plateName;
        }

        public void setPlateName(String plateName) {
            this.plateName = plateName;
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

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
    }
}
