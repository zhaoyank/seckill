package com.kaishengit.entity;

import java.io.Serializable;

/**
 * @author 
 */
public class Account implements Serializable {
    private Integer id;

    private String accountName;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}