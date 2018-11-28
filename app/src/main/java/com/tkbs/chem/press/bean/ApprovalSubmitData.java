package com.tkbs.chem.press.bean;

/**
 * 作者    qyl
 * 时间    2018/11/4 12:33
 * 文件    Press
 * 描述
 */
public class ApprovalSubmitData {

    private String content;
    private String guid;
    // 几个月
    private int  limitTime;
    // 0 通过
    private int isPass ;

    public int getIsPass() {
        return isPass;
    }

    public void setIsPass(int isPass) {
        this.isPass = isPass;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public int getLimitTime() {
        return limitTime;
    }

    public void setLimitTime(int limitTime) {
        this.limitTime = limitTime;
    }
}
