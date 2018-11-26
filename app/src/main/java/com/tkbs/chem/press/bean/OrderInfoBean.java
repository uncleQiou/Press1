package com.tkbs.chem.press.bean;

/**
 * 作者    qyl
 * 时间    2018/11/23 14:15
 * 文件    Press
 * 描述
 */
public class OrderInfoBean {
    private String title;
    private String order_number;
    private String pay_order_number;
    private int pay_type;
    private double pay_price;
    private String cover;
    private String fileSize;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getPay_order_number() {
        return pay_order_number;
    }

    public void setPay_order_number(String pay_order_number) {
        this.pay_order_number = pay_order_number;
    }

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public double getPay_price() {
        return pay_price;
    }

    public void setPay_price(double pay_price) {
        this.pay_price = pay_price;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }
}
