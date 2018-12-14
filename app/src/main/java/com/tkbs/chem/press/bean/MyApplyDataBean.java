package com.tkbs.chem.press.bean;

/**
 * 作者    qyl
 * 时间    2018/12/14 16:39
 * 文件    Press
 * 描述
 */
public class MyApplyDataBean {

    /**
     * documentName : 现代分子光化学（1）原理篇12
     * state : 0
     * createDate : 2018-11-28 09:43:24
     */

    private String documentName;
    private int state;
    private String createDate;

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
