package com.tkbs.chem.press.bean;

/**
 * 作者    qyl
 * 时间    2019/2/21 16:10
 * 文件    Press
 * 描述
 */
public class ShareDataBean {


    /**
     * cover : http://192.168.1.105:8580/webFile/resource/63E59BED62FB4002BC69D7EE0066CE71/cover.jpg
     * title : 有机合成原理与工艺
     * introduction : 本书从有机化学基础知识入手，系统介绍了有机合成的相关机理，并...
     * url : http://192.168.1.105:8281/app/share/63E59BED62FB4002BC69D7EE0066CE71
     */

    private String cover;
    private String title;
    private String introduction;
    private String url;

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
