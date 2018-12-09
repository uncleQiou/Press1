package com.tkbs.chem.press.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
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
import com.tkbs.chem.press.util.Config;
import com.tkbs.chem.press.util.MessageEvent;
import com.tkbs.chem.press.util.UiUtils;
import com.tkbs.chem.press.view.ReWebChomeClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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
    private String filePath;
    /**
     * 文件下载目标路径
     */
    private String CIP_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "CIP" + File.separator;

    private String bookUrl = Config.API_SERVER + "hello/book_detail.html";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_book_detail;
    }


    @Override
    protected void initdata() {
        guid = getIntent().getStringExtra("guid");
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
     * 下载
     *
     * @param filePathStr
     */
    private void downLoadBook(String filePathStr) {
        filePathStr = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1544000312107&di=9ad128d9e6117a8f0ced7c8e78ff1336&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F6%2F589a7fca946fe.jpg%3Fdown";
        Logger.e(filePathStr);
//        filePath = CIP_FILE_PATH + File.separator + guid + ".tkbs";
        filePath = CIP_FILE_PATH + File.separator + guid + ".jpg";
        if (isExist()) {
            // 已经下载 直接阅读
            toastShow("已经下载 直接阅读");
            return;
        }
        Call<ResponseBody> call = apiStores.downloadFileWithUrl(filePathStr);
        showProgressDialog("下载中...");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d("TAG", "server contacted and has file");

                    boolean writtenToDisk = writeResponseBodyToDisk(response.body());
                    if (writtenToDisk) {
                        Intent intent = new Intent();
                        File file = new File(filePath);
                        //设置标记
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //动作，查看
                        intent.setAction(Intent.ACTION_VIEW);
                        //设置类型
                        intent.setDataAndType(Uri.fromFile(file), UiUtils.getMIMEType(file));
                        BookDetailActivity.this.startActivity(intent);
                    }
                    Log.d("TAG", "file download was a success? " + writtenToDisk);
                } else {
                    Log.d("TAG", "server contact failed");
                }
                dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("TAG", "error");
                dismissProgressDialog();
            }
        });


    }

    /**
     * 在SD卡上创建指定名称的目录
     *
     * @param
     */
    private File createDir() {
        File file = new File(CIP_FILE_PATH);
        file.mkdir();
        return file;
    }

    /**
     * 判断指定名称的文件在SD卡上是否存在
     */
    public boolean isExist() {
        File file = new File(filePath);
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
            // todo change the file location/name according to your needs
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
        public void viewDirectory() {
            toastShow("查看目录");
        }


        @JavascriptInterface
        public void getBookDetail(String guidStr) {
            guid = guidStr;
            //  刷新界面
            bookDetailWeb.loadUrl(bookUrl);
        }

        @JavascriptInterface
        public void goBuyBook() {
            Intent intent = new Intent(BookDetailActivity.this, PayActivity.class);
            intent.putExtra("guid", guid);
            startActivity(intent);
        }

        @JavascriptInterface
        public void finshBookDetail() {
            finish();
        }

        @JavascriptInterface
        public void readBook() {
            Intent intent = new Intent(BookDetailActivity.this, TkbsReaderActivity.class);
            intent.putExtra("BookId", guid);
            startActivity(intent);
        }
    }
}
