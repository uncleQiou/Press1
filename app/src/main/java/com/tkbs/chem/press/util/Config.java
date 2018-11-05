package com.tkbs.chem.press.util;

/**
 * Created by Administrator on 2017/8/28.
 */
public class Config {
    public static final String TOPIC = "topic";
    /**
     * Xjl
     */
//    public static String API_SERVER = "http://192.168.1.154:8281/app/";
    /**
     * Zmy
     */
    public static String API_SERVER = "http://192.168.1.153:8281/app/";
    /**
     * 251
     */
//    public static String API_SERVER = "http://192.168.1.251:8281/app/";


    /**
     * String
     */
    public static final String USERNAME = "username";
    public static final String USERKRY = "userKey";
    public static final String WX_APP_ID = "wx444955314a8cc03d";
    /**
     * 查看word缓存路径
     */
    public static String BASE_WORD_FILE_DIR = "/sdcard/xzyanxue/library";
    /**
     * 图片缓存文件夹
     */
    public static String BASE_WORD_FILE = "xzyanxue/library";
    /**
     * 上传文件的BaseUrl
     */
    public static String YX_UPLOAD_URL = API_SERVER + "phone/resource/";

    /**
     * 保存搜索的的值
     */
    public static final String SAVEDTAB = "savedTab";
    /**
     * 用户信息保存Key
     */
    public static final String LOGIN_NAME = "login_name";
    public static final String PASSWORD = "PASSWORD";
    public static final String NICK_NAME = "nick_name";
    public static final String REAL_NAME = "real_name";
    public static final String WORKPHONE = "workphone";
    public static final String PHONE = "phone";
    /**
     * 1、超级管理员 2、业务员 3、教师 4、游客
     */
    public static final String MEMBER_TYPE = "member_type";

}
