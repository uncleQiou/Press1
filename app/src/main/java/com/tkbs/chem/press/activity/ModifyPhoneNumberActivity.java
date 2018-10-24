package com.tkbs.chem.press.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkbs.chem.press.R;
import com.tkbs.chem.press.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class ModifyPhoneNumberActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_tips)
    TextView tvTips;
    @BindView(R.id.ll_phone)
    LinearLayout llPhone;
    @BindView(R.id.tv_passwird)
    TextView tvPasswird;
    @BindView(R.id.ed_phone_number)
    EditText edPhoneNumber;
    @BindView(R.id.ll_phone_number)
    LinearLayout llPhoneNumber;
    @BindView(R.id.ed_check_code)
    EditText edCheckCode;
    @BindView(R.id.ll_check_code)
    LinearLayout llCheckCode;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_right)
    TextView tvRight;
    private int state = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify_phone_number;
    }

    @Override
    protected void initdata() {
        //  修改手机号码 分三步  1、密码验证 2、输入手机号 3、输入验证码
        SettingView();

    }

    private void SettingView() {
        if (state == 1) {
            // 1、密码验证
            tvTips.setText(R.string.input_password);
            tvTips.setVisibility(View.VISIBLE);
            llPhone.setVisibility(View.GONE);
            llPhoneNumber.setVisibility(View.VISIBLE);
            tvPasswird.setText(R.string.login_password);
            edPhoneNumber.setHint(R.string.use_of_check);
            llCheckCode.setVisibility(View.GONE);
        } else if (state == 2) {
            // 2、输入手机号
            tvTips.setText("");
            tvTips.setVisibility(View.GONE);
            llPhone.setVisibility(View.VISIBLE);
            llPhoneNumber.setVisibility(View.VISIBLE);
            tvPasswird.setText(R.string.new_phone_number);
            edPhoneNumber.setHint(R.string.input_phone_number);
            llCheckCode.setVisibility(View.GONE);
        } else if (state == 3) {
            tvTips.setText("请输入23234234的短信验证码");
            tvTips.setVisibility(View.VISIBLE);
            llPhone.setVisibility(View.GONE);
            llPhoneNumber.setVisibility(View.GONE);
            tvPasswird.setText(R.string.new_phone_number);
            edPhoneNumber.setHint(R.string.input_phone_number);
            llCheckCode.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initTitle() {
        title.setText(R.string.modify_phone_title);
    }

    @OnClick({R.id.back, R.id.btn_next})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_next:
                if (state == 1) {
                    state = 2;
                } else if (state == 2) {
                    state = 3;
                    btnNext.setText(R.string.done);
                }
                SettingView();
                break;
            default:
                break;
        }
    }
}
