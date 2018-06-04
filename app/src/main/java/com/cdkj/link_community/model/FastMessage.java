package com.cdkj.link_community.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 李先俊 on 2018/3/23.
 */

public class FastMessage implements Parcelable {


    /**
     * code : XQ20151029602582
     * type : 1
     * content : 小李简历
     * status : 1
     * source : 金色财经
     * updater : admin
     * updateDatetime : 2016-09-18 19:11:41
     * isRead : 1
     * showDatetime : 2018-01-01 00:00:00
     */

    private String code;
    private String type;
    private String content;
    private String status;
    private String source;
    private String updater;
    private String updateDatetime;
    private String isRead;
    private String isTop;
    private String showDatetime;

    private boolean isOpen;// 是否展开状态 用于

    public String getIsTop() {
        return isTop;
    }

    public void setIsTop(String isTop) {
        this.isTop = isTop;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
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

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getShowDatetime() {
        return showDatetime;
    }

    public void setShowDatetime(String showDatetime) {
        this.showDatetime = showDatetime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.type);
        dest.writeString(this.content);
        dest.writeString(this.status);
        dest.writeString(this.source);
        dest.writeString(this.updater);
        dest.writeString(this.updateDatetime);
        dest.writeString(this.isRead);
        dest.writeString(this.showDatetime);
        dest.writeByte(this.isOpen ? (byte) 1 : (byte) 0);
    }

    public FastMessage() {
    }

    protected FastMessage(Parcel in) {
        this.code = in.readString();
        this.type = in.readString();
        this.content = in.readString();
        this.status = in.readString();
        this.source = in.readString();
        this.updater = in.readString();
        this.updateDatetime = in.readString();
        this.isRead = in.readString();
        this.showDatetime = in.readString();
        this.isOpen = in.readByte() != 0;
    }

    public static final Parcelable.Creator<FastMessage> CREATOR = new Parcelable.Creator<FastMessage>() {
        @Override
        public FastMessage createFromParcel(Parcel source) {
            return new FastMessage(source);
        }

        @Override
        public FastMessage[] newArray(int size) {
            return new FastMessage[size];
        }
    };
}
