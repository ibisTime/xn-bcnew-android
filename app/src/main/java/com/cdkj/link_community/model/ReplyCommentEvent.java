package com.cdkj.link_community.model;

/**
 * 评论回复
 * Created by cdkj on 2018/3/24.
 */

public class ReplyCommentEvent {

    private String code;

    private String name;

    private String content;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
