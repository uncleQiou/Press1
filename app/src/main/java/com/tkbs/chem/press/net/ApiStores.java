package com.tkbs.chem.press.net;


import com.tkbs.chem.press.bean.BannerDataBean;
import com.tkbs.chem.press.bean.BookCityDataBean;
import com.tkbs.chem.press.bean.BookCityResCatagory;
import com.tkbs.chem.press.bean.GiveBookListBean;
import com.tkbs.chem.press.bean.HttpResponse;
import com.tkbs.chem.press.bean.SampleBookDetailDataBean;
import com.tkbs.chem.press.bean.SampleBookManageDataBean;
import com.tkbs.chem.press.bean.SecondClassifyDataBean;
import com.tkbs.chem.press.bean.ThreeClassifyDataBena;
import com.tkbs.chem.press.bean.UserBean;
import com.tkbs.chem.press.bean.UserInfoManageDataBean;
import com.tkbs.chem.press.bean.UserManageDataBean;

import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

public interface ApiStores {
    /**
     * 游客用户免登陆
     */
    @POST("login/toLogin/{equipment}")
    Observable<HttpResponse<UserBean>> NoLanding(@Path("equipment") String equipment);

    /**
     * 登陆
     *
     * @param body
     * @return
     */
    @POST("login/toLogin")
    Observable<HttpResponse<UserBean>> PressLogin(@Body RequestBody body);

    /***
     * 获取图片路径
     *
     * @return
     */
    @POST("common/webPath")
    Observable<HttpResponse<String>> GetWebPath();

    /**
     * 首页banner图
     *
     * @return
     */
    @POST("resDocument/polling/1/10")
    Observable<HttpResponse<ArrayList<BannerDataBean>>> BannerData();

    /**
     * 书城首页数据
     *
     * @return
     */
    @POST("resDocument/index")
    Observable<HttpResponse<ArrayList<BookCityDataBean>>> BookCityData();

    /**
     * 书城二级页面Indicator数据
     *
     * @return
     */
    @POST("resDocument/customizationTwo{guid}")
    Observable<HttpResponse<ArrayList<BookCityResCatagory>>> SecondClassificIndicator(@Path("guid") String guid);

    /**
     * 书城二级页面
     *
     * @param guid
     * @return
     */
    @POST("resDocument/customization{guid}")
    Observable<HttpResponse<ArrayList<SecondClassifyDataBean>>> SecondClassifyData(@Path("guid") String guid);

    /**
     * 书城三级页面
     *
     * @param guid
     * @return
     */
    @POST("resDocument/customizationFour{guid}")
    Observable<HttpResponse<ArrayList<ThreeClassifyDataBena>>> ThreeClassifyData(@Path("guid") String guid);

    /**
     * 我的定制
     *
     * @return
     */
    @POST("resDocument/interestIndex")
    Observable<HttpResponse<ArrayList<ThreeClassifyDataBena>>> MyCustomData();

    /**
     * 样书管理列表
     */
    @POST("salesman/info")
    Observable<HttpResponse<ArrayList<SampleBookManageDataBean>>> SampleBookManageList();

    /**
     * 样书管理详情
     */
    @POST("salesman/sampleInfo/{guid}")
    Observable<HttpResponse<ArrayList<SampleBookDetailDataBean>>> SampleBookDetail(@Path("guid") String guid);

    /**
     * 用户管理列表
     *
     * @return
     */
    @POST("salesmanUser/userList")
    Observable<HttpResponse<ArrayList<UserManageDataBean>>> UserManageDataList();

    /**
     * 用户管理——用户信息
     *
     * @param guid
     * @return
     */
    @POST("salesmanUser/userInfo/{guid}")
    Observable<HttpResponse<UserInfoManageDataBean>> UserDetail(@Path("guid") String guid);

    /**
     * 用户管理——赠书清单
     *
     * @param guid
     * @return
     */
    @POST("salesmanUser/givebookList/{guid}")
    Observable<HttpResponse<ArrayList<GiveBookListBean>>> GiveBookList(@Path("guid") String guid);

    /**
     * 用户管理——样书清单
     *
     * @param guid
     * @return
     */
    @POST("salesmanUser/samplebookList/{guid}")
    Observable<HttpResponse<ArrayList<GiveBookListBean>>> SampleBookList(@Path("guid") String guid);

    /**
     * 样书审核
     *
     * @param body
     * @return
     */
    @POST("salesman/updateInfo")
    Observable<HttpResponse<Object>> ApprovalDataSubmit(@Body RequestBody body);
    /*************************************************************************************************************/

}
