package com.tkbs.chem.press.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tkbs.chem.press.R;
import com.tkbs.chem.press.base.BaseActivity;
import com.tkbs.chem.press.base.BaseApplication;
import com.tkbs.chem.press.bean.BookDetailBean;
import com.tkbs.chem.press.bean.HttpResponse;
import com.tkbs.chem.press.bean.OrderInfo;
import com.tkbs.chem.press.bean.PayResult;
import com.tkbs.chem.press.net.ApiCallback;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class PayActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.img_book_cover)
    ImageView imgBookCover;
    @BindView(R.id.tv_book_name)
    TextView tvBookName;
    @BindView(R.id.tv_book_author)
    TextView tvBookAuthor;
    @BindView(R.id.tv_book_price)
    TextView tvBookPrice;
    @BindView(R.id.cb_recharge_wx)
    CheckBox cbRechargeWx;
    @BindView(R.id.cb_recharge_zfb)
    CheckBox cbRechargeZfb;
    @BindView(R.id.tv_pay)
    TextView tvPay;
    /**
     * 支付方式 1 微信 2 支付宝
     */
    private String paytype = "1";

    private String guid;

    private BookDetailBean bookDetailData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay;
    }

    @Override
    protected void initdata() {
        cbRechargeZfb.setOnCheckedChangeListener(this);
        cbRechargeWx.setOnCheckedChangeListener(this);
        guid = getIntent().getStringExtra("guid");
        getBookDetail();
        // TODO 支付宝 沙箱环境
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
    }

    /**
     * 获取图书详情
     */
    private void getBookDetail() {
        showProgressDialog();
        addSubscription(apiStores.getBookDetail(guid), new ApiCallback<HttpResponse<BookDetailBean>>() {
            @Override
            public void onSuccess(HttpResponse<BookDetailBean> model) {
                if (model.isStatus()) {
                    //  添加书籍记录
                    bookDetailData = model.getData();
                    Glide.with(PayActivity.this).load(bookDetailData.getCover())
                            .apply(BaseApplication.options)
                            .into(imgBookCover);
                    tvBookName.setText(bookDetailData.getTitle());
                    tvBookAuthor.setText(bookDetailData.getAuthor());
                    String priceStr = String.valueOf(bookDetailData.getPrice());
                    tvBookPrice.setText("￥" + priceStr);
                    String surePayStr = getString(R.string.sure_pay_amount);
                    tvPay.setText(surePayStr + priceStr);

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

    @Override
    protected void initTitle() {
        title.setText(R.string.str_pay);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_recharge_wx:
                if (isChecked) {
                    cbRechargeZfb.setChecked(false);
                    paytype = "1";
                } else {
                    cbRechargeZfb.setChecked(true);
                    paytype = "2";
                }
                break;
            case R.id.cb_recharge_zfb:
                if (isChecked) {
                    cbRechargeWx.setChecked(false);
                    paytype = "2";
                } else {
                    cbRechargeWx.setChecked(true);
                    paytype = "1";
                }
                break;

            default:
                break;
        }
    }

    @OnClick({R.id.back, R.id.tv_pay})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_pay:
                if (paytype.equals("2")) {
                    // 支付宝支付
                    toastShow("支付宝支付");
                    payReadyAlipay();
                } else {
                    // 微信支付
                    toastShow("微信支付");
//                    payReadyWX();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 支付宝支付
     */
    private void payReadyAlipay() {
        showProgressDialog();
        addSubscription(apiStores.payReadyAlipay(guid), new ApiCallback<HttpResponse<OrderInfo>>() {
            @Override
            public void onSuccess(HttpResponse<OrderInfo> model) {
                if (model.isStatus()) {
                    zfbpay(model.getData().getOrderInfo());
                } else {
                    toastShow(model.getErrorDescription());
                }
                dismissProgressDialog();
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

    private void zfbpay(final String orderinfo) {

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PayActivity.this);
                Map<String, String> result = alipay.payV2(orderinfo, true);
                Logger.addLogAdapter(new AndroidLogAdapter());
//                Logger.e(result.toString());

                Message msg = new Message();
                msg.what = 1;
                msg.obj = result;
                zfbHandler.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private Handler zfbHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    // 同步返回需要验证的信息
                    String resultInfo = payResult.getResult();
                    Logger.e(resultInfo);
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (resultStatus.equals("9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        paysuccess();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        // 服务器校验
                        toastShow("支付失败");
                        checkOrder(false);
                    }
                    break;
                }
                case 2:
                    checkOrder(true);
                    dismissProgressDialog();
                    Intent intent = new Intent(PayActivity.this, PayCompleted.class);
                    intent.putExtra("guid", guid);
                    startActivity(intent);
                    finish();
                    break;

                default:
                    break;
            }
        }
    };

    private void paysuccess() {
        // 和服务器校验
        showProgressDialog("订单生成中...");
        zfbHandler.sendEmptyMessageDelayed(2, 3000);

    }

    /**
     * 订单信息确认和查询
     */
    private void checkOrder(final boolean flg) {
//        showProgressDialog("订单生成中....");
//        OrderRequest requestData = new OrderRequest();
//        requestData.setStudyActivityId(StudyActivityId);
//        requestData.setMemberId(preference.getString("userId", ""));
//        requestData.setOrderState(flg);
//        Gson gson = new Gson();
//        String route = gson.toJson(requestData);
//        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), route);
//        addSubscription(apiStores.ActOrder(body), new ApiCallback<HttpResponse<OrderBen>>() {
//            @Override
//            public void onSuccess(HttpResponse<OrderBen> model) {
//                if ("0".equals(model.getStatus())) {
//                    if (flg) {// 查询状态
//                        if (model.getData().isFlag()) {
//                            toastShow("支付成功");
//                            //刷新主界面
//                            EventBus.getDefault().post(new MessageEvent("2"));
//                            finish();
//                        } else {
//                            zfbHandler.sendEmptyMessageDelayed(2, 1500);
//                        }
//
//                    } else {//通知后台支付失败
//                        toastShow("支付失败");
//                    }
//
//                } else {
//                    toastShow(model.getErrorDescription());
//                }
//
//            }
//
//            @Override
//            public void onFailure(String msg) {
//                toastShow(msg);
//            }
//
//            @Override
//            public void onFinish() {
//                dismissProgressDialog();
//            }
//        });

    }
}
