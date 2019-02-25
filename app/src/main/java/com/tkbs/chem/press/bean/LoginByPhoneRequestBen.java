package com.tkbs.chem.press.bean;

/**
 * 作者    qyl
 * 时间    2019/2/21 14:25
 * 文件    Press
 * 描述
 */
public class LoginByPhoneRequestBen {
    private String hash;
    private String tamp;
    private String msgNum;
    private String phone;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getTamp() {
        return tamp;
    }

    public void setTamp(String tamp) {
        this.tamp = tamp;
    }

    public String getMsgNum() {
        return msgNum;
    }

    public void setMsgNum(String msgNum) {
        this.msgNum = msgNum;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
