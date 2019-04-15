package com.tkbs.chem.press.bean;

import java.util.ArrayList;

/**
 * 作者    qyl
 * 时间    2018/12/22 9:47
 * 文件    Press
 * 描述
 */
public class SBookAllStatisticsDataBena {

    /**
     * name : 2018年第51周
     * count : 4
     */

    private ArrayList<StatisticsCoordinateDataBean> dateTimeData;
    /**
     * name : 电子科技大学
     * count : 1
     */

    private ArrayList<StatisticsCoordinateDataBean> schoolData;
    /**
     * name : 用户
     * count : 14
     */

    private ArrayList<StatisticsCoordinateDataBean> teacherData;
    private ArrayList<String> schoolList;

    private ProgressBarData data;

    public ProgressBarData getData() {
        return data;
    }

    public void setData(ProgressBarData data) {
        this.data = data;
    }

    public ArrayList<StatisticsCoordinateDataBean> getDateTimeData() {
        return dateTimeData;
    }

    public void setDateTimeData(ArrayList<StatisticsCoordinateDataBean> dateTimeData) {
        this.dateTimeData = dateTimeData;
    }

    public ArrayList<StatisticsCoordinateDataBean> getSchoolData() {
        return schoolData;
    }

    public void setSchoolData(ArrayList<StatisticsCoordinateDataBean> schoolData) {
        this.schoolData = schoolData;
    }

    public ArrayList<StatisticsCoordinateDataBean> getTeacherData() {
        return teacherData;
    }

    public void setTeacherData(ArrayList<StatisticsCoordinateDataBean> teacherData) {
        this.teacherData = teacherData;
    }

    public ArrayList<String> getSchoolList() {
        return schoolList;
    }

    public void setSchoolList(ArrayList<String> schoolList) {
        this.schoolList = schoolList;
    }


    public class ProgressBarData {

        /**
         * total : 321
         * count : 3
         */

        private int total;
        private int count;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }

}
