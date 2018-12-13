package com.tkbs.chem.press.util;


import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.tkbs.chem.press.base.BaseApplication;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;


/**
 * Created by Administrator on 2017/8/30.
 */
public class UiUtils {
    public static boolean isNullorEmpty(String str) {
        if (str == null || str.trim().length() == 0) {
            return true;
        }
        return false;
    }

    /***
     * var S4 = function () {
     * return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
     * };
     * return (S4() + S4() + "-" + S4() + "-" + S4() + "-" + S4() + "-" + S4() + S4() + S4());
     */
    public static String S4() {

        return Double.toHexString((1 + Math.random()) * 0x10000).toString().substring(4, 8);
    }

    public static String random() {

        return (S4() + S4() + "-" + S4() + "-" + S4() + "-" + S4() + "-" + S4() + S4() + S4());
    }

    /**
     * 图片路径替换
     *
     * @param string
     * @return
     */
    public static String picUrlReplace(String string) {
        if (isNullorEmpty(string)) {
            return "";
        } else {
            return string.replace("\\", "/");
        }
    }

    /**
     * 获取用户信息
     *
     * @return
     */
//    public static UserMessageDataBean obtainUserInfo() {
//        UserMessageDataBean userInfo = new UserMessageDataBean();
//        userInfo.setOrgId(BaseApplication.preferences.getString("orgId", ""));
//        userInfo.setUserId(BaseApplication.preferences.getString("userId", ""));
//        userInfo.setNickName(BaseApplication.preferences.getString("nickName", ""));
//        userInfo.setOrgName(BaseApplication.preferences.getString("orgName", ""));
//        userInfo.setUserName(BaseApplication.preferences.getString("userNumber", ""));
//        userInfo.setUserKey(BaseApplication.preferences.getString("userKey", ""));
//        userInfo.setPhoneModel(BaseApplication.preferences.getString("mobile", ""));
//        userInfo.setUserTicket(BaseApplication.preferences.getString("userTicket", ""));
//        userInfo.setPublishOrg(BaseApplication.preferences.getBoolean("isPublishOrg", false));
//        return userInfo;
//    }

    /**
     * 根据dip值转化成px值
     *
     * @param context
     * @param dip
     * @return
     */
    public static int dipToPix(Context context, int dip) {
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources().getDisplayMetrics());
        return size;
    }

    //屏幕宽度（像素）
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 完整图片路径
     *
     * @param path
     * @return
     */
    public static String ImageMachining(String path) {
        return "";
//        return BaseApplication.imgBasePath + UiUtils.picUrlReplace(path);
    }

    /**
     * html 文本
     *
     * @param text
     * @return
     */
    public static CharSequence getHtmlText(String text) {
        CharSequence charSequence;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            charSequence = Html.fromHtml(text,
                    Html.FROM_HTML_MODE_LEGACY);
        } else {
            charSequence = Html.fromHtml(text);
        }
        return charSequence;
    }


    /**
     * 隐藏键盘
     *
     * @param context
     */
    public static void hiddenKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 获取手机唯一标识
     *
     * @param context
     * @return
     */
    public synchronized static String getid(Context context) {
        TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String ID = TelephonyMgr.getDeviceId();
        return ID;
    }

    public static String getMIMEType(File file) {

        String type = "*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
    /* 获取文件的后缀名 */
        String fileType = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (fileType == null || "".equals(fileType)) {
            return type;
        }
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) {
            if (fileType.equals(MIME_MapTable[i][0])) {
                type = MIME_MapTable[i][1];
            }
        }
        return type;
    }

    // android  文件类型
    private static final String[][] MIME_MapTable = {
            //{后缀名，    MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".tkbs", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".prop", "text/plain"},
            {".rar", "application/x-rar-compressed"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            //{".xml",    "text/xml"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/zip"},
            {"", "*/*"}
    };

    public static byte[] readTkbs(String guid, Integer pageNum) {
        try {
            InputStream encodeStream = null;
            //资源GUID
//            String documentGuid = "34DBCCE472A14205A2431AF22538644D";
            String documentGuid = guid;
            //页码
            Integer readNum = pageNum;
//            Integer readNum = 5;
            File file = new File(Config.CIP_FILE_PATH + File.separator + "resource");
            if (!file.exists()) {
                file.mkdir();
            }
            //查询资源对象
            String basePath = Config.CIP_FILE_PATH + documentGuid + ".tkbs";
            String indexTemp = Config.CIP_FILE_PATH + "resource" +
                    File.separator + documentGuid + "_temp.xml";
            String indexFile = Config.CIP_FILE_PATH + "resource" +
                    File.separator + documentGuid + "_index.xml";
            File tkbsIndex = new File(indexTemp);
            if (!tkbsIndex.exists()) {
                ReadTkbsFile.decodeIndex(basePath, indexTemp, Config.FILE_KEY);
                // 加密文件
                DESUtil.encryptFile(indexTemp, Config.CIP_FILE_PATH + "resource", documentGuid + "_index.xml",
                        Config.SUBJECT_ENCRYPT_KEY, null);
                delete_file(new File(indexTemp));
            }
            // 解密文件返回文件流:
            InputStream inputStream = DESUtil.decryptFile(indexFile, Config.SUBJECT_ENCRYPT_KEY, false);
            // 解析索引文件获得文件的起始位置和文件长度 根据PageNum来查找
            //查询阅读历史
            Integer count = readNum;
            // xpath表达式进行查询
            Map<String, String> index = ParseTkbsFileIndex.textPath(inputStream, count);

            // 得到要显示文件的坐标
            // 103152
            int htmlPos = Integer.parseInt(index.get("htmlPos"));
            // 539
            // int htmlLen = Integer.parseInt(index.get("htmlLen"));
            // 544
            int htmlEncodeLen = Integer.parseInt(index.get("htmlEncodeLen"));

            // 从开始指定的位置读取: 并且解密html文件:
            FileInputStream inputStream2 = null;
            byte[] data = new byte[htmlEncodeLen];

            inputStream2 = new FileInputStream(basePath);
            // 指定开始位置:
            inputStream2.skip(htmlPos);
            inputStream2.read(data, 0, htmlEncodeLen);
            inputStream2.close();
            // 解密文件生成新的文件:
            InputStream inputStr = new ByteArrayInputStream(data);

            encodeStream = DESUtil.doDecryptTkbsFile(inputStr, Config.FILE_KEY);
            // 将数据流写入到浏览器页面：
            byte[] btHtm = ReadTkbsFile.readStream(encodeStream);
            return btHtm;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 递归文件删除
     *
     * @param file
     */
    public static void delete_file(File file) {
        if (!file.exists()) {
            return;
        }
        // 如果是文件直接删掉
        if (!file.isDirectory()) {
            boolean result = file.delete();
            if (!result) {
                // 系统进行资源强制回收
                System.gc();
                file.delete();
            }
        } else {// 如果是文件夹则先删除子文件
            File[] files = file.listFiles();
            // 没有子文件
            if (files == null || files.length == 0) {
                boolean result = file.delete();
                if (!result) {
                    // 系统进行资源强制回收
                    System.gc();
                    file.delete();
                }
            } else {
                for (File f : files) {
                    // 删除子文件
                    delete_file(f);
                }
                boolean result = file.delete();
                if (!result) {
                    // 系统进行资源强制回收
                    System.gc();
                    file.delete();
                }
            }
        }
    }


}
