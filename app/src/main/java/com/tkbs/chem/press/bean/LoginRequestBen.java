package com.tkbs.chem.press.bean;

/**
 * Created by Administrator on 2018/5/25.
 */
public class LoginRequestBen {
    /**
     * {"loginName":"20180101","password":"1"}
     */
    private String loginName;
    private String password;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
