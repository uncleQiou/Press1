package com.tkbs.chem.press.bean;

/**
 * Created by Administrator on 2018/10/26.
 */
public class BookCityResCatagory {

    /**
     * id : 1
     * parentId : 0
     * guid : 4883B1337532B1CBCE4620D5DD1CF256
     * documentCatagoryGuid : 291A958848BF4AA099098ACF3AF3F56D
     * code : 00020001
     * title : 科技
     * type : 1
     * level : 1
     * subexists : 1
     * showIndex : null
     * showNode : 1
     * ztfcode : null
     * remark : null
     * state : 1
     * createUser : null
     * createDate : null
     * modifyUser : null
     * modifyDate : null
     * extInt : null
     * extStr : null
     */

    private int id;
    private int parentId;
    private String guid;
    private String documentCatagoryGuid;
    private String code;
    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getDocumentCatagoryGuid() {
        return documentCatagoryGuid;
    }

    public void setDocumentCatagoryGuid(String documentCatagoryGuid) {
        this.documentCatagoryGuid = documentCatagoryGuid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
