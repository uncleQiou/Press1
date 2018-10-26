package com.tkbs.chem.press.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/10/26.
 */
public class BookCityResultDataList {

    private BookCityResCatagory resCatagory;
    private List<BookCityResDocument> resDocuments;

    public BookCityResCatagory getResCatagory() {
        return resCatagory;
    }

    public void setResCatagory(BookCityResCatagory resCatagory) {
        this.resCatagory = resCatagory;
    }

    public List<BookCityResDocument> getResDocuments() {
        return resDocuments;
    }

    public void setResDocuments(List<BookCityResDocument> resDocuments) {
        this.resDocuments = resDocuments;
    }
}
