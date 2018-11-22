package com.tkbs.chem.press.bean;

import java.util.List;

/**
 * 作者    qyl
 * 时间    2018/11/20 17:29
 * 文件    Press
 * 描述
 */
public class BarChartBean {


    /**
     * eFinType : 3
     * stFinDate : {"fVal":-21.7467,"sDtSecCode":"0001000063","sFinanceDesc":"净资产收益率-21.75%，行业排名100/101","stIndSTRank":{"iDtNum":101,"iRank":100},"vtDateValue":[{"fValue":-21.7467,"sYearMonth":"2018-03"},{"fValue":14.4348,"sYearMonth":"2017-12"},{"fValue":12.6504,"sYearMonth":"2017-09"},{"fValue":7.9092,"sYearMonth":"2017-06"},{"fValue":4.3995,"sYearMonth":"2017-03"}],"vtDateValueAvg":[{"fValue":0.95137,"sYearMonth":"2017-03"},{"fValue":3.50875,"sYearMonth":"2017-06"},{"fValue":4.62725,"sYearMonth":"2017-09"},{"fValue":1.38761,"sYearMonth":"2017-12"},{"fValue":0.61198,"sYearMonth":"2018-03"}]}
     */

    private int eFinType;
    /**
     * fVal : -21.7467
     * sDtSecCode : 0001000063
     * sFinanceDesc : 净资产收益率-21.75%，行业排名100/101
     * stIndSTRank : {"iDtNum":101,"iRank":100}
     * vtDateValue : [{"fValue":-21.7467,"sYearMonth":"2018-03"},{"fValue":14.4348,"sYearMonth":"2017-12"},{"fValue":12.6504,"sYearMonth":"2017-09"},{"fValue":7.9092,"sYearMonth":"2017-06"},{"fValue":4.3995,"sYearMonth":"2017-03"}]
     * vtDateValueAvg : [{"fValue":0.95137,"sYearMonth":"2017-03"},{"fValue":3.50875,"sYearMonth":"2017-06"},{"fValue":4.62725,"sYearMonth":"2017-09"},{"fValue":1.38761,"sYearMonth":"2017-12"},{"fValue":0.61198,"sYearMonth":"2018-03"}]
     */

    private StFinDateBean stFinDate;

    public int getEFinType() {
        return eFinType;
    }

    public void setEFinType(int eFinType) {
        this.eFinType = eFinType;
    }

    public StFinDateBean getStFinDate() {
        return stFinDate;
    }

    public void setStFinDate(StFinDateBean stFinDate) {
        this.stFinDate = stFinDate;
    }

    public static class StFinDateBean {
        private double fVal;
        private String sDtSecCode;
        private String sFinanceDesc;
        /**
         * iDtNum : 101
         * iRank : 100
         */

        private StIndSTRankBean stIndSTRank;
        /**
         * fValue : -21.7467
         * sYearMonth : 2018-03
         */

        private List<VtDateValueBean> vtDateValue;
        /**
         * fValue : 0.95137
         * sYearMonth : 2017-03
         */

        private List<VtDateValueAvgBean> vtDateValueAvg;

        public double getFVal() {
            return fVal;
        }

        public void setFVal(double fVal) {
            this.fVal = fVal;
        }

        public String getSDtSecCode() {
            return sDtSecCode;
        }

        public void setSDtSecCode(String sDtSecCode) {
            this.sDtSecCode = sDtSecCode;
        }

        public String getSFinanceDesc() {
            return sFinanceDesc;
        }

        public void setSFinanceDesc(String sFinanceDesc) {
            this.sFinanceDesc = sFinanceDesc;
        }

        public StIndSTRankBean getStIndSTRank() {
            return stIndSTRank;
        }

        public void setStIndSTRank(StIndSTRankBean stIndSTRank) {
            this.stIndSTRank = stIndSTRank;
        }

        public List<VtDateValueBean> getVtDateValue() {
            return vtDateValue;
        }

        public void setVtDateValue(List<VtDateValueBean> vtDateValue) {
            this.vtDateValue = vtDateValue;
        }

        public List<VtDateValueAvgBean> getVtDateValueAvg() {
            return vtDateValueAvg;
        }

        public void setVtDateValueAvg(List<VtDateValueAvgBean> vtDateValueAvg) {
            this.vtDateValueAvg = vtDateValueAvg;
        }

        public static class StIndSTRankBean {
            private int iDtNum;
            private int iRank;

            public int getIDtNum() {
                return iDtNum;
            }

            public void setIDtNum(int iDtNum) {
                this.iDtNum = iDtNum;
            }

            public int getIRank() {
                return iRank;
            }

            public void setIRank(int iRank) {
                this.iRank = iRank;
            }
        }

        public static class VtDateValueBean {
            private double fValue;
            private String sYearMonth;

            public double getFValue() {
                return fValue;
            }

            public void setFValue(double fValue) {
                this.fValue = fValue;
            }

            public String getSYearMonth() {
                return sYearMonth;
            }

            public void setSYearMonth(String sYearMonth) {
                this.sYearMonth = sYearMonth;
            }
        }

        public static class VtDateValueAvgBean {
            private double fValue;
            private String sYearMonth;

            public double getFValue() {
                return fValue;
            }

            public void setFValue(double fValue) {
                this.fValue = fValue;
            }

            public String getSYearMonth() {
                return sYearMonth;
            }

            public void setSYearMonth(String sYearMonth) {
                this.sYearMonth = sYearMonth;
            }
        }
    }
}
