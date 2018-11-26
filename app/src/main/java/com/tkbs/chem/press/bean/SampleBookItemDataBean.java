package com.tkbs.chem.press.bean;

/**
 * 作者    qyl
 * 时间    2018/11/9 10:43
 * 文件    Press
 * 描述
 */
public class SampleBookItemDataBean {


    /**
     * shortdocno : 25169
     * author : 吴建一、缪程平、宗乾收、刘丹 编著
     * publish_time : 2015-10-01
     * price : 35
     * degree : 59
     * guid : 110C836D1F3247E79342D961AD3C282B
     * id : 309
     * docno : 9787122251695
     * title : 有机合成原理与工艺
     * metadata_xml : <info><edited>魏巍、赵玉清</edited><shortdocno>25169</shortdocno><author>吴建一、缪程平、宗乾收、刘丹 编著</author><authorintroduction></authorintroduction><cip>(2015)第218113号</cip><docno>9787122251695</docno><title>有机合成原理与工艺</title><threelevel>本科化学</threelevel><pagenum>181</pagenum><ztf>①O621.3②TQ02</ztf><eventkeyword></eventkeyword><secondlevel>本科</secondlevel><onelevel>教材</onelevel><price>35</price><series></series><publishtime>2015-10-01 00:00:00</publishtime><editionnum>1</editionnum><wordnum>228</wordnum><subjectkeyword></subjectkeyword><longdocno>978-7-122-25169-5</longdocno><department>高教第二出版分社</department><keyword>有机合成;原理;工艺;</keyword><introduction>本书从有机化学基础知识入手，系统介绍了有机合成的相关机理，并将有机合成理论与实践相结合，系统性较强，便于学生自学和“学习共同体”课堂教学改革的试行。全书共分9章，内容包括：有机合成的历史、有机合成基础知识、碳-碳单键的形成、碳碳双键的形成、氧化反应、还原反应、重排反应、杂环的形成和有机合成设计。本书既可作为高等院校化学、化工、制药等专业有机合成教学用书，也可作为从事相关科学研究的人员参考用书或自学用书。</introduction><thematicwords></thematicwords></info>
     * <p>
     * cover
     */

    private String shortdocno;
    private String author;
    private String publish_time;
    private double price;
    private int degree;
    private String guid;
    private int id;
    private String docno;
    private String title;
    private String metadata_xml;
    private String cover;
    private boolean isChecked;


    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getShortdocno() {
        return shortdocno;
    }

    public void setShortdocno(String shortdocno) {
        this.shortdocno = shortdocno;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDocno() {
        return docno;
    }

    public void setDocno(String docno) {
        this.docno = docno;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMetadata_xml() {
        return metadata_xml;
    }

    public void setMetadata_xml(String metadata_xml) {
        this.metadata_xml = metadata_xml;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}
