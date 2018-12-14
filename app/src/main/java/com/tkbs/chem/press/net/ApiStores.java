package com.tkbs.chem.press.net;


import com.tkbs.chem.press.bean.BannerDataBean;
import com.tkbs.chem.press.bean.BookCityDataBean;
import com.tkbs.chem.press.bean.BookCityResCatagory;
import com.tkbs.chem.press.bean.BookCityResDocument;
import com.tkbs.chem.press.bean.BookDetailBean;
import com.tkbs.chem.press.bean.GiveBookListBean;
import com.tkbs.chem.press.bean.HttpResponse;
import com.tkbs.chem.press.bean.MyApplyDataBean;
import com.tkbs.chem.press.bean.OpinionManageBean;
import com.tkbs.chem.press.bean.OrderInfo;
import com.tkbs.chem.press.bean.OrderInfoBean;
import com.tkbs.chem.press.bean.RechargeResult;
import com.tkbs.chem.press.bean.SampleBookDetailDataBean;
import com.tkbs.chem.press.bean.SampleBookItemDataBean;
import com.tkbs.chem.press.bean.SampleBookManageDataBean;
import com.tkbs.chem.press.bean.SearchHotKey;
import com.tkbs.chem.press.bean.SecondClassifyDataBean;
import com.tkbs.chem.press.bean.ThreeClassifyDataBena;
import com.tkbs.chem.press.bean.UserBean;
import com.tkbs.chem.press.bean.UserInfoManageDataBean;
import com.tkbs.chem.press.bean.UserManageDataBean;

import java.util.ArrayList;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
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
    @POST("resDocument/customizationTwo/{guid}")
    Observable<HttpResponse<ArrayList<BookCityResCatagory>>> SecondClassificIndicator(@Path("guid") String guid);

    /**
     * 书城二级页面
     *
     * @param
     * @return
     */
    @POST("resDocument/customization/{guid}/{pageNum}/10")
    Observable<HttpResponse<ArrayList<SecondClassifyDataBean>>> SecondClassifyData(@Path("guid") String guid, @Path("pageNum") int pageNum);

    /**
     * 书城三级页面
     *
     * @param guid
     * @return
     */
    @POST("resDocument/customizationFour/{guid}/{pageNum}/10")
    Observable<HttpResponse<ArrayList<ThreeClassifyDataBena>>> ThreeClassifyData(@Path("guid") String guid, @Path("pageNum") int pageNum);

    /**
     * 我的定制
     *
     * @return
     */
    @POST("resDocument/interestIndex")
    Observable<HttpResponse<ArrayList<ThreeClassifyDataBena>>> MyCustomData();

    /**
     * 样书管理列表  废弃
     */
//    @POST("salesman/info")
//    Observable<HttpResponse<ArrayList<SampleBookManageDataBean>>> SampleBookManageList();

    /**
     * 样书管理列表
     */
    @POST("sampleBook/querySampleBookOne/{pageNum}/10")
    Observable<HttpResponse<ArrayList<SampleBookManageDataBean>>> SampleBookManageList(@Path("pageNum") int page);

//    /**
//     * 样书管理详情 废弃
//     */
//    @POST("salesman/sampleInfo/{guid}")
//    Observable<HttpResponse<ArrayList<SampleBookDetailDataBean>>> SampleBookDetail(@Path("guid") String guid);

    /**
     * 样书管理详情
     */
    @POST("sampleBook/querySampleBookTwo/{guid}")
    Observable<HttpResponse<ArrayList<SampleBookDetailDataBean>>> SampleBookDetail(@Path("guid") String guid);

//    /**
//     * 用户管理列表  废弃
//     *
//     * @return
//     */
//    @POST("salesmanUser/userList")
//    Observable<HttpResponse<ArrayList<UserManageDataBean>>> UserManageDataList();

    /**
     * 用户管理列表
     *
     * @return
     */
    @POST("mmMember/queryMemberList/{pageNum}/10")
    Observable<HttpResponse<ArrayList<UserManageDataBean>>> UserManageDataList(@Path("pageNum") int page);

//    /**
//     * 用户管理——用户信息 废弃
//     *
//     * @param guid
//     * @return
//     */
//    @POST("salesmanUser/userInfo/{guid}")
//    Observable<HttpResponse<UserInfoManageDataBean>> UserDetail(@Path("guid") String guid);

    /**
     * 用户管理——用户信息
     *
     * @param guid
     * @return
     */
    @POST("mmMember/queryPersonInfo/{userGuid}")
    Observable<HttpResponse<UserInfoManageDataBean>> UserDetail(@Path("userGuid") String guid);

//    /**
//     * 用户管理——赠书清单 废弃
//     *
//     * @param guid
//     * @return
//     */
//    @POST("salesmanUser/givebookList/{guid}")
//    Observable<HttpResponse<ArrayList<GiveBookListBean>>> GiveBookList(@Path("guid") String guid);

    /**
     * 用户管理——赠书清单
     *
     * @param guid
     * @return
     */
    @POST("giveBook/queryGiveBookBySaleMan/{memberGuid}")
    Observable<HttpResponse<ArrayList<GiveBookListBean>>> GiveBookList(@Path("memberGuid") String guid);

//    /**
//     * 用户管理——样书清单 废弃
//     *
//     * @param guid
//     * @return
//     */
//    @POST("salesmanUser/samplebookList/{guid}")
//    Observable<HttpResponse<ArrayList<GiveBookListBean>>> SampleBookList(@Path("guid") String guid);

    /**
     * 用户管理——样书清单
     *
     * @param guid
     * @return
     */
    @POST("sampleBook/querySampleBookBySaleMan/{memberGuid}")
    Observable<HttpResponse<ArrayList<GiveBookListBean>>> SampleBookList(@Path("memberGuid") String guid);

//    /**
//     * 样书审核  废弃
//     *
//     * @param body
//     * @return
//     */
//    @POST("salesman/updateInfo")
//    Observable<HttpResponse<Object>> ApprovalDataSubmit(@Body RequestBody body);

    /**
     * 样书审核
     *
     * @param body
     * @return
     */
    @POST("sampleBook/approveSampleBook")
    Observable<HttpResponse<Object>> ApprovalDataSubmit(@Body RequestBody body);

    /**
     * 书架 ——免费样书 教师
     *
     * @param pageNum
     * @return
     */
    @POST("sampleBook/{pageNum}/10")
    Observable<HttpResponse<ArrayList<SampleBookItemDataBean>>> getSampleBookList(@Path("pageNum") int pageNum);

    /**
     * 书架 ——免费样书 教师  删除
     *
     * @param guids
     * @return
     */
    @POST("sampleBook")
    Observable<HttpResponse<Object>> deleteSampleBook(@Query("guids") String[] guids);

    /**
     * 书架 ——我的收藏 教师
     *
     * @param pageNum
     * @return
     */
    @POST("collection/{pageNum}/10")
    Observable<HttpResponse<ArrayList<SampleBookItemDataBean>>> getCollectionBookList(@Path("pageNum") int pageNum);

    /**
     * 书架 ——我的收藏 教师
     *
     * @param guids
     * @return
     */
    @POST("collection")
    Observable<HttpResponse<Object>> deleteCollectionBook(@Query("guids") String[] guids);

    /**
     * 书架 ——已购图书 教师
     *
     * @param pageNum
     * @return
     */
    @POST("order/{pageNum}/10")
    Observable<HttpResponse<ArrayList<SampleBookItemDataBean>>> getBuyedBookList(@Path("pageNum") int pageNum);

    /**
     * 书架 ——我的赠书 教师
     *
     * @param pageNum
     * @return
     */
    @POST("giveBook/{pageNum}/10")
    Observable<HttpResponse<ArrayList<SampleBookItemDataBean>>> getGiveBookList(@Path("pageNum") int pageNum);

    /**
     * 书架 ——我的赠书 教师 删除
     *
     * @param guids
     * @return
     */
    @POST("giveBook")
    Observable<HttpResponse<Object>> deleteGiveBook(@Query("guids") String[] guids);

    /**
     * 搜索结果
     *
     * @param pageNum
     * @return
     */
    @POST("search/queryDocumentBySearch/{pageNum}/10")
    Observable<HttpResponse<ArrayList<BookCityResDocument>>> getSearchList(@Path("pageNum") int pageNum, @Body RequestBody body);

    /**
     * 搜索结果
     *
     * @param pageNum
     * @return
     */
    @POST("searchWord/{pageNum}/10")
    Observable<HttpResponse<ArrayList<SearchHotKey>>> getSearchHotKey(@Path("pageNum") int pageNum);

    /**
     * 查询图书详情
     *
     * @param guid
     * @return
     */
    @POST("resDocument/queryDocument/{guid}")
    Observable<HttpResponse<BookDetailBean>> getBookDetail(@Path("guid") String guid);

    /**
     * 支付宝 支付
     *
     * @param documentGUID
     * @return
     */
    @POST("pay/payReadyAlipay/{documentGUID}")
    Observable<HttpResponse<OrderInfo>> payReadyAlipay(@Path("documentGUID") String documentGUID);

    /***
     * 查询订单信息
     *
     * @param documentGuid
     * @return
     */
    @POST("order/queryOrderInfo")
    Observable<HttpResponse<OrderInfoBean>> checkOrderInfo(@Query("documentGuid") String documentGuid);

    /**
     * 微信 支付
     *
     * @param documentGuid
     * @return
     */
    @POST("pay/payReadyWx")
    Observable<HttpResponse<RechargeResult.WeChat>> payReadyWeChat(@Query("documentGuid") String documentGuid);

    /**
     * 黑名单 0 正常 1 黑名单
     *
     * @param memberGuid ,state
     * @return
     */
    @POST("mmMember/updateMemberOfBlackList/{memberGuid}/{state}")
    Observable<HttpResponse<RechargeResult.WeChat>> blackList(@Path("memberGuid") String memberGuid, @Path("state") int state);

    /**
     * 黑名单 0 正常 1 黑名单
     *
     * @param guids ,timeLimit
     * @return
     */
    @POST("sampleBook/batchApproveSampleBook")
    Observable<HttpResponse<Object>> oneKeyApprove(@Query("guids") ArrayList<String> guids, @Query("timeLimit") int timeLimit);

    /**
     * private Integer type
     * 1、平台回复 2、我的回复
     * 业务员意见管理列表
     *
     * @param pageNum
     * @return
     */
    @POST("opinion/queryOpinionByPager/{pageNum}/10")
    Observable<HttpResponse<ArrayList<OpinionManageBean>>> saleManOpinionManage(@Path("pageNum") int pageNum);

    /**
     * private Integer type
     * 1、平台回复 2、我的回复
     * 教师意见管理列表
     *
     * @param pageNum
     * @return
     */
    @POST("opinion/queryMyOpinionByPager/{pageNum}/10")
    Observable<HttpResponse<ArrayList<OpinionManageBean>>> teaOpinionManage(@Path("pageNum") int pageNum);

    /**
     * 发表回复意见
     *
     * @param parentId
     * @return
     */
    @POST("opinion/addOpinion")
    Observable<HttpResponse<Object>> addOpinion(@Query("parentId") int parentId, @Query("content") String content);


    /**
     * 下载文件
     * Streaming 大文件要加 否则oom
     */
    @Streaming
    @GET
    Call<ResponseBody> downloadFileWithUrl(@Url String fileUrl);

    /**
     * 获取书籍目录
     *
     * @param documentGuid
     * @return
     */
    @POST("read/getDirPath")
    Observable<HttpResponse<String>> getBookDir(@Query("documentGuid") String documentGuid);

    /**
     * 获取下载路径
     *
     * @param documentGuid
     * @return
     */
    @POST("read//queryTkbsFilePath/{documentGuid}")
    Observable<HttpResponse<String>> getResDownLoadPath(@Path("documentGuid") String documentGuid);

    /**
     * 我的申请
     *
     * @param pageNum
     * @return
     */
    @POST("sampleBook/querySampleBookApplication/{pageNum}/10")
    Observable<HttpResponse<ArrayList<MyApplyDataBean>>> getMyApplyData(@Path("pageNum") int pageNum);


    /*************************************************************************************************************/

}
