package com.tkbs.chem.press.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/10/26.
 */
public class BookCityDataBean {

    private BookCityResCatagory resCatagory;
    private BookCityResDocument resDocuments;
    private List<BookCityResultDataList> resultDataList;

    public BookCityResCatagory getResCatagory() {
        return resCatagory;
    }

    public void setResCatagory(BookCityResCatagory resCatagory) {
        this.resCatagory = resCatagory;
    }

    public BookCityResDocument getResDocuments() {
        return resDocuments;
    }

    public void setResDocuments(BookCityResDocument resDocuments) {
        this.resDocuments = resDocuments;
    }

    public List<BookCityResultDataList> getResultDataList() {
        return resultDataList;
    }

    public void setResultDataList(List<BookCityResultDataList> resultDataList) {
        this.resultDataList = resultDataList;
    }
}
