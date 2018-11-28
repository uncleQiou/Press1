package com.tkbs.chem.press.bean;

/**
 * Created by Administrator on 2018/10/30.
 */
public class SampleBookManageDataBean {


    /**
     * userGuid : 03B5781927A04588928CBEC03745E31E
     * username : 用户
     * job :
     * school :
     * faculty :
     * date : null
     * state : null
     * approvedNumber : 0
     * unApprovedNumber : 6
     * unPassNumber : 0
     * sampleBookNumber : null
     * giveBookNumber : null
     */

    private String userGuid;
    private String username;
    private String job;
    private String school;
    private String faculty;
    private long date;
    private int state;
    private int approvedNumber;
    private int unApprovedNumber;
    private int unPassNumber;
    private Object sampleBookNumber;
    private Object giveBookNumber;

    public String getUserGuid() {
        return userGuid;
    }

    public void setUserGuid(String userGuid) {
        this.userGuid = userGuid;
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

    public long getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getApprovedNumber() {
        return approvedNumber;
    }

    public void setApprovedNumber(int approvedNumber) {
        this.approvedNumber = approvedNumber;
    }

    public int getUnApprovedNumber() {
        return unApprovedNumber;
    }

    public void setUnApprovedNumber(int unApprovedNumber) {
        this.unApprovedNumber = unApprovedNumber;
    }

    public int getUnPassNumber() {
        return unPassNumber;
    }

    public void setUnPassNumber(int unPassNumber) {
        this.unPassNumber = unPassNumber;
    }

    public Object getSampleBookNumber() {
        return sampleBookNumber;
    }

    public void setSampleBookNumber(Object sampleBookNumber) {
        this.sampleBookNumber = sampleBookNumber;
    }

    public Object getGiveBookNumber() {
        return giveBookNumber;
    }

    public void setGiveBookNumber(Object giveBookNumber) {
        this.giveBookNumber = giveBookNumber;
    }
}
