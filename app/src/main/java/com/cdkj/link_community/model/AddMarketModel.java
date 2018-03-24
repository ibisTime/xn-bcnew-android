package com.cdkj.link_community.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cdkj on 2018/3/24.
 */

public class AddMarketModel implements Parcelable {


    /**
     * type : 1
     * ename : BTC
     * sname : 比特币
     */

    private String type;
    private String ename;
    private String sname;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.ename);
        dest.writeString(this.sname);
    }

    public AddMarketModel() {
    }

    protected AddMarketModel(Parcel in) {
        this.type = in.readString();
        this.ename = in.readString();
        this.sname = in.readString();
    }

    public static final Parcelable.Creator<AddMarketModel> CREATOR = new Parcelable.Creator<AddMarketModel>() {
        @Override
        public AddMarketModel createFromParcel(Parcel source) {
            return new AddMarketModel(source);
        }

        @Override
        public AddMarketModel[] newArray(int size) {
            return new AddMarketModel[size];
        }
    };
}
