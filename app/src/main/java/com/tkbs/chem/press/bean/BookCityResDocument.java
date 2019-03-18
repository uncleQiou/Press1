package com.tkbs.chem.press.bean;

/**
 * Created by Administrator on 2018/10/26.
 */
public class BookCityResDocument {

    /**
     * id : 4
     * guid : B114B9D8B8A244088153C43F0B2D1407
     * docno : null
     * shortdocno : null
     * title : 时装设计中的民族元素
     * author : 刘天勇、王培娜  著
     * publishTime : 1525104000000
     * price : 78
     * eprice : null
     * trialPage : null
     * linkXml : null
     * metadataXml : null
     * recommend : 0
     * operateState : null
     * remark : null
     * state : null
     * createUser : null
     * createDate : null
     * modifyUser : null
     * modifyDate : null
     * extInt : null
     * extStr : null
     * cover
     */

    private int id;
    private String guid;
    private long docno;
    private long shortdocno;
    private String title;
    private String author;
    private long publishTime;
    private double price;
    private int degree;
    private String cover;

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public long getDocno() {
        return docno;
    }

    public void setDocno(long docno) {
        this.docno = docno;
    }

    public long getShortdocno() {
        return shortdocno;
    }

    public void setShortdocno(long shortdocno) {
        this.shortdocno = shortdocno;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
