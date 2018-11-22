package com.tkbs.chem.press.bean;

import java.util.List;

/**
 * 作者    qyl
 * 时间    2018/11/21 14:51
 * 文件    Press
 * 描述
 */
public class ClassifyRequestBean {
    List<String> catagoryGuids;
    String content;

    public List<String> getCatagoryGuids() {
        return catagoryGuids;
    }

    public void setCatagoryGuids(List<String> catagoryGuids) {
        this.catagoryGuids = catagoryGuids;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
