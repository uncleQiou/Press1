package com.tkbs.chem.press.util;

import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2017/8/28.
 */
public class Config {
    public static final String TOPIC = " ";
    /**
     * Xjl
     */
//    public static String API_SERVER = "http://192.168.113.146:8281/app/";
    /**
     * Zmy
     */
    public static String API_SERVER = "http://192.168.113.147:8281/app/";
    /**
     * 251
     */
//    public static String API_SERVER = "http://192.168.1.251:8281/app/";

    /**
     *  TKBS url
     */
//    public static String API_SERVER_BOOK = "http://221.122.68.70:8097/";

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
     * 1、超级管理员 2、业务员 3、教师 4、普通用户 5、游客
     */
    public static final String MEMBER_TYPE = "member_type";
    /**
     * 支付宝 20 微信  10
     */
    public static final int ZFB_PAY_TYPE = 20;
    /***
     * 账户切换
     */
    public static final int ACCOUNT_SWITCHING = 1990;

    /**
     * 文件下载目标路径
     */
    public static final String CIP_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "CIP" + File.separator;

    //// KEY为自定义秘钥
    public static final String  SMS_CUSTOM_KEY = "smsCustomKey";

    // 试读页数
    public static final Integer DEFAULT_READ_PAGE = 17;

    // 资源加密密钥
    public static String SUBJECT_ENCRYPT_KEY = "1234567890";
    public static String FILE_KEY = "12345678";
}
