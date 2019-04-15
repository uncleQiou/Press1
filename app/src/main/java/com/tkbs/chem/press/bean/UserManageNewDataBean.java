package com.tkbs.chem.press.bean;

import java.util.List;

/**
 * 作者    qyl
 * 时间    2019/1/7 15:41
 * 文件    Press
 * 描述
 */
public class UserManageNewDataBean {
    /**
     * giveBookBtnPermission : true
     * list : [{"userGuid":"5F8E734E63F44A2287324E4B4962EF71","username":"赵苗苗","job":"","school":"北京化工大学","faculty":"","date":1546847317231,"state":0,"approvedNumber":null,"unApprovedNumber":null,"unPassNumber":null,"sampleBookNumber":0,"giveBookNumber":1},{"userGuid":"856B186F550C4DB598EA4FA25D353D12","username":"hehehe","job":"","school":"hh","faculty":"","date":1546847317239,"state":0,"approvedNumber":null,"unApprovedNumber":null,"unPassNumber":null,"sampleBookNumber":5,"giveBookNumber":21}]
     */

    private boolean giveBookBtnPermission;
    /**
     * userGuid : 5F8E734E63F44A2287324E4B4962EF71
     * username : 赵苗苗
     * job :
     * school : 北京化工大学
     * faculty :
     * date : 1546847317231
     * state : 0
     * approvedNumber : null
     * unApprovedNumber : null
     * unPassNumber : null
     * sampleBookNumber : 0
     * giveBookNumber : 1
     */

    private List<UserManageDataBean> list;

    public boolean isGiveBookBtnPermission() {
        return giveBookBtnPermission;
    }

    public void setGiveBookBtnPermission(boolean giveBookBtnPermission) {
        this.giveBookBtnPermission = giveBookBtnPermission;
    }

    public List<UserManageDataBean> getList() {
        return list;
    }

    public void setList(List<UserManageDataBean> list) {
        this.list = list;
    }


    //private boolean giveBookBtnPermission;
    // private List<UserManageDataBean> list;


}
