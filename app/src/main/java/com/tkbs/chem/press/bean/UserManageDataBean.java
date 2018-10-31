package com.tkbs.chem.press.bean;

/**
 * Created by Administrator on 2018/10/31.
 */
public class UserManageDataBean {

    /**
     * guid : 03B5781927A04588928CBEC03745E31E
     * username : 用户
     * job :
     * school :
     * faculty :
     * approved : 1
     * unapproved : 1
     * unpass : 1
     * sampleBookNumber : 2
     * giveBookNumber : 1
     */

    private String guid;
    private String username;
    private String job;
    private String school;
    private String faculty;
    private int approved;
    private int unapproved;
    private int unpass;
    private int sampleBookNumber;
    private int giveBookNumber;
    private long date;
    private int state;


    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public int getUnapproved() {
        return unapproved;
    }

    public void setUnapproved(int unapproved) {
        this.unapproved = unapproved;
    }

    public int getUnpass() {
        return unpass;
    }

    public void setUnpass(int unpass) {
        this.unpass = unpass;
    }

    public int getSampleBookNumber() {
        return sampleBookNumber;
    }

    public void setSampleBookNumber(int sampleBookNumber) {
        this.sampleBookNumber = sampleBookNumber;
    }

    public int getGiveBookNumber() {
        return giveBookNumber;
    }

    public void setGiveBookNumber(int giveBookNumber) {
        this.giveBookNumber = giveBookNumber;
    }
}
