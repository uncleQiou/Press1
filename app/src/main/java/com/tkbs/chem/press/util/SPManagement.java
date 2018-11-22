package com.tkbs.chem.press.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.Map;


public class SPManagement {

    private static Context mContext;

    private static SPUtil spUtil;

    public static void init(Context ctx) {
        mContext = ctx;
    }

    public static SPUtil getSPUtilInstance(String fileName) {
        if (null == mContext) {
            throw new NullPointerException("此类没有进行初始化");
        }
        if (null == spUtil) {
            spUtil = SPUtil.getInstance(mContext, fileName);
        }
        return spUtil;
    }


    /**
     * 配置IO类
     */
    public static class SPUtil {
        private static Context mContext;
        private static String fileName;
        private SharedPreferences saveInfo;
        private SharedPreferences.Editor saveEditor;


        private SPUtil() {
            if (null == mContext) {
                throw new NullPointerException("此类没有进行初始化");
            }
            if (TextUtils.isEmpty(fileName)) {
                throw new NullPointerException("此类没有进行初始化");
            }
            saveInfo = mContext.getSharedPreferences(fileName,
                    Context.MODE_PRIVATE);
            saveEditor = saveInfo.edit();
        }

        private static SPUtil getInstance(Context ctx, String name) {
            mContext = ctx;
            fileName = name;
            return new SPUtil();
        }

        /**
         * 删除全部数据
         *
         * @return
         */
        public boolean clearAllItem() {
            saveEditor.clear();
            return saveEditor.commit();

        }

        // --------- string ----------
        public void putString(String key, String value) {
            // 搜索内容为空 不添加记录
            if (null == key || "".equals(key)) {
                return;
            }
            //   判断是否已经添加过
            String searched = getString(key, "");
            if ("".equals(searched)) {
                saveEditor.putString(key, value);
                saveEditor.apply();
            }
        }

        public String getString(String key, String defaultValue) {
            return saveInfo.getString(key, defaultValue);
        }

        //获取所有数据
        public Map<String, ?> getAll() {
            Map<String, ?> all = saveInfo.getAll();

            return all;
        }

        /**
         * s删除单个数据
         *
         * @param key
         * @param key
         */

        public void remove(String key) {


            SharedPreferences.Editor remove = saveEditor.remove(key);
            remove.apply();
        }


        // --------- float ----------
        public void putFloat(String key, Float value) {
            saveEditor.putFloat(key, value);
            saveEditor.apply();
        }

        public Float getFloat(String key, Float defaultValue) {
            return saveInfo.getFloat(key, defaultValue);
        }

        // --------- boolean ----------
        public void putBoolean(String key, boolean value) {
            saveEditor.putBoolean(key, value);
            saveEditor.apply();
        }

        public boolean getBoolean(String key, boolean defaultValue) {
            return saveInfo.getBoolean(key, defaultValue);
        }

        // ---------- int ---------
        public void putInt(String key, int value) {
            saveEditor.putInt(key, value);
            saveEditor.apply();
        }

        public int getInt(String key, int defaultValue) {
            return saveInfo.getInt(key, defaultValue);
        }

        // ---------- long ----------
        public void putLong(String key, long value) {
            saveEditor.putLong(key, value);
            saveEditor.apply();
        }

        public long getLong(String key, long defaultValue) {
            return saveInfo.getLong(key, defaultValue);
        }


    }
}
