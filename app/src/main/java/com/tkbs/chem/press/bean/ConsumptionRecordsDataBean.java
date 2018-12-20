package com.tkbs.chem.press.bean;

/**
 * 作者    qyl
 * 时间    2018/12/20 15:01
 * 文件    Press
 * 描述
 */
public class ConsumptionRecordsDataBean {

    /**
     * cover : http://192.168.113.147:8580/webFile/resource/C0274126E059486FBF1608169621FD6A/cover.jpg
     * pay_state : 10
     * pay_price : 1900
     * document_guid : C0274126E059486FBF1608169621FD6A
     * degree : 5
     * guid : 145005A923FB4067B4564B959662D294
     * title : 10天打造完美口才（实战篇）
     * create_date : 1545289408000
     */

    private String cover;
    /**
     * //10 支付中 20 支付成功 30支付失败
     */
    private int pay_state;
    private int pay_price;
    private String document_guid;
    private int degree;
    private String guid;
    private String title;
    private long create_date;

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getPay_state() {
        return pay_state;
    }

    public void setPay_state(int pay_state) {
        this.pay_state = pay_state;
    }

    public int getPay_price() {
        return pay_price;
    }

    public void setPay_price(int pay_price) {
        this.pay_price = pay_price;
    }

    public String getDocument_guid() {
        return document_guid;
    }

    public void setDocument_guid(String document_guid) {
        this.document_guid = document_guid;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
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

    public long getCreate_date() {
        return create_date;
    }

    public void setCreate_date(long create_date) {
        this.create_date = create_date;
    }
}
