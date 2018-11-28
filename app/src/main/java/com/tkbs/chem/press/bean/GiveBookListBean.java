package com.tkbs.chem.press.bean;

/**
 * Created by Administrator on 2018/11/1.
 */
public class GiveBookListBean {


    /**
     * time_limit : 1551324264000
     * price : 100
     * guid : DC58653EAFEC49BCBCA5380277B1E6D9
     * title : 现代分子光化学（1）原理篇12
     */

    private long time_limit;
    private int price;
    private String guid;
    private String title;

    public long getTime_limit() {
        return time_limit;
    }

    public void setTime_limit(long time_limit) {
        this.time_limit = time_limit;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

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
}
