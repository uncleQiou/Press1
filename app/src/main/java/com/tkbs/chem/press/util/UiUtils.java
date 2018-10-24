package com.tkbs.chem.press.util;


import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.tkbs.chem.press.base.BaseApplication;


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

}
