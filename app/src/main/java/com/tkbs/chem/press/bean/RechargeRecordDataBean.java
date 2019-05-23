package com.tkbs.chem.press.bean;

import java.util.List;

/**
 * 作者    qyl
 * 时间    2018/12/20 13:44
 * 文件    Press
 * 描述
 */
public class RechargeRecordDataBean {


    /**
     * yearMonth : 2019-05
     * year : 2019
     * month : 5
     * list : [{"pay_date":"2019-05-23","pay_price":0.01,"fill_value":1,"guid":"2F57916FA5A344A7AEB45B01E82C6CDC","pay_type":10,"id":76},{"pay_date":"2019-05-23","pay_price":0.01,"fill_value":1,"guid":"DA5F6F968CE440CEB11573E1980C7266","pay_type":20,"id":77},{"pay_date":"2019-05-23","pay_price":3,"fill_value":300,"guid":"3E14A8922D53412090548379D3603666","pay_type":10,"id":70},{"pay_date":"2019-05-23","pay_price":1,"fill_value":1,"guid":"EA31AA06C3F6426689813372C9628FD7","pay_type":10,"id":72},{"pay_date":"2019-02-19","pay_price":388,"fill_value":38800,"guid":"F3C4F742AACE4FA7AA850124BE85AF40","pay_type":50,"id":20},{"pay_date":"2019-02-19","pay_price":488,"fill_value":48800,"guid":"6B17A2668899402597D8228C5ACB5874","pay_type":50,"id":21},{"pay_date":"2019-02-19","pay_price":388,"fill_value":38800,"guid":"546E00B347C748EC878D9D62E489C2F4","pay_type":50,"id":18},{"pay_date":"2019-02-19","pay_price":388,"fill_value":38800,"guid":"1AAD5ED4E6EA4E5E8AC9E37ECFC4CC66","pay_type":50,"id":19},{"pay_date":"2019-02-19","pay_price":1,"fill_value":100,"guid":"E991EC4E15884B9AA97849D8C73CF803","pay_type":10,"id":17},{"pay_date":"2019-02-19","pay_price":388,"fill_value":38800,"guid":"F39771E894D14EAEBB1C803F60B0C70D","pay_type":50,"id":16}]
     * totalPrice : 2614.02
     */

    private String yearMonth;
    private int year;
    private int month;
    private double totalPrice;
    /**
     * pay_date : 2019-05-23
     * pay_price : 0.01
     * fill_value : 1
     * guid : 2F57916FA5A344A7AEB45B01E82C6CDC
     * pay_type : 10
     * id : 76
     */

    private List<ListBean> list;

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private String pay_date;
        private double pay_price;
        private int fill_value;
        private String guid;
        private int pay_type;
        private int id;

        public String getPay_date() {
            return pay_date;
        }

        public void setPay_date(String pay_date) {
            this.pay_date = pay_date;
        }

        public double getPay_price() {
            return pay_price;
        }

        public void setPay_price(double pay_price) {
            this.pay_price = pay_price;
        }

        public int getFill_value() {
            return fill_value;
        }

        public void setFill_value(int fill_value) {
            this.fill_value = fill_value;
        }

        public String getGuid() {
            return guid;
        }

        public void setGuid(String guid) {
            this.guid = guid;
        }

        public int getPay_type() {
            return pay_type;
        }

        public void setPay_type(int pay_type) {
            this.pay_type = pay_type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
