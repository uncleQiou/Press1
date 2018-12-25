package com.tkbs.chem.press.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.tkbs.chem.press.R;
import com.tkbs.chem.press.base.BaseActivity;
import com.tkbs.chem.press.bean.CreateOrderDataBean;
import com.tkbs.chem.press.bean.HttpResponse;
import com.tkbs.chem.press.net.ApiCallback;
import com.tkbs.chem.press.util.Config;
import com.tkbs.chem.press.util.MessageEvent;
import com.tkbs.chem.press.view.BookBuyPopupWindow;
import com.tkbs.chem.press.view.ReWebChomeClient;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;


public class BookDetailActivity extends BaseActivity implements View.OnClickListener, ReWebChomeClient.OpenFileChooserCallBack {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.book_detail_web)
    WebView bookDetailWeb;
    private String guid;

    private BookBuyPopupWindow menuWindow;

    private String bookUrl = Config.API_SERVER + "hello/book_detail.html";
    private String param3 = "";
    private boolean isReadAll;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_book_detail;
    }


    @Override
    protected void initdata() {
        guid = getIntent().getStringExtra("guid");
//        guid = "34DBCCE472A14205A2431AF22538644D";
        Logger.e("bookGuid == " + guid);
        EventBus.getDefault().register(this);
        createDir();
        initWeb();
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void RefreshUi(MessageEvent messageEvent) {
        if ("PaySuccess".endsWith(messageEvent.getMessage())) {
            Logger.e("支付成功 刷新页面");
            refreshUI();
        }
    }

    /**
     * 获取充值配置
     */
    private void createOrder() {
        showProgressDialog();
        addSubscription(apiStores.createOrder(guid), new ApiCallback<HttpResponse<CreateOrderDataBean>>() {
            @Override
            public void onSuccess(HttpResponse<CreateOrderDataBean> model) {

                menuWindow = new BookBuyPopupWindow(BookDetailActivity.this, itemsOnClick,
                        model.getData().getVirtualPrice(), model.getData().getAccountBalance());
                menuWindow.showAtLocation(findViewById(R.id.title),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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

    /**
     * 支付
     */
    private void ConfirmPayment() {
        showProgressDialog();
        addSubscription(apiStores.ConfirmPayment(guid), new ApiCallback<HttpResponse<Object>>() {
            @Override
            public void onSuccess(HttpResponse<Object> model) {
                // 通知html5页面进行刷新 购买完成 reflushData
                bookDetailWeb.loadUrl("javascript:reflushData()");
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

    /**
     * 刷新图书详情页面
     */
    private void refreshUI() {
        bookDetailWeb.loadUrl(bookUrl);
    }

    private void initWeb() {
        WebSettings setting = bookDetailWeb.getSettings();
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
        bookDetailWeb.addJavascriptInterface(new BookDetailInterface(), "TKBS");
        /*****************************************************************
         * 在点击请求的是链接时才会调用，重写此方法返回true表明点击网页里
         * 面的链接还是在当前的WebView里跳转，不会跳到浏览器上运行。
         *****************************************************************/
        bookDetailWeb.setWebViewClient(new WebViewClient() {
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
        bookDetailWeb.setWebChromeClient(new ReWebChomeClient(this) {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress >= 50) {
                    dismissProgressDialog();
                }
            }
        });

        bookDetailWeb.loadUrl(bookUrl);
    }

    /***
     * 为弹出窗口实现监听类
     */
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                // 去充值
                case R.id.tv_go_recharge:
                    Intent intent = new Intent(BookDetailActivity.this, RechargeActivity.class);
                    //Intent intent = new Intent(BookDetailActivity.this, PayActivity.class);
                    intent.putExtra("guid", guid);
                    startActivity(intent);
                    break;
                // 支付
                case R.id.tv_pay:
                    //  调用扣点接口
                    ConfirmPayment();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void initTitle() {
        title.setText(R.string.book_detail_title);
    }


    @OnClick({R.id.back})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
//                downLoadBook("sss");
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void openFileChooserCallBack(ValueCallback<Uri> uploadMsg, String acceptType) {

    }


    /**
     * 在SD卡上创建指定名称的目录
     *
     * @param
     */
    private File createDir() {
        File file = new File(Config.CIP_FILE_PATH);
        file.mkdir();
        return file;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // 根据上面发送过去的请求码来区别
        switch (requestCode) {
            case 886:
                if (data != null) {
                    param3 = data.getStringExtra("PARAM3");
                    Intent intent = new Intent(BookDetailActivity.this, TkbsReaderActivity.class);
                    intent.putExtra("BookId", guid);
                    String resPath = Config.CIP_FILE_PATH + guid + ".tkbs";
                    intent.putExtra("isLoacRead", isExist(resPath));
                    intent.putExtra("PARAM3", param3);
                    intent.putExtra("isReadAll", isReadAll);
                    startActivity(intent);
                }
            default:
                break;
        }
    }

    /**
     * 判断指定名称的文件在SD卡上是否存在
     */
    public boolean isExist(String path) {
        File file = new File(path);
        return file.exists();
    }


    private String getExternalCacheDirPath() {
        File d = getExternalCacheDir();
        if (d != null) {
            d.mkdirs();
            if (d.exists() && d.isDirectory()) {
                return d.getPath();
            }
        }
        return null;
    }

    private class BookDetailInterface {

        BookDetailInterface() {
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
        public String getBookGuid() {
            return guid;
        }

        @JavascriptInterface
        public void showBookCatalog() {
            Intent intent = new Intent(BookDetailActivity.this, TKBSCatalogAvtivity.class);
            intent.putExtra("BookId", guid);
            intent.putExtra("BookType", "tkbs");
            intent.putExtra("ISMULU", "tkbs");
            startActivityForResult(intent, 886);
        }


        @JavascriptInterface
        public String getUser() {
            String user = preference.getString("login_name", "") +
                    "," +
                    preference.getString("PASSWORD", "");

            return user;
        }


        @JavascriptInterface
        public void getBookDetail(String guidStr) {
            guid = guidStr;
            //  刷新界面
            bookDetailWeb.loadUrl(bookUrl);
        }

        @JavascriptInterface
        public void goBuyBook() {
            //   获取订单信息
            createOrder();

        }

        @JavascriptInterface
        public void finshBookDetail() {
            finish();
        }

//        @JavascriptInterface
//        public void downLoadRes(String path) {
//            downLoadBook(path);
//        }

        @JavascriptInterface
        public void isReadAll(boolean flg) {
            isReadAll = flg;
        }

        @JavascriptInterface
        public void readBook() {
            Intent intent = new Intent(BookDetailActivity.this, TkbsReaderActivity.class);
            intent.putExtra("BookId", guid);
            String resPath = Config.CIP_FILE_PATH + guid + ".tkbs";
            intent.putExtra("isLoacRead", isExist(resPath));
            intent.putExtra("isReadAll", isReadAll);
            startActivity(intent);
        }
    }
}
