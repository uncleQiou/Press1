package com.tkbs.chem.press.util;

import android.content.Context;

import com.google.gson.Gson;
import com.tkbs.chem.press.base.BaseApplication;


public class WebUserInfor {


    private Context hh;
    private String string;
    String key = "xzwAndroid";
    private String decrypt;
    private String user;

    public static WebUserInfor builder(Context hh) {

        return new WebUserInfor(hh);
    }


    private WebUserInfor(Context con) {
        hh = con;
        People p = new People();
        String userName = BaseApplication.preferences.getString("login_name", "");
        String userKey = BaseApplication.preferences.getString(
                "PASSWORD", "");
        p.setPassword(userKey);
        p.setUserName(userName);
        Gson gson = new Gson();
        string = gson.toJson(p);
        user = gson.toJson(p);


        try {
            decrypt = DESUtil.decrypt(string, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getString() {

        return decrypt;
    }

    public String getUserinfor() {


        return user;

    }


    class People {
        String userName;
        String Password;

        public String getPassword() {
            return Password;
        }

        public void setPassword(String password) {
            Password = password;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
}
