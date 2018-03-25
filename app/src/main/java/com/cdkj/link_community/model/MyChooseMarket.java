package com.cdkj.link_community.model;

import java.util.List;

/**
 * 我的行情自选
 * Created by cdkj on 2018/3/25.
 */

public class MyChooseMarket {


    /**
     * id : 14
     * userId : U2018038107391626
     * exchangeEname : kraken
     * coin : BTC
     * toCoin : JPY
     * orderNo : 1
     * updateDatetime : Mar 25, 2018 10:55:39 AM
     * marketFxh : {"id":"14","exchangeEname":"kraken","exchangeCname":"K网","coinEname":"bitcoin","coinCname":"比特币","coinSymbol":"BTC","toCoinSymbol":"JPY","coinSymbolPair":"btc/jpy","lastPrice":"911,623","lastCnyPrice":"54,943","volume":"10","changeRate":"-0.03","isUse":"1"}
     */

    private String id;
    private String userId;
    private String exchangeEname;
    private String coin;
    private String toCoin;
    private String orderNo;
    private String updateDatetime;
    private MarketFxhBean marketFxh;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getExchangeEname() {
        return exchangeEname;
    }

    public void setExchangeEname(String exchangeEname) {
        this.exchangeEname = exchangeEname;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getToCoin() {
        return toCoin;
    }

    public void setToCoin(String toCoin) {
        this.toCoin = toCoin;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(String updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public MarketFxhBean getMarketFxh() {
        return marketFxh;
    }

    public void setMarketFxh(MarketFxhBean marketFxh) {
        this.marketFxh = marketFxh;
    }

    public static class MarketFxhBean {
        /**
         * id : 14
         * exchangeEname : kraken
         * exchangeCname : K网
         * coinEname : bitcoin
         * coinCname : 比特币
         * coinSymbol : BTC
         * toCoinSymbol : JPY
         * coinSymbolPair : btc/jpy
         * lastPrice : 911,623
         * lastCnyPrice : 54,943
         * volume : 10
         * changeRate : -0.03
         * isUse : 1
         */

        private String id;
        private String exchangeEname;
        private String exchangeCname;
        private String coinEname;
        private String coinCname;
        private String coinSymbol;
        private String toCoinSymbol;
        private String coinSymbolPair;
        private String lastPrice;
        private String lastCnyPrice;
        private String volume;
        private double changeRate;
        private String isUse;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getExchangeEname() {
            return exchangeEname;
        }

        public void setExchangeEname(String exchangeEname) {
            this.exchangeEname = exchangeEname;
        }

        public String getExchangeCname() {
            return exchangeCname;
        }

        public void setExchangeCname(String exchangeCname) {
            this.exchangeCname = exchangeCname;
        }

        public String getCoinEname() {
            return coinEname;
        }

        public void setCoinEname(String coinEname) {
            this.coinEname = coinEname;
        }

        public String getCoinCname() {
            return coinCname;
        }

        public void setCoinCname(String coinCname) {
            this.coinCname = coinCname;
        }

        public String getCoinSymbol() {
            return coinSymbol;
        }

        public void setCoinSymbol(String coinSymbol) {
            this.coinSymbol = coinSymbol;
        }

        public String getToCoinSymbol() {
            return toCoinSymbol;
        }

        public void setToCoinSymbol(String toCoinSymbol) {
            this.toCoinSymbol = toCoinSymbol;
        }

        public String getCoinSymbolPair() {
            return coinSymbolPair;
        }

        public void setCoinSymbolPair(String coinSymbolPair) {
            this.coinSymbolPair = coinSymbolPair;
        }

        public String getLastPrice() {
            return lastPrice;
        }

        public void setLastPrice(String lastPrice) {
            this.lastPrice = lastPrice;
        }

        public String getLastCnyPrice() {
            return lastCnyPrice;
        }

        public void setLastCnyPrice(String lastCnyPrice) {
            this.lastCnyPrice = lastCnyPrice;
        }

        public String getVolume() {
            return volume;
        }

        public void setVolume(String volume) {
            this.volume = volume;
        }

        public double getChangeRate() {
            return changeRate;
        }

        public void setChangeRate(double changeRate) {
            this.changeRate = changeRate;
        }

        public String getIsUse() {
            return isUse;
        }

        public void setIsUse(String isUse) {
            this.isUse = isUse;
        }
    }

}
