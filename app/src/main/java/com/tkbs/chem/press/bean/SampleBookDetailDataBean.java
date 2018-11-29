package com.tkbs.chem.press.bean;

/**
 * Created by Administrator on 2018/10/30.
 */
public class SampleBookDetailDataBean {


    /**
     * guid : 79C59A84F44F408D9A5F06B722A82371
     * documentGuid : DC58653EAFEC49BCBCA5380277B1E6D9
     * state : 1
     * createDate : 1543369404000
     * title : 现代分子光化学（1）原理篇12
     * price : 100
     * cover : null
     * remark : null
     */

    private String guid;
    private String documentGuid;
    private int state;
    private long createDate;
    private String title;
    private double price;
    private String cover;
    private String remark;
    private int degree;
    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getDocumentGuid() {
        return documentGuid;
    }

    public void setDocumentGuid(String documentGuid) {
        this.documentGuid = documentGuid;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }
}
