package com.tkbs.chem.press.bean;

/**
 * 作者    qyl
 * 时间    2019/3/25 14:29
 * 文件    Press
 * 描述
 */
public class UpdateTeacherInfoBean {
    // 教师guid
    private String guid;
    // 学校
    private String organization;
    // 院系
    private String department;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
