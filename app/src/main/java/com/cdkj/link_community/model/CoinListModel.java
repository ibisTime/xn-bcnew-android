package com.cdkj.link_community.model;

/**
 * 币种列表
 * Created by cdkj on 2018/3/24.
 */

public class CoinListModel {


    /**
     * id : 2
     * exchangeEname : bitfinex
     * exchangeCname : Bitfinex
     * coinEname : bitcoin
     * coinCname : 比特币
     * coinSymbol : BTC
     * toCoinSymbol : USD
     * coinSymbolPair : btc/usd
     * lastPrice : 8,755
     * lastCnyPrice : 55,254
     * volume : 44,165
     * changeRate : 0.05
     */

    private String id;
    private String exchangeEname;//
    private String exchangeCname;
    private String coinEname;
    private String coinCname;
    private String coinSymbol;//
    private String toCoinSymbol;
    private String coinSymbolPair;
    private String lastPrice;
    private String lastCnyPrice;
    private String volume;
    private String isChoice;  //是否自选 1 是
    private double changeRate; // - 红色


    public String getIsChoice() {
        return isChoice;
    }

    public void setIsChoice(String isChoice) {
        this.isChoice = isChoice;
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
}
