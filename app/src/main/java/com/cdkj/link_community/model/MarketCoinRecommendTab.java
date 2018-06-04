package com.cdkj.link_community.model;

/**
 * Created by cdkj on 2018/5/16.
 */

public class MarketCoinRecommendTab {


    /**
     * id : 1
     * ename : bitcoin
     * cname : 比特币
     * symbol : BTC
     * unit : 0
     * isChoice : 1
     */

    private String id;
    private String ename;
    private String cname;
    private String symbol;
    private int unit;
    private String isChoice;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public String getIsChoice() {
        return isChoice;
    }

    public void setIsChoice(String isChoice) {
        this.isChoice = isChoice;
    }
}
