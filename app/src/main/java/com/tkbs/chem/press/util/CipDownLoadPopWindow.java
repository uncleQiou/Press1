package com.tkbs.chem.press.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.tkbs.chem.press.R;
import com.tkbs.chem.press.base.BaseApplication;

import java.util.Hashtable;

/**
 * 作者    qyl
 * 时间    2019/8/14 9:50
 * 文件    Press
 * 描述
 */
public class CipDownLoadPopWindow {
    private static CipDownLoadPopWindow instance;
    private Activity mContext;
    private PopupWindow mPopwindow;
    private View mPopView = null;
    private ImageView img_close;
    private ImageView img_qr_code;
    private ImageView tv_android;
    private ImageView tv_ios;

    private View mView;
    private int QR_WIDTH = 500;
    private int QR_HEIGHT = 500;
    private String imgUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1565763308233&di=a53f09bb89931a44b30787f3e19d6205&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201801%2F20%2F20180120123551_mE8UG.thumb.700_0.png";

    /**
     * 私有化构造方法，变成单例模式
     */
    private CipDownLoadPopWindow() {

    }

    /**
     * 对外提供一个该类的实例，考虑多线程问题，进行同步操作
     */
    public static CipDownLoadPopWindow getInstance() {
        if (instance == null) {
            synchronized (CipDownLoadPopWindow.class) {
                if (instance == null) {
                    instance = new CipDownLoadPopWindow();
                }
            }
        }
        return instance;
    }

    public void showPopWindow(final Activity context, View view, String img) {
        mContext = context;
        mView = view;
        if (img.length() > 0) {
            imgUrl = img;
        }
        imgUrl = Config.API_SERVER + "download/qrimage";
        mPopView = context.getLayoutInflater().inflate(R.layout.cip_load_pop_window, null);
        // 创建一个PopuWidow对象
        mPopwindow = new PopupWindow(mPopView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        // 使其聚集 ，要想监听菜单里控件的事件就必须要调用此方法
        mPopwindow.setFocusable(true);
        // 设置不允许在外点击消失
        mPopwindow.setOutsideTouchable(false);
        // 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        mPopwindow.setBackgroundDrawable(new BitmapDrawable());
        // PopupWindow的显示及位置设置
        mPopwindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        initView();
    }

    private void initView() {
        img_close = (ImageView) mPopView.findViewById(R.id.img_close);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopwindow.dismiss();
            }
        });
        img_qr_code = (ImageView) mPopView.findViewById(R.id.img_qr_code);
        tv_android = (ImageView) mPopView.findViewById(R.id.tv_android);
//        tv_android.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                createQRImage("你是我的小苹果");
//            }
//        });
        tv_ios = (ImageView) mPopView.findViewById(R.id.tv_ios);
//        tv_ios.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                createQRImage("小丫小苹果");
//            }
//        });
        Glide.with(mContext).load(imgUrl)
                .apply(BaseApplication.options)
                .into(img_qr_code);
    }

    /**
     * QR_WIDETH 二维码宽度 自行设置
     * QR_HIGHT 二维码高度 自行设置
     * imgQrcode 一个ImageView
     */
    private Bitmap createQRImage(String address) {
        Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
        try {
            //判断URL合法性
            if (address == null || "".equals(address) || address.length() < 1) {
                return null;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(address,
                    BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            //下面这里按照二维码的算法，逐个生成二维码的图片，
            //两个for循环是图片横列扫描的结果
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }
            //生成二维码图片的格式，使用ARGB_8888
            bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            //显示到一个ImageView上面
            img_qr_code.setImageBitmap(bitmap);
        } catch (com.google.zxing.WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
