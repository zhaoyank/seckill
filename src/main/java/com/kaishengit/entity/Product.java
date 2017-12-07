package com.kaishengit.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 
 */
public class Product implements Serializable {
    private Integer id;

    private String productName;

    private String productTitle;

    private Integer productInventory;

    private String image;

    private BigDecimal price;

    private BigDecimal marketPrice;

    private Date startTime;

    private Date endTime;

    private String productDesc;

    private static final long serialVersionUID = 1L;

    /**
     * 判断是否已经开始秒杀
     * @return true:开始 false:没有开始
     */
    public boolean isStart() {
        return startTime.before(new Date());
    }

    /**
     * 判断是否已经结束秒杀
     * @return true:结束 false:没有结束
     */
    public boolean isEnd() {
        return endTime.before(new Date());
    }

    public Long getStartTimeToMil() {
        return startTime.getTime();
    }

    public Long getEndTimeToMil() {
        return endTime.getTime();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public Integer getProductInventory() {
        return productInventory;
    }

    public void setProductInventory(Integer productInventory) {
        this.productInventory = productInventory;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }
}