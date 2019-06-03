package com.tkbs.chem.press.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.tkbs.chem.press.MainActivity;
import com.tkbs.chem.press.R;
import com.tkbs.chem.press.base.BaseActivity;
import com.tkbs.chem.press.base.BaseApplication;
import com.tkbs.chem.press.bean.HttpResponse;
import com.tkbs.chem.press.bean.LoginRequestBen;
import com.tkbs.chem.press.bean.UserBean;
import com.tkbs.chem.press.net.ApiCallback;
import com.tkbs.chem.press.util.Config;
import com.tkbs.chem.press.util.MessageEvent;
import com.tkbs.chem.press.util.UiUtils;
import com.tkbs.chem.press.view.ReWebChomeClient;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import okhttp3.RequestBody;

public class RegisterAvtivity extends BaseActivity implements View.OnClickListener, ReWebChomeClient.OpenFileChooserCallBack {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.register_web)
    WebView registerWeb;

    private String baseUrl = Config.API_SERVER + "hello/register.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_avtivity;
    }

    @Override
    protected void initdata() {
        initWeb();
    }

    @Override
    protected void initTitle() {
        title.setText(R.string.register);
    }

    @OnClick({R.id.back})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                registerWeb.loadUrl("javascript:disSmsInterval()");
                finish();
                break;
            default:
                break;
        }
    }

    private void initWeb() {
        WebSettings setting = registerWeb.getSettings();
        //允许加载javascript
        setting.setJavaScriptEnabled(true);
        //允许缩放
        setting.setSupportZoom(true);
        //原网页基础上缩放
        setting.setBuiltInZoomControls(true);
        setting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //任意比例缩放
        setting.setUseWideViewPort(false);
        setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        setting.setLoadWithOverviewMode(true);
        setting.setSaveFormData(true);
        setting.setDomStorageEnabled(false);
        setting.setJavaScriptCanOpenWindowsAutomatically(true);
        setting.setSupportMultipleWindows(true);
        registerWeb.addJavascriptInterface(new RegisterInterface(), "TKBS");
        /*****************************************************************
         * 在点击请求的是链接时才会调用，重写此方法返回true表明点击网页里
         * 面的链接还是在当前的WebView里跳转，不会跳到浏览器上运行。
         *****************************************************************/
        registerWeb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showProgressDialog();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Log.e("qiou", "errow" + description);
                String data = "  ";
                view.loadUrl("javascript:document.body.innerHTML=\"" + data + "\"");
                view.stopLoading();
                view.clearView();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        registerWeb.setWebChromeClient(new ReWebChomeClient(this) {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress >= 50) {
                    dismissProgressDialog();
                }
            }
        });

        registerWeb.loadUrl(baseUrl);
    }

    @Override
    public void openFileChooserCallBack(ValueCallback<Uri> uploadMsg, String acceptType) {

    }

    private String result;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0:
                    result = data.getStringExtra("result");
                    registerWeb.loadUrl("javascript:getInterestList(" + result + ")");
                    Logger.e(result);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        registerWeb.loadUrl("javascript:disSmsInterval()");
        super.onBackPressed();
    }

    @Override
    protected synchronized void onDestroy() {
        registerWeb.loadUrl("javascript:disSmsInterval()");
        if (registerWeb != null) {
            ViewParent parent = registerWeb.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(registerWeb);
            }
            //退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            registerWeb.stopLoading();
            registerWeb.getSettings().setJavaScriptEnabled(false);
            registerWeb.clearHistory();
            registerWeb.clearView();
            registerWeb.removeAllViews();
            try {
                registerWeb.destroy();
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
        super.onDestroy();
    }

    private class RegisterInterface {

        RegisterInterface() {
        }


        @JavascriptInterface
        public void ShowProgressbar(final int progress) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (progress >= 50) {
                        dismissProgressDialog();
                    } else {
                        showProgressDialog();
                    }
                }
            });
        }

        @JavascriptInterface
        public String getUser() {
            String user = preference.getString("login_name", "") +
                    "," +
                    preference.getString("PASSWORD", "") + "," + UiUtils.getid(RegisterAvtivity.this);

            return user;
        }

        @JavascriptInterface
        public void finshBookDetail() {
            finish();
        }

        @JavascriptInterface
        public void MyInterest() {
            Intent intent = new Intent(RegisterAvtivity.this, MyInterestActivity.class);
            intent.putExtra("myInterst", result);
            startActivityForResult(intent, 0);
        }

        @JavascriptInterface
        public void goToS() {
            Intent intent = new Intent(RegisterAvtivity.this, ServiceWebActivityA.class);
            startActivity(intent);
        }
        @JavascriptInterface
        public void login(String loginName, String password) {

            //{"loginName":"xx000001","password":"1"}
            LoginRequestBen loginRequestBen = new LoginRequestBen();
            loginRequestBen.setLoginName(loginName);
            loginRequestBen.setPassword(password);
            final Gson gson = new Gson();
            String route = gson.toJson(loginRequestBen);
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), route);
            showProgressDialog();
            addSubscription(apiStores.PressLogin(body), new ApiCallback<HttpResponse<UserBean>>() {
                @Override
                public void onSuccess(HttpResponse<UserBean> model) {
                    if (model.isStatus()) {
                        UserBean user = model.getData();
                        SharedPreferences.Editor edit = BaseApplication.preferences.edit();
                        edit.putString(Config.LOGIN_NAME, user.getLogin_name());
                        edit.putString(Config.GUID, user.getGuid());
                        edit.putString(Config.PASSWORD, user.getPASSWORD());
                        edit.putString(Config.NICK_NAME, user.getNick_name());
                        edit.putString(Config.REAL_NAME, user.getReal_name());
                        edit.putString(Config.WORKPHONE, user.getWorkphone());
                        edit.putString(Config.PHONE, user.getPhone());
                        edit.putInt(Config.MEMBER_TYPE, user.getMember_type());
                        edit.putInt(Config.MEMBER_STATE, user.getState());
                        edit.commit();
                        //  refresh MainActivity
                        EventBus.getDefault().post(new MessageEvent("Refresh"));
                        setResult(RESULT_OK);
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
}
