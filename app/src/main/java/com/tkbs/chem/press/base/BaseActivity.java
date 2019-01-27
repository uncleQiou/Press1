package com.tkbs.chem.press.base;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;


import com.tkbs.chem.press.R;
import com.tkbs.chem.press.net.ApiStores;
import com.tkbs.chem.press.net.AppClient;
import com.tkbs.chem.press.util.SystemBarTintManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public abstract class BaseActivity extends AppCompatActivity {


    private BaseActivity mActivity;
    public Unbinder unbinder;
    public ApiStores apiStores = AppClient.retrofit().create(ApiStores.class);
    public SharedPreferences preference;
    public String WebPath;
    public boolean mStateEnable;

    private MyBaseActiviy_Broad oBaseActiviy_Broad;

    //定义一个广播
    public class MyBaseActiviy_Broad extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            //接收发送过来的广播内容
            int closeAll = intent.getIntExtra("closeAll", 0);
            if (closeAll == 1) {
                finish();//销毁BaseActivity
            }
        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            //通知栏所需颜色
            tintManager.setStatusBarTintResource(R.color.white);
        }
        //黑色
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        preference = getSharedPreferences("press", Context.MODE_PRIVATE);
        WebPath = preference.getString("webPath", "");
        mActivity = this;
        // AppManager.getInstance().add(mActivity);
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }

        unbinder = ButterKnife.bind(this);
        initdata();
        initTitle();
        //动态注册广播
        oBaseActiviy_Broad = new MyBaseActiviy_Broad();
        IntentFilter intentFilter = new IntentFilter("com.tkbs.chem.press.base.BaseActivity");
        registerReceiver(oBaseActiviy_Broad, intentFilter);
    }

    protected abstract int getLayoutId();

    protected abstract void initdata();

    protected abstract void initTitle();

    @Override
    protected void onStart() {
        super.onStart();
        // super.onStart();中将mStateSaved置为false
        mStateEnable = true;
    }

    @Override
    protected void onResume() {
        // onPause之后便可能调用onSaveInstanceState，因此onresume中也需要置true
        mStateEnable = true;
        super.onResume();
        mActivity = this;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        // super.onSaveInstanceState();中将mStateSaved置为true
        mStateEnable = false;
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onStop() {
        // super.onStop();中将mStateSaved置为true
        mStateEnable = false;
        super.onStop();
    }

    @Override
    protected synchronized void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        unregisterReceiver(oBaseActiviy_Broad);
        //注销广播
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public void toastShow(int resId) {
        Toast.makeText(mActivity, resId, Toast.LENGTH_SHORT).show();
    }

    public void toastShow(String resId) {
        Toast.makeText(mActivity, resId, Toast.LENGTH_SHORT).show();
    }

    private CompositeSubscription mCompositeSubscription;

    public void addSubscription(Observable observable, Subscriber subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    public void addSubscription(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    public void onUnsubscribe() {
        //取消注册，以避免内存泄露
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    public ProgressDialog progressDialog;

    public ProgressDialog showProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = new ProgressDialog(mActivity);
        progressDialog.setMessage("加载中");
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        return progressDialog;
    }

    public ProgressDialog showProgressDialog(CharSequence message) {
        progressDialog = new ProgressDialog(mActivity);
        progressDialog.setMessage(message);
        progressDialog.show();
        return progressDialog;
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            // progressDialog.hide();会导致android.view.WindowLeaked
            progressDialog.dismiss();
        }
    }

    /**
     * activity状态是否处于可修改周期内，避免状态丢失的错误
     *
     * @return
     */
    public boolean isStateEnable() {
        return mStateEnable;
    }

}
