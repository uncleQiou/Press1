package com.tkbs.chem.press.bean;

/**
 * 作者    qyl
 * 时间    2018/12/15 15:57
 * 文件    Press
 * 描述
 */
public class MessageUserBean {

    /**
     * date : 1238712873182638172387
     * realName : aaa1
     * state : 0
     * job : 教学主任
     */

    private long date;
    private String realName;
    private int state;
    private String job;

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
