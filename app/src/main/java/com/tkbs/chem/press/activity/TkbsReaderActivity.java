package com.tkbs.chem.press.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
import com.tkbs.chem.press.bean.HttpResponse;
import com.tkbs.chem.press.net.ApiCallback;
import com.tkbs.chem.press.util.Config;
import com.tkbs.chem.press.util.UiUtils;
import com.tkbs.chem.press.util.WebUserInfor;
import com.tkbs.chem.press.view.ReWebChomeClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TkbsReaderActivity extends BaseActivity implements View.OnClickListener, ReWebChomeClient.OpenFileChooserCallBack {

    @BindView(R.id.tkbs_read_web)
    WebView tkbsReadWeb;
    @BindView(R.id.img_back)
    TextView imgBack;
    @BindView(R.id.img_toc)
    TextView imgToc;
    @BindView(R.id.img_refresh)
    ImageView imgRefresh;
    @BindView(R.id.tv_download)
    TextView tvDownload;
    @BindView(R.id.tv_page_num)
    TextView tvPageNum;
    private String err_url;
    private int progressNum;
    private String bookId;
    private String readData;
    private String param3 = "";
    private boolean isLocaRead;
    private int readHistory;
    private boolean isReadAll;
    private String filePath;
    /**
     * handler处理消息机制
     */
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    imgRefresh.setVisibility(View.VISIBLE);
                    break;
                case 10:
                    showProgressDialog("loading...");
                    break;
                case 100:
                    dismissProgressDialog();
                    toastShow(R.string.download_done);
                    handler.removeMessages(100);
                    break;
                case 1000:
                    //阅读时长
                    addReadTime();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_tkbs_reader;
    }

    @Override
    protected void initdata() {
        imgRefresh.setOnClickListener(this);
        bookId = getIntent().getStringExtra("BookId");
        readData = getIntent().getStringExtra("ReadData");
        param3 = getIntent().getStringExtra("PARAM3");
        if (null == param3) {
            param3 = "";
        }
        isLocaRead = getIntent().getBooleanExtra("isLoacRead", false);
        isReadAll = getIntent().getBooleanExtra("isReadAll", false);
        if (isLocaRead) {
            readHistory = preference.getInt(bookId, 0);
//            toastShow("历史记录：" + readHistory);
        }
        // 阅读页面 隐藏下载
        tvDownload.setVisibility(View.GONE);
//        imgBack.getBackground().setAlpha(100);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TkbsReaderActivity.this.finish();
            }
        });
//        imgToc.getBackground().setAlpha(100);
        imgToc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TkbsReaderActivity.this, TKBSCatalogAvtivity.class);
                intent.putExtra("BookId", bookId);
                intent.putExtra("BookType", "tkbs");
                intent.putExtra("ISMULU", "tkbs");
                startActivityForResult(intent, 886);
            }
        });
        tvPageNum.setText("0/0");
        initWeb();
//        rlDialog.setOnClickListener(this);
    }

    @Override
    protected void initTitle() {

    }

    /**
     * 添加阅读记录
     */
    private void addReadTime() {
        //showProgressDialog();
        addSubscription(apiStores.addBookReadTIme(bookId), new ApiCallback<HttpResponse<Object>>() {
            @Override
            public void onSuccess(HttpResponse<Object> model) {
                if (model.isStatus()) {
                    handler.sendEmptyMessageDelayed(1000, 60000);
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

    /**
     * 获取下载路径
     */
    private void resLoadPath() {
        showProgressDialog();
        addSubscription(apiStores.getResDownLoadPath(bookId), new ApiCallback<HttpResponse<String>>() {
            @Override
            public void onSuccess(HttpResponse<String> model) {
                if (model.isStatus()) {
                    if ("".equals(model.getData())) {
                        toastShow("暂无资源");
                        finish();
                    } else {
                        dismissProgressDialog();
                        downLoadBook(model.getData());
                    }

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

    /**
     * 下载
     *
     * @param filePathStr
     */
    private void downLoadBook(String filePathStr) {
//        filePathStr = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1544000312107&di=9ad128d9e6117a8f0ced7c8e78ff1336&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F6%2F589a7fca946fe.jpg%3Fdown";
        Logger.e(filePathStr);
        filePath = Config.CIP_FILE_PATH + bookId + ".tkbs";
//        filePath = Config.CIP_FILE_PATH + File.separator + bookId + ".jpg";
        if (isExist(filePath)) {
            // 已经下载 直接阅读
            toastShow("已经下载 直接阅读");
            return;
        }
        Call<ResponseBody> call = apiStores.downloadFileWithUrl(filePathStr);
        showProgressDialog("下载中...");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d("TAG", "server contacted and has file");

                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            //发送通知，加入线程
                            Message msg = handler.obtainMessage();
                            //下载中
                            msg.what = 10;
                            //通知发送！
                            handler.sendMessage(msg);
                            boolean writtenToDisk = writeResponseBodyToDisk(response.body());
                            if (writtenToDisk) {
                                dismissProgressDialog();
                            }
//                            handler.removeMessages(msg.what);
                            msg = handler.obtainMessage();
                            //下载中
                            msg.what = 100;
                            //通知发送！
                            handler.sendMessage(msg);
                            Log.d("TAG", "file download was a success? " + writtenToDisk);
                        }
                    }.start();
//                    boolean writtenToDisk = writeResponseBodyToDisk(response.body());
//                    if (writtenToDisk) {
//                        dismissProgressDialog();
//                    }
//                    Log.d("TAG", "file download was a success? " + writtenToDisk);
                } else {
                    Log.d("TAG", "server contact failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("TAG", "error");
                dismissProgressDialog();
            }
        });


    }

    /**
     * 判断指定名称的文件在SD卡上是否存在
     */
    public boolean isExist(String path) {
        File file = new File(path);
        return file.exists();
    }

    /**
     * 将下载的文件保存至手机
     *
     * @param body
     * @return
     */
    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            //  change the file location/name according to your needs
            File futureStudioIconFile = new File(filePath);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d("TAG", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    @OnClick({R.id.img_refresh, R.id.tv_download})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_refresh:
                toastShow("refresh");
                break;
            case R.id.tv_download:
                // download
                resLoadPath();
                break;
            default:
                break;
        }
    }

    private void initWeb() {
        WebSettings setting = tkbsReadWeb.getSettings();
        setting.setJavaScriptEnabled(true);
        //允许加载javascript
        setting.setSupportZoom(true);
        //允许缩放
        setting.setBuiltInZoomControls(true);
        //原网页基础上缩放
        setting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        setting.setUseWideViewPort(true);
        setting.setDisplayZoomControls(false);
        //任意比例缩放
        setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        setting.setLoadWithOverviewMode(true);
        setting.setSaveFormData(true);
        setting.setDomStorageEnabled(true);
        setting.setJavaScriptCanOpenWindowsAutomatically(true);
        setting.setSupportMultipleWindows(true);
        tkbsReadWeb.addJavascriptInterface(new TKBSJavaScriptInterface(), "TKBS");
        /*****************************************************************
         * 在点击请求的是链接时才会调用，重写此方法返回true表明点击网页里
         * 面的链接还是在当前的WebView里跳转，不会跳到浏览器上运行。
         *****************************************************************/
        tkbsReadWeb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showProgressDialog();
                err_url = url;
                imgRefresh.setVisibility(View.GONE);
                handler.sendEmptyMessage(1000);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Log.e("qiou", "errow" + description);
                String data = "  ";
                view.loadUrl("javascript:document.body.innerHTML=\"" + data + "\"");
                err_url = failingUrl;
                view.stopLoading();
                view.clearView();
                //发送通知，加入线程
                Message msg = handler.obtainMessage();
                //通知加载自定义404页面
                msg.what = 1;
                //通知发送！
                handler.sendMessage(msg);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                if (null != param3 && param3.length() > 0) {
//                    tkbsReadWeb.loadUrl("javascript:getCataLog(" + param3 + ")");
//                }
            }
        });
        tkbsReadWeb.setWebChromeClient(new ReWebChomeClient(this) {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress >= 50) {
                    dismissProgressDialog();
                }
            }
        });
        if (null != param3 && param3.length() > 0) {
            Logger.e(Config.API_SERVER + "hello/read.html?bookId="
                    + bookId + "&sign=" + WebUserInfor.builder(TkbsReaderActivity.this).getString()
                    + "&dir=true" + "&num=" + param3);
            tkbsReadWeb.loadUrl(Config.API_SERVER + "hello/read.html?bookId="
                    + bookId + "&sign=" + WebUserInfor.builder(TkbsReaderActivity.this).getString()
                    + "&dir=true" + "&num=" + param3);
        } else {
            Logger.e(Config.API_SERVER + "hello/read.html?bookId="
                    + bookId + "&sign=" + WebUserInfor.builder(TkbsReaderActivity.this).getString()
                    + "&bookType=tkbs");
            tkbsReadWeb.loadUrl(Config.API_SERVER + "hello/read.html?bookId="
                    + bookId + "&sign=" + WebUserInfor.builder(TkbsReaderActivity.this).getString()
                    + "&bookType=tkbs");
        }
//        tkbsReadWeb.loadUrl("http://www.jiankeyan.com/Index/Index.aspx");


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // 根据上面发送过去的请求码来区别
        switch (requestCode) {
            case 886:
                if (null != data) {
                    param3 = data.getStringExtra("PARAM3");
                    tkbsReadWeb.loadUrl("javascript:getCataLog(" + param3 + ")");
//                    tkbsReadWeb.loadUrl(Config.API_SERVER + "hello/read.html?bookId="
//                            + bookId + "&sign=" + WebUserInfor.builder(TkbsReaderActivity.this).getString()
//                            + "&dir=true" + "&num=" + param3);
                }
            default:
                break;
        }
    }

    @Override
    public void openFileChooserCallBack(ValueCallback<Uri> uploadMsg, String acceptType) {

    }

    @Override
    public void finish() {
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();
        tkbsReadWeb.removeAllViews();
        tkbsReadWeb.destroy();
        // 更新阅读记录
        if (isLocaRead) {
            SharedPreferences.Editor edit = preference.edit();
            edit.putInt(bookId, readHistory);
            edit.commit();
        }
        super.finish();
    }

    @Override
    protected synchronized void onDestroy() {
        if (tkbsReadWeb != null) {
            // 销毁
            tkbsReadWeb.destroy();
            tkbsReadWeb = null;
        }
        handler.removeMessages(1000);
        super.onDestroy();
    }

    private class TKBSJavaScriptInterface {
        Context ctx = TkbsReaderActivity.this;

        TKBSJavaScriptInterface() {
        }


        @JavascriptInterface
        public void ShowProgressbar(int progress) {
            progressNum = progress;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (progressNum >= 50) {
                        dismissProgressDialog();
                    } else {
                        showProgressDialog();
                    }
                }
            });
        }

        @JavascriptInterface
        public void showPage(final int currentpage, final int totalPage) {
//            Logger.e(currentpage + "/" + totalPage);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvPageNum.setText(currentpage + "/" + totalPage);
                }
            });

        }

        @JavascriptInterface
        public String getReadData() {
            return readData;
        }

        @JavascriptInterface
        public int getReadHistory() {
            return readHistory;
        }

        @JavascriptInterface
        public String getUser() {
            String user = preference.getString("login_name", "") +
                    "," +
                    preference.getString("PASSWORD", "");

            return user;
        }

        @JavascriptInterface
        public String getBookGuid() {
            return bookId;
        }

        @JavascriptInterface
        public String readTkbs(String guid, int pageNum) {
            readHistory = pageNum;
            String result = new String(UiUtils.readTkbs(guid, pageNum));
            return result;
        }

        @JavascriptInterface
        public boolean isLoacRead() {
            return isLocaRead;
        }

        @JavascriptInterface
        public String getParamNum() {
            return param3;
        }

        @JavascriptInterface
        public void ShowToast(String Message) {
            toastShow(Message);
        }

        @JavascriptInterface
        public void showBookCatalog() {
            Intent intent = new Intent(TkbsReaderActivity.this, TKBSCatalogAvtivity.class);
            intent.putExtra("BookId", bookId);
            intent.putExtra("BookType", "tkbs");
            intent.putExtra("ISMULU", "tkbs");
            startActivityForResult(intent, 886);
        }

        @JavascriptInterface
        public void finshRead() {
            finish();
        }

        @JavascriptInterface
        public boolean isReadAll() {
            return isReadAll;
        }
    }
}
