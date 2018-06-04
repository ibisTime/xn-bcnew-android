package com.cdkj.link_community.model;

import java.io.Serializable;

/**
 * Created by cdkj on 2018/4/26.
 */

public class UserWarnModel implements Serializable {


    /**
     * id : 3
     * type : 0
     * exchangeEname : huobiPro
     * symbol : ETH
     * toSymbol : USDT
     * currentPrice : 3000
     * changeRate : 10
     * warnPrice : 3100
     * warnContent : 赶紧卖
     * status : 0
     */

    private String id;
    private String type;
    private String exchangeEname;
    private String symbol;
    private String toSymbol;
    private String currentPrice;
    private String changeRate;
    private String warnPrice;
    private String warnContent;
    private String status;
    private String warnCurrency;
    private String warnDirection;

    public String getWarnDirection() {
        return warnDirection;
    }

    public void setWarnDirection(String warnDirection) {
        this.warnDirection = warnDirection;
    }

    public String getWarnCurrency() {
        return warnCurrency;
    }

    public void setWarnCurrency(String warnCurrency) {
        this.warnCurrency = warnCurrency;
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

    public String getExchangeEname() {
        return exchangeEname;
    }

    public void setExchangeEname(String exchangeEname) {
        this.exchangeEname = exchangeEname;
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

    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getChangeRate() {
        return changeRate;
    }

    public void setChangeRate(String changeRate) {
        this.changeRate = changeRate;
    }

    public String getWarnPrice() {
        return warnPrice;
    }

    public void setWarnPrice(String warnPrice) {
        this.warnPrice = warnPrice;
    }

    public String getWarnContent() {
        return warnContent;
    }

    public void setWarnContent(String warnContent) {
        this.warnContent = warnContent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
