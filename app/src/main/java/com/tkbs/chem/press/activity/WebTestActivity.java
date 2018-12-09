package com.tkbs.chem.press.activity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.tkbs.chem.press.R;
import com.tkbs.chem.press.base.BaseActivity;

import butterknife.BindView;

public class WebTestActivity extends BaseActivity {

    @BindView(R.id.web_test)
    WebView webTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_web_test);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web_test;
    }

    @Override
    protected void initdata() {
        webTest.getSettings().setJavaScriptEnabled(true);
        webTest.getSettings().setSupportZoom(true);
        webTest.getSettings().setBuiltInZoomControls(true);
        webTest.getSettings().setBuiltInZoomControls(true);
        webTest.getSettings().setDisplayZoomControls(false);
        webTest.loadUrl("http://www.jiankeyan.com/Index/Index.aspx");
    }

    @Override
    protected void initTitle() {

    }
}
