package com.cdkj.link_community.model;

import java.io.Serializable;

/**
 * 币种列表
 * Created by cdkj on 2018/3/24.
 */

public class CoinListModel implements Serializable {


    /**
     * id : 7
     * exchangeEname : huobiPro
     * exchangeCname : 火币Pro
     * symbol : ada
     * toSymbol : usdt
     * symbolPair : ada/usdt
     * amount : 2.7565636811519306E7
     * open : 0.262039
     * close : 0.278206
     * low : 0.260075
     * high : 0.285669
     * bidPrice : 0.278132
     * bidAmount : 47.1785
     * askPrice : 0.2784
     * askAmount : 500
     * lastPrice : 0.278206
     * lastUsdPrice : 1.7480239392
     * lastCnyPrice : 1.7480239392
     * volume : 7534485.2410606565
     * marketCapUsd : 0
     * percentChange24h : 0.011294115936444698
     * percentChange7d : 0.011294115936444698
     * percentChange1m : 0.011294115936444698
     * percentChange : 0.011294115936444698
     */

    private String id;
    private int choiceCount;
    private String exchangeEname;
    private String exchangeCname;
    private String symbol;
    private String toSymbol;
    private String symbolPair;
    private String amount;
    private String count;
    private String volume;
    private String open;
    private String close;
    private String low;
    private String high;
    private String priceChange;
    private String bidPrice;
    private String bidAmount;
    private String askPrice;
    private String askAmount;
    private String lastPrice;
    private String lastUsdPrice;
    private String lastCnyPrice;

    private String totalMarketCapUsd;
    private String totalMarketCapCny;
    private String maxMarketCapUsd;
    private String maxMarketCapCny;

    private String isWarn;
    private String symbolPic;
    private String percentChange24h;
    private String percentChange7d;
    private String percentChange1m;
    private String percentChange;
    private String isChoice;  //是否自选 1 是
    private boolean isNeedChoice; // 是否需要（收藏）自选按钮
    private boolean isCloseDrawer; // 是否关闭抽屉（模拟交易）

    public String getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(String priceChange) {
        this.priceChange = priceChange;
    }

    public String getIsWarn() {
        return isWarn;
    }

    public void setIsWarn(String isWarn) {
        this.isWarn = isWarn;
    }

    public boolean isCloseDrawer() {
        return isCloseDrawer;
    }

    public void setCloseDrawer(boolean closeDrawer) {
        isCloseDrawer = closeDrawer;
    }

    private Coin coin;

    public Coin getCoin() {
        return coin;
    }

    public void setCoin(Coin coin) {
        this.coin = coin;
    }

    public String getSymbolPic() {
        return symbolPic;
    }

    public void setSymbolPic(String symbolPic) {
        this.symbolPic = symbolPic;
    }

    public boolean isNeedChoice() {
        return isNeedChoice;
    }

    public void setNeedChoice(boolean needChoice) {
        isNeedChoice = needChoice;
    }

    public int getChoiceCount() {
        return choiceCount;
    }

    public void setChoiceCount(int choiceCount) {
        this.choiceCount = choiceCount;
    }

    public String getIsChoice() {
        return isChoice;
    }

    public void setIsChoice(String isChoice) {
        this.isChoice = isChoice;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
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
        if (symbol != null) {
            return symbol.toUpperCase();
        }
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getToSymbol() {
        if (toSymbol != null) {
            return toSymbol.toUpperCase();
        }
        return toSymbol;
    }

    public void setToSymbol(String toSymbol) {
        this.toSymbol = toSymbol;
    }

    public String getSymbolPair() {
        return symbolPair;
    }

    public void setSymbolPair(String symbolPair) {
        this.symbolPair = symbolPair;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(String bidPrice) {
        this.bidPrice = bidPrice;
    }

    public String getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(String bidAmount) {
        this.bidAmount = bidAmount;
    }

    public String getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(String askPrice) {
        this.askPrice = askPrice;
    }

    public String getAskAmount() {
        return askAmount;
    }

    public void setAskAmount(String askAmount) {
        this.askAmount = askAmount;
    }

    public String getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(String lastPrice) {
        this.lastPrice = lastPrice;
    }

    public String getLastUsdPrice() {
        return lastUsdPrice;
    }

    public void setLastUsdPrice(String lastUsdPrice) {
        this.lastUsdPrice = lastUsdPrice;
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

    public String getPercentChange24h() {
        return percentChange24h;
    }

    public void setPercentChange24h(String percentChange24h) {
        this.percentChange24h = percentChange24h;
    }

    public String getPercentChange7d() {
        return percentChange7d;
    }

    public void setPercentChange7d(String percentChange7d) {
        this.percentChange7d = percentChange7d;
    }

    public String getPercentChange1m() {
        return percentChange1m;
    }

    public void setPercentChange1m(String percentChange1m) {
        this.percentChange1m = percentChange1m;
    }

    public String getPercentChange() {
        return percentChange;
    }

    public void setPercentChange(String percentChange) {
        this.percentChange = percentChange;
    }

    public String getTotalMarketCapUsd() {
        return totalMarketCapUsd;
    }

    public void setTotalMarketCapUsd(String totalMarketCapUsd) {
        this.totalMarketCapUsd = totalMarketCapUsd;
    }

    public String getTotalMarketCapCny() {
        return totalMarketCapCny;
    }

    public void setTotalMarketCapCny(String totalMarketCapCny) {
        this.totalMarketCapCny = totalMarketCapCny;
    }

    public String getMaxMarketCapUsd() {
        return maxMarketCapUsd;
    }

    public void setMaxMarketCapUsd(String maxMarketCapUsd) {
        this.maxMarketCapUsd = maxMarketCapUsd;
    }

    public String getMaxMarketCapCny() {
        return maxMarketCapCny;
    }

    public void setMaxMarketCapCny(String maxMarketCapCny) {
        this.maxMarketCapCny = maxMarketCapCny;
    }

    public class Coin implements Serializable {


        /**
         * id : 2
         * ename : ethereum
         * cname : 以太坊
         * symbol : ETH
         * unit : 0
         * pic : ETH@3x_1524731524144.png
         * introduce :
         * ETH
         * status : 1
         * updater : hss
         * updateDatetime : Apr 26, 2018 4:32:14 PM
         * remark :
         * totalSupply : 99056287.0
         * totalSupplyMarket : 61904235860.0
         * rank : 2
         * putExchange :  ETH
         * topExchange : 2
         * walletType :  ETH
         * webUrl :  ETH
         * gitUrl :  ETH
         * twitter :  ETH
         * icoDatetime :  ETH
         * icoCost :  ETH
         * raiseAmount :  ETH
         * tokenDist :  ETH
         * lastCommitCount : 1
         * totalCommitCount : 1
         * totalDist : 100
         * fansCount : 1
         * keepCount : 1
         * copyCount : 1
         */

        private String id;
        private String ename = "-";
        private String cname = "-";
        private String symbol = "-";
        private int unit;
        private String pic = "-";
        private String introduce = "-";
        private String status = "-";
        private String updater = "-";
        private String updateDatetime = "-";
        private String remark = "-";
        private String totalSupply = "-";
        private String totalSupplyMarket = "-";
        private String maxSupply = "-";
        private String maxSupplyMarket = "-";
        private String rank = "-";
        private String putExchange = "-";
        private String topExchange = "-";
        private String walletType = "-";
        private String webUrl = "-";
        private String gitUrl = "-";
        private String twitter = "-";
        private String icoDatetime = "-";
        private String icoCost = "-";
        private String raiseAmount = "-";
        private String tokenDist = "-";
        private String lastCommitCount = "-";
        private String totalCommitCount = "-";
        private String totalDist = "-";
        private String fansCount = "-";
        private String keepCount = "-";
        private String copyCount = "-";

        public String getMaxSupplyMarket() {
            return maxSupplyMarket;
        }

        public void setMaxSupplyMarket(String maxSupplyMarket) {
            this.maxSupplyMarket = maxSupplyMarket;
        }

        public String getMaxSupply() {
            return maxSupply;
        }

        public void setMaxSupply(String maxSupply) {
            this.maxSupply = maxSupply;
        }

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

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
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

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getTotalSupply() {
            return totalSupply;
        }

        public void setTotalSupply(String totalSupply) {
            this.totalSupply = totalSupply;
        }

        public String getTotalSupplyMarket() {
            return totalSupplyMarket;
        }

        public void setTotalSupplyMarket(String totalSupplyMarket) {
            this.totalSupplyMarket = totalSupplyMarket;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getPutExchange() {
            return putExchange;
        }

        public void setPutExchange(String putExchange) {
            this.putExchange = putExchange;
        }

        public String getTopExchange() {
            return topExchange;
        }

        public void setTopExchange(String topExchange) {
            this.topExchange = topExchange;
        }

        public String getWalletType() {
            return walletType;
        }

        public void setWalletType(String walletType) {
            this.walletType = walletType;
        }

        public String getWebUrl() {
            return webUrl;
        }

        public void setWebUrl(String webUrl) {
            this.webUrl = webUrl;
        }

        public String getGitUrl() {
            return gitUrl;
        }

        public void setGitUrl(String gitUrl) {
            this.gitUrl = gitUrl;
        }

        public String getTwitter() {
            return twitter;
        }

        public void setTwitter(String twitter) {
            this.twitter = twitter;
        }

        public String getIcoDatetime() {
            return icoDatetime;
        }

        public void setIcoDatetime(String icoDatetime) {
            this.icoDatetime = icoDatetime;
        }

        public String getIcoCost() {
            return icoCost;
        }

        public void setIcoCost(String icoCost) {
            this.icoCost = icoCost;
        }

        public String getRaiseAmount() {
            return raiseAmount;
        }

        public void setRaiseAmount(String raiseAmount) {
            this.raiseAmount = raiseAmount;
        }

        public String getTokenDist() {
            return tokenDist;
        }

        public void setTokenDist(String tokenDist) {
            this.tokenDist = tokenDist;
        }

        public String getLastCommitCount() {
            return lastCommitCount;
        }

        public void setLastCommitCount(String lastCommitCount) {
            this.lastCommitCount = lastCommitCount;
        }

        public String getTotalCommitCount() {
            return totalCommitCount;
        }

        public void setTotalCommitCount(String totalCommitCount) {
            this.totalCommitCount = totalCommitCount;
        }

        public String getTotalDist() {
            return totalDist;
        }

        public void setTotalDist(String totalDist) {
            this.totalDist = totalDist;
        }

        public String getFansCount() {
            return fansCount;
        }

        public void setFansCount(String fansCount) {
            this.fansCount = fansCount;
        }

        public String getKeepCount() {
            return keepCount;
        }

        public void setKeepCount(String keepCount) {
            this.keepCount = keepCount;
        }

        public String getCopyCount() {
            return copyCount;
        }

        public void setCopyCount(String copyCount) {
            this.copyCount = copyCount;
        }
    }
}
