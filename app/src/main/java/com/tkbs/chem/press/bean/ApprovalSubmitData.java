package com.tkbs.chem.press.bean;

/**
 * 作者    qyl
 * 时间    2018/11/4 12:33
 * 文件    Press
 * 描述
 */
public class ApprovalSubmitData {

    private String content;
    private String resGuid;
    private String userGuid;
    // 几个月
    private int  timeLimit;
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

    public String getResGuid() {
        return resGuid;
    }

    public void setResGuid(String resGuid) {
        this.resGuid = resGuid;
    }

    public String getUserGuid() {
        return userGuid;
    }

    public void setUserGuid(String userGuid) {
        this.userGuid = userGuid;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }
}
