package com.tkbs.chem.press.base;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;

import com.bumptech.glide.request.RequestOptions;
import com.mob.MobSDK;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tkbs.chem.press.R;
import com.tkbs.chem.press.util.SPManagement;


/**
 * Created by Administrator on 2017/8/24.
 */
public class BaseApplication extends Application {
    public static BaseApplication myApplication;
    public static RequestOptions options;
    private static Context context;
    public static SharedPreferences preferences;
    public static String imgBasePath;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MobSDK.init(this);
        myApplication = this;
        options = new RequestOptions();
        options.placeholder(R.drawable.ic_default_image);
        options.error(R.drawable.ic_default_image);
        options.fitCenter();

        context = getApplicationContext();
        preferences = getSharedPreferences("press", MODE_PRIVATE);
        Logger.addLogAdapter(new AndroidLogAdapter());
        SPManagement.init(this);
    }


    public static BaseApplication getInstances() {
        return myApplication;
    }

    public static Context getContext() {
        return context;
    }
}
