package com.kaishengit.entity;

import java.io.Serializable;

/**
 * @author 
 */
public class AccountProductKey implements Serializable {
    private Integer accountid;

    private Integer productid;

    private static final long serialVersionUID = 1L;

    public Integer getAccountid() {
        return accountid;
    }

    public void setAccountid(Integer accountid) {
        this.accountid = accountid;
    }

    public Integer getProductid() {
        return productid;
    }

    public void setProductid(Integer productid) {
        this.productid = productid;
    }
}