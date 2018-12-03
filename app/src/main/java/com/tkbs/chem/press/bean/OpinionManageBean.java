package com.tkbs.chem.press.bean;

import java.util.List;

/**
 * 作者    qyl
 * 时间    2018/11/30 9:39
 * 文件    Press
 * 描述
 */
public class OpinionManageBean {

    /**
     * createDate : 2018-11-29 10:32:29
     * createUser : 我的意见
     * content : 我觉得你们应该改进一下
     * type : 2
     * opinionDtoList : [{"createDate":"2018-11-29 10:47:38","createUser":"","content":"您放心。我们会改进的","type":1,"opinionDtoList":[]}]
     */

    private int opinionId ;
    private String createDate;
    private String createUser;
    private String content;
    private int type;

    public int getOpinionId() {
        return opinionId;
    }

    public void setOpinionId(int opinionId) {
        this.opinionId = opinionId;
    }

    /**
     * createDate : 2018-11-29 10:47:38
     * createUser :
     * content : 您放心。我们会改进的
     * type : 1
     * opinionDtoList : []
     */


    private List<OpinionManageBean> opinionDtoList;

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<OpinionManageBean> getOpinionDtoList() {
        return opinionDtoList;
    }

    public void setOpinionDtoList(List<OpinionManageBean> opinionDtoList) {
        this.opinionDtoList = opinionDtoList;
    }


}
