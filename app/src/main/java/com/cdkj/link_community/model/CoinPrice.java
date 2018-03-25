package com.cdkj.link_community.model;

/**
 * Created by cdkj on 2018/3/25.
 */

public class CoinPrice {


    /**
     * id : 1
     * name : Bitcoin
     * symbol : BTC
     * rank : 1
     * priceUsd : 8612.81
     * priceCny : 54367.0712035
     * h24VolumeUsd : 5606960000.0
     * h24VolumeCny : 35393093956.0
     * marketCapUsd : 145882053218
     * marketCapCny : 920858578631
     * totalSuply : 16937800.0
     * maxSupply : 21000000.0
     * percentChange24h : -2.71
     */

    private String id;
    private String name;
    private String symbol;
    private String rank;
    private String priceUsd;
    private String priceCny; //
    private String h24VolumeUsd;
    private String h24VolumeCny;//
    private String marketCapUsd;
    private String marketCapCny;
    private String totalSuply;
    private String maxSupply;
    private double percentChange24h;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getPriceUsd() {
        return priceUsd;
    }

    public void setPriceUsd(String priceUsd) {
        this.priceUsd = priceUsd;
    }

    public String getPriceCny() {
        return priceCny;
    }

    public void setPriceCny(String priceCny) {
        this.priceCny = priceCny;
    }

    public String getH24VolumeUsd() {
        return h24VolumeUsd;
    }

    public void setH24VolumeUsd(String h24VolumeUsd) {
        this.h24VolumeUsd = h24VolumeUsd;
    }

    public String getH24VolumeCny() {
        return h24VolumeCny;
    }

    public void setH24VolumeCny(String h24VolumeCny) {
        this.h24VolumeCny = h24VolumeCny;
    }

    public String getMarketCapUsd() {
        return marketCapUsd;
    }

    public void setMarketCapUsd(String marketCapUsd) {
        this.marketCapUsd = marketCapUsd;
    }

    public String getMarketCapCny() {
        return marketCapCny;
    }

    public void setMarketCapCny(String marketCapCny) {
        this.marketCapCny = marketCapCny;
    }

    public String getTotalSuply() {
        return totalSuply;
    }

    public void setTotalSuply(String totalSuply) {
        this.totalSuply = totalSuply;
    }

    public String getMaxSupply() {
        return maxSupply;
    }

    public void setMaxSupply(String maxSupply) {
        this.maxSupply = maxSupply;
    }

    public double getPercentChange24h() {
        return percentChange24h;
    }

    public void setPercentChange24h(double percentChange24h) {
        this.percentChange24h = percentChange24h;
    }
}
