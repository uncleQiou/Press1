package com.tkbs.chem.press.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/10/27.
 */
public class SecondClassifyDataBean {
    private BookCityResCatagory resCatagory;
    private List<BookCityResDocument> resDocumentList;

    public BookCityResCatagory getResCatagory() {
        return resCatagory;
    }

    public void setResCatagory(BookCityResCatagory resCatagory) {
        this.resCatagory = resCatagory;
    }

    public List<BookCityResDocument> getResDocumentList() {
        return resDocumentList;
    }

    public void setResDocumentList(List<BookCityResDocument> resDocumentList) {
        this.resDocumentList = resDocumentList;
    }
}
