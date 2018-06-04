package com.cdkj.link_community.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cdkj on 2018/5/16.
 */

public class MarketCoinTab implements Parcelable {


    /**
     * id : 3
     * type : C
     * navCode : ethereum
     * navName : 以太坊
     * location : 0
     * userId : U201805162022516838639
     * updateDatetime : May 16, 2018 8:22:51 PM
     */

    private String id;
    private String type;
    private String navCode;
    private String navName;
    private String location;
    private String userId;
    private String updateDatetime;

    public MarketCoinTab() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNavCode() {
        return navCode;
    }

    public void setNavCode(String navCode) {
        this.navCode = navCode;
    }

    public String getNavName() {
        return navName;
    }

    public void setNavName(String navName) {
        this.navName = navName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(String updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(type);
        parcel.writeString(navCode);
        parcel.writeString(navName);
        parcel.writeString(location);
        parcel.writeString(userId);
        parcel.writeString(updateDatetime);
    }

    protected MarketCoinTab(Parcel in) {
        id = in.readString();
        type = in.readString();
        navCode = in.readString();
        navName = in.readString();
        location = in.readString();
        userId = in.readString();
        updateDatetime = in.readString();
    }

    public static final Creator<MarketCoinTab> CREATOR = new Creator<MarketCoinTab>() {
        @Override
        public MarketCoinTab createFromParcel(Parcel in) {
            return new MarketCoinTab(in);
        }

        @Override
        public MarketCoinTab[] newArray(int size) {
            return new MarketCoinTab[size];
        }
    };
}
