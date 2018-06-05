package com.cdkj.link_community.model;

/**
 * 我的行情自选
 * Created by cdkj on 2018/3/25.
 */

public class MyChooseMarket {

//    /**
//     * id : 14
//     * userId : U2018038107391626
//     * exchangeEname : kraken
//     * coin : BTC
//     * toCoin : JPY
//     * orderNo : 1
//     * updateDatetime : Mar 25, 2018 10:55:39 AM
//     * marketFxh : {"id":"14","exchangeEname":"kraken","exchangeCname":"K网","coinEname":"bitcoin","coinCname":"比特币","coinSymbol":"BTC","toCoinSymbol":"JPY","coinSymbolPair":"btc/jpy","lastPrice":"911,623","lastCnyPrice":"54,943","volume":"10","changeRate":"-0.03","isUse":"1"}
//     */
//
//    private String id;
//    private String userId;
//    private String exchangeEname;
//    private String coin;
//    private String toCoin;
//    private String orderNo;
//    private String updateDatetime;
//    private MarketFxhBean marketFxh;
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getUserId() {
//        return userId;
//    }
//
//    public void setUserId(String userId) {
//        this.userId = userId;
//    }
//
//    public String getExchangeEname() {
//        return exchangeEname;
//    }
//
//    public void setExchangeEname(String exchangeEname) {
//        this.exchangeEname = exchangeEname;
//    }
//
//    public String getCoin() {
//        return coin;
//    }
//
//    public void setCoin(String coin) {
//        this.coin = coin;
//    }
//
//    public String getToCoin() {
//        return toCoin;
//    }
//
//    public void setToCoin(String toCoin) {
//        this.toCoin = toCoin;
//    }
//
//    public String getOrderNo() {
//        return orderNo;
//    }
//
//    public void setOrderNo(String orderNo) {
//        this.orderNo = orderNo;
//    }
//
//    public String getUpdateDatetime() {
//        return updateDatetime;
//    }
//
//    public void setUpdateDatetime(String updateDatetime) {
//        this.updateDatetime = updateDatetime;
//    }
//
//    public MarketFxhBean getMarketFxh() {
//        return marketFxh;
//    }
//
//    public void setMarketFxh(MarketFxhBean marketFxh) {
//        this.marketFxh = marketFxh;
//    }

//    public static class MarketFxhBean {


    /**
     * id : 768
     * exchangeEname : Binance
     * exchangeCname : 币安
     * symbol : gto
     * toSymbol : btc
     * amount : 6314
     * lastPrice : 4.263E-5
     * lastCnyPrice : 2.21927446396194
     * volume : 26679167
     * percentChange24h : 0
     * percentChange : 0
     * choiceCount : 0
     */

    private String id;
    private String exchangeEname;
    private String exchangeCname;
    private String symbol;
    private String toSymbol;
    private int amount;
    private String isWarn;
    private String lastPrice;
    private String lastCnyPrice;
    private int volume;
    private int percentChange24h;
    private String percentChange;
    private int choiceCount;
    private int choiceId;

    public int getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(int choiceId) {
        this.choiceId = choiceId;
    }

    public String getIsWarn() {
        return isWarn;
    }

    public void setIsWarn(String isWarn) {
        this.isWarn = isWarn;
    }

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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
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

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getPercentChange24h() {
        return percentChange24h;
    }

    public void setPercentChange24h(int percentChange24h) {
        this.percentChange24h = percentChange24h;
    }

    public String getPercentChange() {
        return percentChange;
    }

    public void setPercentChange(String percentChange) {
        this.percentChange = percentChange;
    }

    public int getChoiceCount() {
        return choiceCount;
    }

    public void setChoiceCount(int choiceCount) {
        this.choiceCount = choiceCount;
    }


//    }


}
