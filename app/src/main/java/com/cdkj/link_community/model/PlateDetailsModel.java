package com.cdkj.link_community.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 版块详情
 * Created by cdkj on 2018/6/13.
 */

public class PlateDetailsModel implements Parcelable {
    /**
     * code : NS20180382013215441
     * description : aaaaa
     * avgChange : 0.123
     * bestChange : 0.2222
     * worstChange : -0.123
     * bestSymbol : eth
     * worstSymbol : btc
     * remark : addg
     * location : 0
     * orderNo : 1
     * status : 1
     * createDatetime : 2018-05-13 14:23:11
     * updater : admin
     * updateDatetime : 2018
     * name : 公链
     * upCount : 2
     * downCount : 4
     * totalCount : 6
     * list : [{"symbol":"xrp","toSymbol":"eth","amount":576,"lastPrice":9.815E-4,"lastCnyPrice":4.2924461805225,"percentChange24h":0},{"symbol":"xrp","toSymbol":"eth","amount":576,"lastPrice":9.815E-4,"lastCnyPrice":4.2924461805225,"percentChange24h":0},{"symbol":"xrp","toSymbol":"eth","amount":576,"lastPrice":9.815E-4,"lastCnyPrice":4.2924461805225,"percentChange24h":0},{"symbol":"xrp","toSymbol":"eth","amount":576,"lastPrice":9.815E-4,"lastCnyPrice":4.2924461805225,"percentChange24h":0},{"symbol":"xrp","toSymbol":"eth","amount":576,"lastPrice":9.815E-4,"lastCnyPrice":4.2924461805225,"percentChange24h":0},{"symbol":"xrp","toSymbol":"eth","amount":576,"lastPrice":9.815E-4,"lastCnyPrice":4.2924461805225,"percentChange24h":0}]
     */

    private String code;
    private String description;
    private BigDecimal avgChange;
    private BigDecimal bestChange;
    private BigDecimal worstChange;
    private String bestSymbol;
    private String worstSymbol;
    private String remark;
    private String location;
    private String orderNo;
    private String status;
    private String createDatetime;
    private String updater;
    private String updateDatetime;
    private String name;
    private String upCount;
    private String downCount;
    private String totalCount;
    private List<ListBean> list;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAvgChange() {
        return avgChange;
    }

    public void setAvgChange(BigDecimal avgChange) {
        this.avgChange = avgChange;
    }

    public BigDecimal getBestChange() {
        return bestChange;
    }

    public void setBestChange(BigDecimal bestChange) {
        this.bestChange = bestChange;
    }

    public BigDecimal getWorstChange() {
        return worstChange;
    }

    public void setWorstChange(BigDecimal worstChange) {
        this.worstChange = worstChange;
    }

    public String getBestSymbol() {
        return bestSymbol;
    }

    public void setBestSymbol(String bestSymbol) {
        this.bestSymbol = bestSymbol;
    }

    public String getWorstSymbol() {
        return worstSymbol;
    }

    public void setWorstSymbol(String worstSymbol) {
        this.worstSymbol = worstSymbol;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpCount() {
        return upCount;
    }

    public void setUpCount(String upCount) {
        this.upCount = upCount;
    }

    public String getDownCount() {
        return downCount;
    }

    public void setDownCount(String downCount) {
        this.downCount = downCount;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Parcelable {
        /**
         * symbol : xrp
         * toSymbol : eth
         * amount : 576
         * lastPrice : 9.815E-4
         * lastCnyPrice : 4.2924461805225
         * percentChange24h : 0
         */

        private String symbol;
        private String toSymbol;
        private BigDecimal count;
        private BigDecimal lastPrice;
        private BigDecimal lastCnyPrice;
        private BigDecimal percentChange24h;

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getToSymbol() {
            return toSymbol;
        }

        public void setToSymbol(String toSymbol) {
            this.toSymbol = toSymbol;
        }

        public BigDecimal getCount() {
            return count;
        }

        public void setCount(BigDecimal count) {
            this.count = count;
        }

        public BigDecimal getLastPrice() {
            return lastPrice;
        }

        public void setLastPrice(BigDecimal lastPrice) {
            this.lastPrice = lastPrice;
        }

        public BigDecimal getLastCnyPrice() {
            return lastCnyPrice;
        }

        public void setLastCnyPrice(BigDecimal lastCnyPrice) {
            this.lastCnyPrice = lastCnyPrice;
        }

        public BigDecimal getPercentChange24h() {
            return percentChange24h;
        }

        public void setPercentChange24h(BigDecimal percentChange24h) {
            this.percentChange24h = percentChange24h;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.symbol);
            dest.writeString(this.toSymbol);
            dest.writeSerializable(this.count);
            dest.writeSerializable(this.lastPrice);
            dest.writeSerializable(this.lastCnyPrice);
            dest.writeSerializable(this.percentChange24h);
        }

        public ListBean() {
        }

        protected ListBean(Parcel in) {
            this.symbol = in.readString();
            this.toSymbol = in.readString();
            this.count = (BigDecimal) in.readSerializable();
            this.lastPrice = (BigDecimal) in.readSerializable();
            this.lastCnyPrice = (BigDecimal) in.readSerializable();
            this.percentChange24h = (BigDecimal) in.readSerializable();
        }

        public static final Creator<ListBean> CREATOR = new Creator<ListBean>() {
            @Override
            public ListBean createFromParcel(Parcel source) {
                return new ListBean(source);
            }

            @Override
            public ListBean[] newArray(int size) {
                return new ListBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.description);
        dest.writeSerializable(this.avgChange);
        dest.writeSerializable(this.bestChange);
        dest.writeSerializable(this.worstChange);
        dest.writeString(this.bestSymbol);
        dest.writeString(this.worstSymbol);
        dest.writeString(this.remark);
        dest.writeString(this.location);
        dest.writeString(this.orderNo);
        dest.writeString(this.status);
        dest.writeString(this.createDatetime);
        dest.writeString(this.updater);
        dest.writeString(this.updateDatetime);
        dest.writeString(this.name);
        dest.writeString(this.upCount);
        dest.writeString(this.downCount);
        dest.writeString(this.totalCount);
        dest.writeList(this.list);
    }

    public PlateDetailsModel() {
    }

    protected PlateDetailsModel(Parcel in) {
        this.code = in.readString();
        this.description = in.readString();
        this.avgChange = (BigDecimal) in.readSerializable();
        this.bestChange = (BigDecimal) in.readSerializable();
        this.worstChange = (BigDecimal) in.readSerializable();
        this.bestSymbol = in.readString();
        this.worstSymbol = in.readString();
        this.remark = in.readString();
        this.location = in.readString();
        this.orderNo = in.readString();
        this.status = in.readString();
        this.createDatetime = in.readString();
        this.updater = in.readString();
        this.updateDatetime = in.readString();
        this.name = in.readString();
        this.upCount = in.readString();
        this.downCount = in.readString();
        this.totalCount = in.readString();
        this.list = new ArrayList<ListBean>();
        in.readList(this.list, ListBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<PlateDetailsModel> CREATOR = new Parcelable.Creator<PlateDetailsModel>() {
        @Override
        public PlateDetailsModel createFromParcel(Parcel source) {
            return new PlateDetailsModel(source);
        }

        @Override
        public PlateDetailsModel[] newArray(int size) {
            return new PlateDetailsModel[size];
        }
    };
}
