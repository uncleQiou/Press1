package com.tkbs.chem.press.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tkbs.chem.press.R;
import com.tkbs.chem.press.base.BaseActivity;
import com.tkbs.chem.press.base.BaseApplication;
import com.tkbs.chem.press.bean.HttpResponse;
import com.tkbs.chem.press.bean.OrderInfo;
import com.tkbs.chem.press.bean.PayResult;
import com.tkbs.chem.press.bean.RechargeConfigDataBean;
import com.tkbs.chem.press.bean.RechargeResult;
import com.tkbs.chem.press.net.ApiCallback;
import com.tkbs.chem.press.util.Config;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class RechargeActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.id_rechargelayout)
    TagFlowLayout idRechargelayout;
    @BindView(R.id.cb_recharge_wx)
    CheckBox cbRechargeWx;
    @BindView(R.id.cb_recharge_zfb)
    CheckBox cbRechargeZfb;
    @BindView(R.id.tv_kindly_reminder)
    TextView tvKindlyReminder;
    @BindView(R.id.tv_recharge)
    TextView tvRecharge;
    private TagAdapter tagAdapter;

    private View rechargeItemView;

//    private String[] mVals = new String[]
//            {"100点", "200点", "300点 ", "400点", "500点", "600点",
//                    "700点", "800点", "900点", "1000点", "2000点",
//                    "3000点", "4000点", "5000点", "10000点"};

    private List<RechargeConfigDataBean.MapListBean> configList;

    private String topicTips;

    /**
     * 支付方式 1 微信 2 支付宝
     */
    private String paytype = "1";
    private LayoutInflater inflate;
    private IWXAPI api;

    private int recharge_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void initdata() {
        cbRechargeZfb.setOnCheckedChangeListener(this);
        cbRechargeWx.setOnCheckedChangeListener(this);
        // TODO 支付宝 沙箱环境
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        // 微信
        api = WXAPIFactory.createWXAPI(this, Config.WX_APP_ID);
        //  获取充值列表
        getConfigData();

    }

    private void initConfigList() {
        // 充值列表数据填充
        final LayoutInflater mInflater = LayoutInflater.from(RechargeActivity.this);
        tagAdapter = new TagAdapter<RechargeConfigDataBean.MapListBean>(configList) {
            @Override
            public View getView(FlowLayout parent, int position, RechargeConfigDataBean.MapListBean config) {
                rechargeItemView = mInflater.inflate(R.layout.recharge_item,
                        idRechargelayout, false);
                LinearLayout ll_recharge_item = (LinearLayout) rechargeItemView.findViewById(R.id.ll_recharge_item);
                TextView tv_token_num = (TextView) rechargeItemView.findViewById(R.id.tv_token_num);
                TextView tv_rmb_num = (TextView) rechargeItemView.findViewById(R.id.tv_rmb_num);
                tv_token_num.setText(config.getFillValue() + "CIP币");
                String text_pay = String.format(getResources().getString(R.string.price_rmb), config.getPayPrice());
                tv_rmb_num.setText(text_pay);
                return rechargeItemView;
            }
        };
        idRechargelayout.setAdapter(tagAdapter);
        // 默认选中第一个
        tagAdapter.setSelectedList(0);

        idRechargelayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Logger.e(configList.get(position).getPayPrice().toString());
                recharge_num = Integer.valueOf(configList.get(position).getPayPrice());
                return true;
            }
        });
        // 设置温馨提示内容 tv_kindly_reminder
        tvKindlyReminder.setText(topicTips.replace("。", "。\n"));
        recharge_num = Integer.valueOf(configList.get(0).getPayPrice());
    }

    /**
     * 获取充值配置
     */
    private void getConfigData() {
        showProgressDialog();
        addSubscription(apiStores.getRechargeConfig(), new ApiCallback<HttpResponse<RechargeConfigDataBean>>() {
            @Override
            public void onSuccess(HttpResponse<RechargeConfigDataBean> model) {

                if (model.isStatus()) {
                    configList = model.getData().getMapList();
                    topicTips = model.getData().getReminder();
                    initConfigList();
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
        title.setText(R.string.recharge_center);
    }

    @OnClick({R.id.back, R.id.tv_recharge})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_recharge:
                //  用户是游客账户 去注册
                int user_type = preference.getInt(Config.MEMBER_TYPE, 3);
                if (user_type == 5) {
                    startActivity(new Intent(RechargeActivity.this, LoginActivity.class));
                } else {
                    if (paytype.equals("2")) {
                        // 支付宝支付
                        toastShow("支付宝支付");
                        payReadyAlipay();
                    } else {
                        // 微信支付
                        toastShow("微信支付");
                        payReadyWX();
                    }
                }


                break;
            default:
                break;

        }
    }

    /**
     * 获取微信支付数据
     */
    private RechargeResult.WeChat payMent;

    /**
     * 微信支付
     */
    private void payReadyWX() {
        // TODO 微信接口无法访问
        showProgressDialog();
        addSubscription(apiStores.payReadyWeChat(recharge_num), new ApiCallback<HttpResponse<RechargeResult.WeChat>>() {
            @Override
            public void onSuccess(HttpResponse<RechargeResult.WeChat> model) {
                if (model.isStatus()) {
                    payMent = model.getData();
                    wxpay();
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

    private void wxpay() {
        api.registerApp(payMent.getAppId());
        boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        if (!isPaySupported) {
            Toast.makeText(RechargeActivity.this, "您的微信版本太低，不支持支付功能", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(RechargeActivity.this, "获取订单中...", Toast.LENGTH_SHORT).show();
        try {
            PayReq request = new PayReq();
//            request.appId = "wxd930ea5d5a258f4f";
//            request.partnerId = "1900000109";
//            request.prepayId = "1101000000140415649af9fc314aa427";
//            request.nonceStr = "1101000000140429eb40476f8896f4c9";
//            request.timeStamp = "1398746574";
//            request.sign = "7FFECB600D7157C5AA49810D2D8F28BC2811827B";
            request.appId = payMent.getAppId();
            request.partnerId = payMent.getPartnerId();
            request.prepayId = payMent.getPrepayId();
            request.packageValue = "Sign=WXPay";
            request.nonceStr = payMent.getNoncestr();
            request.timeStamp = payMent.getTimestamp();
            request.sign = payMent.getSign();
            api.sendReq(request);

        } catch (Exception e) {
            Toast.makeText(RechargeActivity.this, "异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void wxpayresult(Integer progress) {
        if (progress == 0) {//支付成功
            paysuccess();
//            Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
//            finish();
        } else {
//            checkOrder(false);
            toastShow("pay fail ");
        }
    }

    /**
     * 支付宝支付
     */
    private void payReadyAlipay() {
        showProgressDialog();
        addSubscription(apiStores.payReadyAlipay(recharge_num), new ApiCallback<HttpResponse<OrderInfo>>() {
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
        Logger.e(orderinfo);

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(RechargeActivity.this);
                Map<String, String> result = alipay.payV2(orderinfo, true);
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
//                    Intent intent = new Intent(PayActivity.this, PayCompleted.class);
//                    intent.putExtra("guid", guid);
//                    startActivity(intent);
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_recharge_wx:
                // 2019年6月3日 隐藏支付宝支付方式
                cbRechargeWx.setChecked(true);
                cbRechargeZfb.setChecked(false);
                paytype = "1";
//                if (isChecked) {
//                    cbRechargeZfb.setChecked(false);
//                    paytype = "1";
//                } else {
//                    cbRechargeZfb.setChecked(true);
//                    paytype = "2";
//                }
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
}
