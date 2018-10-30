package com.tkbs.chem.press.bean;

/**
 * Created by Administrator on 2018/10/30.
 */
public class SampleBookDetailDataBean {

    /**
     * guid : E82DCBEE738947D889FE743A3331D618
     * state : 0
     * operateDate : 1527842713000
     * title : 150种生物柴油配方与制作
     * price : 36
     * cover :
     */

    private String guid;
    private int state;
    private long operateDate;
    private String title;
    private int price;
    private String cover;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(long operateDate) {
        this.operateDate = operateDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}
