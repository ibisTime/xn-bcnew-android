package com.cdkj.link_community.model;

import java.math.BigDecimal;

/**统计币种或平台的发帖量
 * Created by cdkj on 2018/3/26.
 */

public class CoinBBSInfoTotalCount {

    private BigDecimal totalCount;
    private String isExistPlate;  //币吧是否存在 1 是

    public String getIsExistPlate() {
        return isExistPlate;
    }

    public void setIsExistPlate(String isExistPlate) {
        this.isExistPlate = isExistPlate;
    }

    public BigDecimal getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(BigDecimal totalCount) {
        this.totalCount = totalCount;
    }
}
