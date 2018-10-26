package com.tkbs.chem.press.net;


import com.tkbs.chem.press.bean.BannerDataBean;
import com.tkbs.chem.press.bean.BookCityDataBean;
import com.tkbs.chem.press.bean.HttpResponse;
import com.tkbs.chem.press.bean.UserBean;

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

    /*************************************************************************************************************/

}
