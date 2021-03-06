package com.tkbs.chem.press.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.tkbs.chem.press.R;
import com.tkbs.chem.press.base.BaseActivity;
import com.tkbs.chem.press.bean.HttpResponse;
import com.tkbs.chem.press.net.ApiCallback;
import com.tkbs.chem.press.util.Config;

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
        tvMyToken.setText("0CIP币");
        getMyAccountBlance();
    }

    @Override
    protected void initTitle() {

    }

    /**
     * 支付
     */
    private void getMyAccountBlance() {
        showProgressDialog();
        addSubscription(apiStores.myAccountBlance(), new ApiCallback<HttpResponse<Integer>>() {
            @Override
            public void onSuccess(HttpResponse<Integer> model) {
                // 通知html5页面进行刷新 购买完成 reflushData
                if (null != model.getData()) {
                    tvMyToken.setText(model.getData() + "CIP币");
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
                int user_type = preference.getInt(Config.MEMBER_TYPE, 3);
                if (user_type == 5) {
                    startActivityForResult(new Intent(MyAccountActivity.this, LoginActivity.class), Config.ACCOUNT_SWITCHING);
                } else {
                    startActivity(new Intent(MyAccountActivity.this, RechargeActivity.class));
                    finish();
                }
                break;
            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case Config.ACCOUNT_SWITCHING:
                    setResult(RESULT_OK);
                    finish();
                    break;
                default:
                    break;
            }
        }
    }
}
