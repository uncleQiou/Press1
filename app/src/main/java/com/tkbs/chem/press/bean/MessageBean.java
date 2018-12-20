package com.tkbs.chem.press.bean;

/**
 * 作者    qyl
 * 时间    2018/12/15 14:22
 * 文件    Press
 * 描述
 */
public class MessageBean {

    /**
     * id : 1
     * guid : 3BA9C382FC074F9582859E91E6C846F6
     * userType : 2
     * userGuid : 45357FD4C004493D9E65BE6628F6BF4A
     * title : 注册
     * content : xxx0002注册了
     * messageType : 6
     * remark :
     * state :
     * createUser :
     * createDate : 1544854901000
     * modifyUser :
     * modifyDate :
     * extInt : 0
     * extStr :
     */

    private int id;
    private String guid;
    private int userType;
    private String userGuid;
    private String title;
    private String content;
    private int messageType;
    private String remark;
    private String state;
    private String createUser;
    private long createDate;
    private String modifyUser;
    private String modifyDate;
    private int extInt;
    private String extStr;
    private String relationGuid;

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

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getUserGuid() {
        return userGuid;
    }

    public void setUserGuid(String userGuid) {
        this.userGuid = userGuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    public int getExtInt() {
        return extInt;
    }

    public void setExtInt(int extInt) {
        this.extInt = extInt;
    }

    public String getExtStr() {
        return extStr;
    }

    public void setExtStr(String extStr) {
        this.extStr = extStr;
    }

    public String getRelationGuid() {
        return relationGuid;
    }

    public void setRelationGuid(String relationGuid) {
        this.relationGuid = relationGuid;
    }
}
