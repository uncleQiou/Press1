package com.tkbs.chem.press.bean;

/**
 * 作者    qyl
 * 时间    2018/11/22 12:43
 * 文件    Press
 * 描述
 */
public class BookDetailBean {


    /**
     * id : 309
     * guid : 110C836D1F3247E79342D961AD3C282B
     * docno : 9787122251695
     * shortdocno : 25169
     * title : 有机合成原理与工艺
     * author : 吴建一、缪程平、宗乾收、刘丹 编著
     * publishTime : 1443628800000
     * price : 35
     * eprice : 55
     * trialPage : 19
     * linkXml :
     * metadataXml :
     * recommend :
     * operateState : 2
     * remark :
     * state : 1
     * degree : 63
     * cover : http://192.168.1.251:8580/webFile/resource/110C836D1F3247E79342D961AD3C282B/cover.jpg
     * introduction : 本书从有机化学基础知识入手，系统介绍了有机合成的相关机理，并将有机合成理论与实践相结合，系统性较强，便于学生自学和“学习共同体”课堂教学改革的试行。全书共分9章，内容包括：有机合成的历史、有机合成基础知识、碳-碳单键的形成、碳碳双键的形成、氧化反应、还原反应、重排反应、杂环的形成和有机合成设计。本书既可作为高等院校化学、化工、制药等专业有机合成教学用书，也可作为从事相关科学研究的人员参考用书或自学用书。
     * editionnum : 1
     * wordnum : 228
     * pagenum : 181
     * edited : 魏巍、赵玉清
     */

    private int id;
    private String guid;
    private String docno;
    private String shortdocno;
    private String title;
    private String author;
    private long publishTime;
    private double price;
    private int eprice;
    //    private int trialPage;
//    private String linkXml;
//    private String metadataXml;
//    private String recommend;
//    private int operateState;
//    private String remark;
//    private int state;
//    private int degree;
    private String cover;
//    private String introduction;
//    private String editionnum;
//    private String wordnum;
//    private String pagenum;
//    private String edited;

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

    public String getDocno() {
        return docno;
    }

    public void setDocno(String docno) {
        this.docno = docno;
    }

    public String getShortdocno() {
        return shortdocno;
    }

    public void setShortdocno(String shortdocno) {
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

    public int getEprice() {
        return eprice;
    }

    public void setEprice(int eprice) {
        this.eprice = eprice;
    }


    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }


}
