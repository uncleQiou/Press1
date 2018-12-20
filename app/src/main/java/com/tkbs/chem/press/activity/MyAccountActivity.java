package com.tkbs.chem.press.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkbs.chem.press.R;
import com.tkbs.chem.press.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class MyAccountActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_recharge)
    TextView tvRecharge;
    @BindView(R.id.tv_my_token)
    TextView tvMyToken;
    @BindView(R.id.ll_recharge_record)
    LinearLayout llRechargeRecord;
    @BindView(R.id.ll_pay_record)
    LinearLayout llPayRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_account;
    }

    @Override
    protected void initdata() {
        tvMyToken.setText("898989888点");

    }

    @Override
    protected void initTitle() {

    }

    @OnClick({R.id.img_back, R.id.ll_recharge_record, R.id.ll_pay_record, R.id.tv_recharge})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.ll_recharge_record:
                startActivity(new Intent(MyAccountActivity.this, RechargeRecordActivity.class));
                break;
            case R.id.ll_pay_record:
                startActivity(new Intent(MyAccountActivity.this, PayRecordActivity.class));
                break;
            case R.id.tv_recharge:
                startActivity(new Intent(MyAccountActivity.this, RechargeActivity.class));
                break;
            default:
                break;
        }

    }
}
