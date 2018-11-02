package com.tkbs.chem.press.bean;

/**
 * Created by Administrator on 2018/11/1.
 */
public class GiveBookListBean {

    /**
     * guid : 110C836D1F3247E79342D961AD3C282B
     * title : 有机合成原理与工艺
     * price : 35
     * time : null
     */

    private String guid;
    private String title;
    private double price;
    private long time;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
