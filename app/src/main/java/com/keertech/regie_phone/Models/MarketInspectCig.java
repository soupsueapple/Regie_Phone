package com.keertech.regie_phone.Models;

import java.io.Serializable;

/**
 * Created by soup on 15/8/5.
 */
public class MarketInspectCig implements Serializable {

    private Integer id;

    /**
     * 类型（1：公开摆卖；2：暗中售卖；3：销售假烟）
     */
    private Integer type;

    /**
     * 卷烟代码
     */
    private String cigCode = "";

    /**
     * 卷烟名称
     */
    private String cigName = "";

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    /**
     * 数量（条）
     */
    private String amount = "";

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCigCode() {
        return cigCode;
    }

    public void setCigCode(String cigCode) {
        this.cigCode = cigCode;
    }

    public String getCigName() {
        return cigName;
    }

    public void setCigName(String cigName) {
        this.cigName = cigName;
    }
}
