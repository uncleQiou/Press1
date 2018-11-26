package com.tkbs.chem.press.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tkbs.chem.press.R;
import com.tkbs.chem.press.base.BaseActivity;
import com.tkbs.chem.press.base.BaseApplication;
import com.tkbs.chem.press.bean.BookDetailBean;
import com.tkbs.chem.press.bean.HttpResponse;
import com.tkbs.chem.press.bean.OrderInfoBean;
import com.tkbs.chem.press.net.ApiCallback;
import com.tkbs.chem.press.util.Config;
import com.tkbs.chem.press.util.MessageEvent;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class PayCompleted extends BaseActivity implements View.OnClickListener {

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
    @BindView(R.id.tv_file_size)
    TextView tvFileSize;
    @BindView(R.id.tv_order_number)
    TextView tvOrderNumber;
    @BindView(R.id.tv_transaction_number)
    TextView tvTransactionNumber;
    @BindView(R.id.tv_pay_way)
    TextView tvPayWay;
    @BindView(R.id.tv_transaction_amount)
    TextView tvTransactionAmount;
    @BindView(R.id.tv_download)
    TextView tvDownload;
    private OrderInfoBean orderDetailData;
    private String guid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay_completed;
    }

    @Override
    protected void initdata() {
        guid = getIntent().getStringExtra("guid");
        getOrderDetail();
    }

    @Override
    protected void initTitle() {
        title.setText(getString(R.string.pay_success));
    }

    /**
     * 获取图书详情
     */
    private void getOrderDetail() {
        showProgressDialog();
        addSubscription(apiStores.checkOrderInfo(guid), new ApiCallback<HttpResponse<OrderInfoBean>>() {
            @Override
            public void onSuccess(HttpResponse<OrderInfoBean> model) {
                if (model.isStatus()) {
                    //  添加书籍记录
                    orderDetailData = model.getData();
                    Glide.with(PayCompleted.this).load(orderDetailData.getCover())
                            .apply(BaseApplication.options)
                            .into(imgBookCover);
                    tvBookName.setText(orderDetailData.getTitle());
                    tvOrderNumber.setText(orderDetailData.getOrder_number());
                    tvTransactionNumber.setText(orderDetailData.getPay_order_number());
                    if (Config.ZFB_PAY_TYPE == orderDetailData.getPay_type()) {
                        tvPayWay.setText(R.string.pay_by_zfb);
                    } else {
                        tvPayWay.setText(R.string.pay_by_wechat);
                    }
                    tvTransactionAmount.setText("￥：" + orderDetailData.getPay_price());
                } else {
                    toastShow(model.getErrorDescription());
                }

            }

            @Override
            public void onFailure(String msg) {
                toastShow(msg);
                dismissProgressDialog();
            }

            @Override
            public void onFinish() {
                dismissProgressDialog();

            }
        });
    }

    @OnClick({R.id.back, R.id.tv_download})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_download:
                toastShow("下载功能未开放");
                break;
            default:
                break;
        }
    }

    @Override
    public void finish() {
        //  通知详情页面 刷新
        EventBus.getDefault().post(new MessageEvent("PaySuccess"));
        super.finish();
    }
}
