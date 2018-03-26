package com.cdkj.link_community.model;

import java.util.List;

/**
 * 币吧详情
 * Created by cdkj on 2018/3/26.
 */

public class CoinBBSDetails {


    /**
     * code : PL201803841149197840
     * name : btc吧
     * introduce : 巴拉巴拉
     * toCoin : BTC
     * location : 0
     * status : 1
     * updater : admin
     * updateDatetime : Mar 25, 2018 11:56:28 AM
     * keepCount : 0
     * postCount : 0
     * dayCommentCount : 0
     * coin : {"id":"1","ename":"bitcoin","cname":"比特币","symbol":"BTC","status":"1","orderNo":"1","lastPrice":"54167.2222025","todayVol":"33789820179.5","todayChange":"-4.07","totalSupply":"16938137.0","maxSupply":"21000000.0","marketCap":"917491830575","rank":"1"}
     * isKeep : 0
     */

    private String code;
    private String name;
    private String introduce;
    private String toCoin;
    private String location;
    private String status;
    private String updater;
    private String updateDatetime;
    private int keepCount;
    private int postCount;
    private int dayCommentCount;
    private CoinBean coin;
    private String isKeep;
    private List<CoinBBSHotCircular> hotPostList;

    public List<CoinBBSHotCircular> getHotPostList() {
        return hotPostList;
    }

    public void setHotPostList(List<CoinBBSHotCircular> hotPostList) {
        this.hotPostList = hotPostList;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getToCoin() {
        return toCoin;
    }

    public void setToCoin(String toCoin) {
        this.toCoin = toCoin;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public int getKeepCount() {
        return keepCount;
    }

    public void setKeepCount(int keepCount) {
        this.keepCount = keepCount;
    }

    public int getPostCount() {
        return postCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }

    public int getDayCommentCount() {
        return dayCommentCount;
    }

    public void setDayCommentCount(int dayCommentCount) {
        this.dayCommentCount = dayCommentCount;
    }

    public CoinBean getCoin() {
        return coin;
    }

    public void setCoin(CoinBean coin) {
        this.coin = coin;
    }

    public String getIsKeep() {
        return isKeep;
    }

    public void setIsKeep(String isKeep) {
        this.isKeep = isKeep;
    }

    public static class CoinBean {
        /**
         * id : 1
         * ename : bitcoin
         * cname : 比特币
         * symbol : BTC
         * status : 1
         * orderNo : 1
         * lastPrice : 54167.2222025
         * todayVol : 33789820179.5
         * todayChange : -4.07
         * totalSupply : 16938137.0
         * maxSupply : 21000000.0
         * marketCap : 917491830575
         * rank : 1
         */

        private String id;
        private String ename;
        private String cname;
        private String symbol;
        private String status;
        private String orderNo;
        private String lastPrice;
        private String todayVol;
        private String todayChange;
        private String totalSupply;
        private String maxSupply;
        private String marketCap;
        private String rank;

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

        public String getLastPrice() {
            return lastPrice;
        }

        public void setLastPrice(String lastPrice) {
            this.lastPrice = lastPrice;
        }

        public String getTodayVol() {
            return todayVol;
        }

        public void setTodayVol(String todayVol) {
            this.todayVol = todayVol;
        }

        public String getTodayChange() {
            return todayChange;
        }

        public void setTodayChange(String todayChange) {
            this.todayChange = todayChange;
        }

        public String getTotalSupply() {
            return totalSupply;
        }

        public void setTotalSupply(String totalSupply) {
            this.totalSupply = totalSupply;
        }

        public String getMaxSupply() {
            return maxSupply;
        }

        public void setMaxSupply(String maxSupply) {
            this.maxSupply = maxSupply;
        }

        public String getMarketCap() {
            return marketCap;
        }

        public void setMarketCap(String marketCap) {
            this.marketCap = marketCap;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }
    }
}
