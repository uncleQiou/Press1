package com.tkbs.chem.press.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/6/16.
 */
public class RechargeResult {
    private String alipay;
    private double payAmount;
    private WeChat weChat;

    public String getAlipay() {
        return alipay;
    }

    public void setAlipay(String alipay) {
        this.alipay = alipay;
    }

    public double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(double payAmount) {
        this.payAmount = payAmount;
    }

    public WeChat getWeChat() {
        return weChat;
    }

    public void setWeChat(WeChat weChat) {
        this.weChat = weChat;
    }

    public static class WeChat {
        private String appid;
        private String noncestr;
        @SerializedName("package")
        private String packageX;
        private String partnerid;
        private String prepayid;
        private String timestamp;
        private String sign;

        public String getAppId() {
            return appid;
        }

        public void setAppId(String appId) {
            this.appid = appId;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getPartnerId() {
            return partnerid;
        }

        public void setPartnerId(String partnerId) {
            this.partnerid = partnerId;
        }

        public String getPrepayId() {
            return prepayid;
        }

        public void setPrepayId(String prepayId) {
            this.prepayid = prepayId;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }

}
