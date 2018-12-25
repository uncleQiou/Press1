package com.tkbs.chem.press.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkbs.chem.press.R;
import com.tkbs.chem.press.base.BaseActivity;
import com.tkbs.chem.press.util.Config;
import com.tkbs.chem.press.util.UiUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_phone_number)
    TextView tvPhoneNumber;
    @BindView(R.id.ll_phone)
    LinearLayout llPhone;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.ll_version)
    LinearLayout llVersion;
    @BindView(R.id.tv_contact)
    TextView tvContact;
    @BindView(R.id.ll_contact)
    LinearLayout llContact;
    @BindView(R.id.tv_exit)
    TextView tvExit;
    @BindView(R.id.ll_change_password)
    LinearLayout llChangePassword;
    // 用户身份
    private int user_type = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initdata() {
        String phone = preference.getString(Config.PHONE, "");
        tvPhoneNumber.setText(phone);
        String version = "当前版本：V" + UiUtils.getVersionCode(this);
        tvVersion.setText(version);
        tvContact.setText("010-19980232");
        user_type = preference.getInt(Config.MEMBER_TYPE, 3);
        if (2 == user_type) {
            llPhone.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initTitle() {
        title.setText(R.string.setting);
    }

    @OnClick({R.id.back, R.id.ll_phone, R.id.ll_version, R.id.ll_contact, R.id.tv_exit, R.id.ll_change_password})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ll_phone:
                toastShow(R.string.edit_phone_number);
                startActivity(new Intent(SettingActivity.this, ModifyPhoneNumberActivity.class));
                break;
            case R.id.ll_version:
                toastShow(R.string.version_update);
                break;
            case R.id.ll_contact:
                toastShow(R.string.contact_us);
                break;
            case R.id.tv_exit:
                toastShow(R.string.exit_logon);
                startActivityForResult(new Intent(SettingActivity.this, LoginActivity.class), Config.ACCOUNT_SWITCHING);
                break;
            case R.id.ll_change_password:
                startActivity(new Intent(SettingActivity.this, ChangePasswordWebActivity.class));
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
