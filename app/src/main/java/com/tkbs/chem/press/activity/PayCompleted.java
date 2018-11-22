package com.tkbs.chem.press.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkbs.chem.press.R;
import com.tkbs.chem.press.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

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

    }

    @Override
    protected void initTitle() {

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
}
