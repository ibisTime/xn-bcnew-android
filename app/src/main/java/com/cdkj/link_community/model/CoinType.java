package com.cdkj.link_community.model;

/**
 * Created by cdkj on 2018/3/24.
 */

public class CoinType {


    /**
     * id : 1
     * ename : bitcoin
     * cname : 比特币
     * symbol : BTC
     * status : 1
     * orderNo : 1
     * lastPrice : 0.00000000
     * todayVol : 0
     * todayChange : 0.00000000
     * totalSupply : 15844176.0
     * maxSupply : 21000000.0
     * totalPrice : 956600000000
     * rank : 1
     */

    private int id;
    private String ename;
    private String cname;
    private String symbol;
    private String status;
    private String orderNo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
