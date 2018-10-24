package com.tkbs.chem.press.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;

import com.tkbs.chem.press.MainActivity;
import com.tkbs.chem.press.R;
import com.tkbs.chem.press.base.BaseActivity;
import com.tkbs.chem.press.base.BaseApplication;
import com.tkbs.chem.press.bean.HttpResponse;
import com.tkbs.chem.press.bean.UserBean;
import com.tkbs.chem.press.net.ApiCallback;
import com.tkbs.chem.press.util.PermissionUtils;
import com.tkbs.chem.press.util.UiUtils;
import com.tkbs.chem.press.view.CustomerDialog;

import java.util.ArrayList;


/**
 * Created by Administrator on 2017/8/25.
 */
public class SplashActivity extends BaseActivity {
    private static final int GOTO_MAIN_ACTIVITY = 0;
    private static final int GOTO_LOGIN_ACTIVITY = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case GOTO_MAIN_ACTIVITY:
                    if (UiUtils.isNullorEmpty(BaseApplication.preferences.getString("login_name", ""))) {
                        NoLanding();
                    } else {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();

                    }
                    break;
                case GOTO_LOGIN_ACTIVITY:
                    Intent intent1 = new Intent();
                    intent1.setClass(SplashActivity.this, LoginActivity.class);
                    startActivity(intent1);
                    finish();
                default:
                    break;
            }
        }

    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initdata() {

    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        getPersimmions();
    }

    private final int SDK_PERMISSION_REQUEST = 127;

    @TargetApi(23)
    private void getPersimmions() {
        PermissionUtils.isCameraPermission(SplashActivity.this, 0x007);
        PermissionUtils.isCameraPermission(SplashActivity.this, 0x009);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
//            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
//            }
//            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
//            }
            /*
             * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
			 */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            }
            // 读取电话状态权限
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
            }
            // 读取电话状态权限
//            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
//                permissions.add(Manifest.permission.READ_PHONE_STATE);
//            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            } else {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                getMsg();
            }
        } else {
            getMsg();
        }
    }


    private void getMsg() {
        mHandler.sendEmptyMessageDelayed(GOTO_MAIN_ACTIVITY, 3000);
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }

        } else {
            return true;
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case SDK_PERMISSION_REQUEST:
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    } else {
                        if (dialog == null || !dialog.isShowing()) {
                            showMissingPermissionDialog();
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    private Dialog dialog;

    // 显示缺失权限提示
    private void showMissingPermissionDialog() {
        dialog = CustomerDialog.createDeletaDialog(this, "是否添加权限？", new MyOnclicklistener());
        dialog.show();
    }


    class MyOnclicklistener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_deletedialog_cancle:
                    finish();
                    break;
                case R.id.tv_deletedialog_delete:
                    dialog.dismiss();
                    startAppSettings();
                    break;
                default:
                    break;
            }
        }
    }

    // 启动应用的设置
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    /**
     * 游客免登陆
     */
    private void NoLanding() {
        showProgressDialog();
        addSubscription(apiStores.NoLanding(UiUtils.getid(SplashActivity.this)),
                new ApiCallback<HttpResponse<UserBean>>() {
                    @Override
                    public void onSuccess(HttpResponse<UserBean> model) {
                        if (model.isStatus()) {
                            UserBean user = model.getData();
                            SharedPreferences.Editor edit = BaseApplication.preferences.edit();
                            edit.putString("login_name", user.getLogin_name());
                            edit.putString("PASSWORD", user.getPASSWORD());
                            edit.putString("nick_name", user.getNick_name());
                            edit.commit();
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();
                        } else {
                            toastShow(model.getErrorDescription());
                        }

                    }

                    @Override
                    public void onFailure(String msg) {
                        toastShow(msg);
                    }

                    @Override
                    public void onFinish() {
                        dismissProgressDialog();
                    }
                });
    }
}
