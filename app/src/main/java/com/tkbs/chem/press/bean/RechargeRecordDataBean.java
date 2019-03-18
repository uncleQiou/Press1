package com.tkbs.chem.press.bean;

import java.security.PrivateKey;
import java.util.List;

/**
 * 作者    qyl
 * 时间    2018/12/20 13:44
 * 文件    Press
 * 描述
 */
public class RechargeRecordDataBean {

    /**
     * list : [{"pay_price":1,"fill_value":100,"guid":"77F2890ABAF54A70B8A51B8CA44C4017","pay_type":50,"id":7},{"pay_price":12,"fill_value":1200,"guid":"117440D72522469A9525179BAEE58B89","pay_type":50,"id":8},{"pay_price":18,"fill_value":1800,"guid":"4C6D19916CC44A6EB8CD614EBF8E1FAE","pay_type":50,"id":9},{"pay_price":18,"fill_value":1800,"guid":"BB8A85F0A7FE4083ACE8DE54CDAAD9D9","pay_type":50,"id":10},{"pay_price":18,"fill_value":1800,"guid":"D67120838B4A4E9F9EF0A497C8C13DD4","pay_type":50,"id":11},{"pay_price":18,"fill_value":1800,"guid":"1A94B95C148E4F75907A1A99130E4056","pay_type":50,"id":12},{"pay_price":18,"fill_value":1800,"guid":"A7474D95FCEA40AFA16DD88E5E973CBF","pay_type":50,"id":13},{"pay_price":18,"fill_value":1800,"guid":"2799CB995A3F495E81E2F6DF26113F2F","pay_type":50,"id":14},{"pay_price":18,"fill_value":1800,"guid":"A38A9CB758664D8EAC51D090C5F2ACB7","pay_type":50,"id":15},{"pay_price":18,"fill_value":1800,"guid":"7B3D7AB5E0064AB6830136C2542DE07A","pay_type":50,"id":16}]
     * totalPrice : 157
     */
//    "yearMonth": "2019-02",
//            "year": 2019,
//            "month": 2,
    private int year;
    private int month;
    private String yearMonth;
    private int totalPrice;

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

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    /**
     * pay_price : 1
     * fill_value : 100
     * guid : 77F2890ABAF54A70B8A51B8CA44C4017
     * pay_type : 50
     * id : 7
     */


    private List<ListBean> list;

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private int pay_price;
        private int fill_value;
        private String guid;
        private int pay_type;
        private int id;
        private String pay_date;

        public String getPay_date() {
            return pay_date;
        }

        public void setPay_date(String pay_date) {
            this.pay_date = pay_date;
        }

        public int getPay_price() {
            return pay_price;
        }

        public void setPay_price(int pay_price) {
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
