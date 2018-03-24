package com.cdkj.link_community.model;

/**平台类型
 * Created by cdkj on 2018/3/24.
 */

public class PlatformType {


    /**
     * id : 1
     * ename : bitfinex
     * cname : bitfinex
     * status : 1
     * orderNo : 1
     * updater : admin
     * updateDatetime : 2018-03-23 20:33:19.0
     */

    private int id;
    private String ename;
    private String cname;
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
