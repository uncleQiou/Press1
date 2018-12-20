package com.tkbs.chem.press.bean;

/**
 * 作者    qyl
 * 时间    2018/12/20 10:37
 * 文件    Press
 * 描述
 */
public class CreateOrderDataBean {

    /**
     * isBuy : false
     * unit : CIP币
     * virtualPrice : 5000
     * accountBalance : 0
     */

    private boolean isBuy;
    private String unit;
    private int virtualPrice;
    private int accountBalance;

    public boolean isIsBuy() {
        return isBuy;
    }

    public void setIsBuy(boolean isBuy) {
        this.isBuy = isBuy;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getVirtualPrice() {
        return virtualPrice;
    }

    public void setVirtualPrice(int virtualPrice) {
        this.virtualPrice = virtualPrice;
    }

    public int getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(int accountBalance) {
        this.accountBalance = accountBalance;
    }
}
