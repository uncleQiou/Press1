package com.tkbs.chem.press.bean;

import java.util.List;

/**
 * 作者    qyl
 * 时间    2018/12/20 9:43
 * 文件    Press
 * 描述
 */
public class RechargeConfigDataBean {


    /**
     * mapList : [{"payPrice":"3","fillValue":300},{"payPrice":"8","fillValue":800},{"payPrice":"12","fillValue":1200},{"payPrice":"18","fillValue":1800},{"payPrice":"25","fillValue":2500},{"payPrice":"30","fillValue":3000},{"payPrice":"50","fillValue":5000},{"payPrice":"88","fillValue":8800},{"payPrice":"98","fillValue":9800}]
     * fillName : CIP币
     * reminder : 1、充值阅读权限仅限本书城使用。 2、使用支付宝或微信方式进行充值，1元兑换100点。 3、充值后，您可至【个人中心】—【我的账户】查看账户余额。 4、若充值后账户余额长时间无变化，请记录您的用户名15011242892后致电书城客服4008813311。
     */

    private String fillName;
    private String reminder;
    /**
     * payPrice : 3
     * fillValue : 300
     */

    private List<MapListBean> mapList;

    public String getFillName() {
        return fillName;
    }

    public void setFillName(String fillName) {
        this.fillName = fillName;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public List<MapListBean> getMapList() {
        return mapList;
    }

    public void setMapList(List<MapListBean> mapList) {
        this.mapList = mapList;
    }

    public static class MapListBean {
        private String payPrice;
        private int fillValue;

        public String getPayPrice() {
            return payPrice;
        }

        public void setPayPrice(String payPrice) {
            this.payPrice = payPrice;
        }

        public int getFillValue() {
            return fillValue;
        }

        public void setFillValue(int fillValue) {
            this.fillValue = fillValue;
        }
    }
}
