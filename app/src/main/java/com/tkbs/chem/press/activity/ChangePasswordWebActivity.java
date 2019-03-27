package com.tkbs.chem.press.activity;

import android.content.Intent;
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

import com.tkbs.chem.press.R;
import com.tkbs.chem.press.base.BaseActivity;
import com.tkbs.chem.press.util.Config;
import com.tkbs.chem.press.util.UiUtils;
import com.tkbs.chem.press.view.ReWebChomeClient;

import butterknife.BindView;
import butterknife.OnClick;

public class ChangePasswordWebActivity extends BaseActivity implements View.OnClickListener, ReWebChomeClient.OpenFileChooserCallBack {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.web_view)
    WebView webView;
    private String baseUrl = Config.API_SERVER + "hello/change_password.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_password_web;
    }

    @Override
    protected void initdata() {
        initWeb();
    }

    @Override
    protected void initTitle() {
        title.setText(R.string.change_password);
    }

    @OnClick({R.id.back})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                webView.loadUrl("javascript:disSmsInterval()");
                finish();
                break;
            default:
                break;
        }
    }

    private void initWeb() {
        WebSettings setting = webView.getSettings();
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
        setting.setDomStorageEnabled(true);
        setting.setJavaScriptCanOpenWindowsAutomatically(true);
        setting.setSupportMultipleWindows(true);
        webView.addJavascriptInterface(new ChangePasswordInterface(), "TKBS");
        /*****************************************************************
         * 在点击请求的是链接时才会调用，重写此方法返回true表明点击网页里
         * 面的链接还是在当前的WebView里跳转，不会跳到浏览器上运行。
         *****************************************************************/
        webView.setWebViewClient(new WebViewClient() {
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
        webView.setWebChromeClient(new ReWebChomeClient(this) {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress >= 50) {
                    dismissProgressDialog();
                }
            }
        });

        webView.loadUrl(baseUrl);
    }

    @Override
    public void openFileChooserCallBack(ValueCallback<Uri> uploadMsg, String acceptType) {

    }

    @Override
    public void onBackPressed() {
        webView.loadUrl("javascript:disSmsInterval()");
        super.onBackPressed();
    }

    @Override
    protected synchronized void onDestroy() {
        webView.loadUrl("javascript:disSmsInterval()");
        if (webView != null) {
            ViewParent parent = webView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(webView);
            }
            //退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            webView.stopLoading();
            webView.getSettings().setJavaScriptEnabled(false);
            webView.clearHistory();
            webView.clearView();
            webView.removeAllViews();
            try {
                webView.destroy();
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
        super.onDestroy();
    }
    private class ChangePasswordInterface {

        ChangePasswordInterface() {
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
                    preference.getString("PASSWORD", "") + "," + UiUtils.getid(ChangePasswordWebActivity.this);

            return user;
        }

        @JavascriptInterface
        public void toLogin() {
            //  删除个人信息
            Intent intent = new Intent(ChangePasswordWebActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }


    }
}
