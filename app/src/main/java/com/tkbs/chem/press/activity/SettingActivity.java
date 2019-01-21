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

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

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
//                startActivity(new Intent(SettingActivity.this, ChangePasswordWebActivity.class));
                showShare();
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

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
//        oks.disableSSOWhenAuthorize();
        //设置一个总开关，用于在分享前若需要授权，则禁用sso功能  添加这个才成功
        oks.disableSSOWhenAuthorize();
        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle("分享测试");
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url在微信、微博，Facebook等平台中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网使用
        oks.setComment("我是测试评论文本");

        // 启动分享GUI
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, cn.sharesdk.framework.Platform.ShareParams paramsToShare) {
                if (Wechat.NAME.equals(platform.getName()) ||
                        WechatMoments.NAME.equals(platform.getName())) {
                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                    paramsToShare.setUrl("http://www.xianzhiwang.cn/");
                    paramsToShare.setText("微信测试微信测试");
                    paramsToShare.setImageUrl("http://221.122.68.72:8070/webFile/column/20181008183210912495.jpg");
                    paramsToShare.setTitle("微信测试能不能行啦还");
                }
                if (SinaWeibo.NAME.equals(platform.getName())) {
                    paramsToShare.setText("微博微博微博测试");
                    paramsToShare.setUrl("http://www.xianzhiwang.cn/");
                    paramsToShare.setImageUrl("http://221.122.68.72:8070/webFile/column/20181008183210912495.jpg");
                }
                if (QQ.NAME.equals(platform.getName())) {
                    paramsToShare.setTitle("QQ");
                    paramsToShare.setTitleUrl("http://www.xianzhiwang.cn/");
                    paramsToShare.setText("QQQQQQ测试");
                    paramsToShare.setUrl("http://www.xianzhiwang.cn/");
                    paramsToShare.setImageUrl("http://221.122.68.72:8070/webFile/column/20181008183210912495.jpg");
                }
            }
        });

        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                toastShow("成功");
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                toastShow("失败" + i);
            }

            @Override
            public void onCancel(Platform platform, int i) {
                toastShow("取消" + i);
            }
        });
        oks.show(this);

    }
}
