package com.tkbs.chem.press.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * TimeUtils
 *
 * @author
 */
public class TimeUtils {

    //    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
    public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat DATE_FORMAT_DATE_Hour = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT_DATE_Hour_END = new SimpleDateFormat("yyyy-MM-dd HH");

    private TimeUtils() {
        throw new AssertionError();
    }

    /**
     * long time to string
     *
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DATE_FORMAT_DATE);
    }

    public static String getCurrentTimeInStringpm() {
        return getTime(getCurrentTimeInLong(), DATE_FORMAT_DATE);
    }


    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }

    public static long stringToLong(String strTime, String formatType)
            throws ParseException {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }

    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    public static long dateToLong(Date date) {
        return date.getTime();
    }

    /**
     * 字符串转日期，格式为："yyyy-MM-dd"
     *
     * @param dateStr
     * @return
     */
    public static Date formatDate(String dateStr) {
        Date result = null;
        try {
            result = DATE_FORMAT_DATE.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据出生日期计算年龄
     *
     * @param dateStr
     * @return
     */
    public static String intervalYear(String dateStr) {
        if (null != dateStr) {
            if (dateStr.length() > 0) {
                Date dateForm = TimeUtils.formatDate(dateStr);
                Calendar from = Calendar.getInstance();
                from.setTime(dateForm);
                Date dateTo = new Date(System.currentTimeMillis());
                Calendar to = Calendar.getInstance();
                to.setTime(dateTo);
                int fromYear = from.get(Calendar.YEAR);
                int toYear = to.get(Calendar.YEAR);
                int age = toYear - fromYear + 1;
                return age + "岁";
            } else {
                return "";
            }
        } else {
            return "";
        }
    }
}
