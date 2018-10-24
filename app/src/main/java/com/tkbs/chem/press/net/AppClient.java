package com.tkbs.chem.press.net;


import com.tkbs.chem.press.base.BaseApplication;
import com.tkbs.chem.press.util.AAHelper;
import com.tkbs.chem.press.util.Config;
import com.tkbs.chem.press.util.UiUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class AppClient {
    public static Retrofit mRetrofit;

    public static Retrofit retrofit() {
        if (mRetrofit == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request originalRequest = chain.request();
                    String nonce = UiUtils.random();
                    Request authorised = originalRequest.newBuilder()
                            .addHeader("Content-Type", "application/json")
                            //.addHeader("Auth", "user=auth;userName=xx000001;nonce=5b681434-c76a-17a2-0bb6-c6777ca1ca0e;accessKey=CD2028085A5F4E3978ED9063A0254F56")
                            .addHeader("Auth", "user=auth;userName=" +
                                    BaseApplication.preferences.getString("login_name", "") +
                                    ";nonce=" + nonce + ";accessKey=" +
                                    AAHelper.CalculatePKey(
                                            BaseApplication.preferences.getString(
                                                    "PASSWORD", ""), nonce))
                            .build();
                    return chain.proceed(authorised);
                }
            });
//            if (Config.DEBUG) {
//                // Log信息拦截器
//                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//                //设置 Debug Log 模式
//                builder.addInterceptor(loggingInterceptor);
//            }
            OkHttpClient okHttpClient = builder.build();
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(Config.API_SERVER)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return mRetrofit;
    }

}
