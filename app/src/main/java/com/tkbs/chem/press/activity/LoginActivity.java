package com.tkbs.chem.press.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tkbs.chem.press.R;
import com.tkbs.chem.press.base.BaseActivity;
import com.tkbs.chem.press.base.BaseApplication;
import com.tkbs.chem.press.bean.HttpResponse;
import com.tkbs.chem.press.bean.LoginRequestBen;
import com.tkbs.chem.press.bean.UserBean;
import com.tkbs.chem.press.net.ApiCallback;
import com.tkbs.chem.press.util.Config;
import com.tkbs.chem.press.util.MessageEvent;
import com.tkbs.chem.press.util.UiUtils;


import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import de.greenrobot.event.EventBus;
import okhttp3.RequestBody;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.img_logo)
    ImageView imgLogo;
    @BindView(R.id.tv_change_tip)
    TextView tvChangeTip;
    @BindView(R.id.img_change)
    ImageView imgChange;
    @BindView(R.id.ed_username)
    EditText edUsername;
    @BindView(R.id.ed_password)
    EditText edPassword;
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;
    @BindView(R.id.btn_login)
    TextView btnLogin;
    @BindView(R.id.btn_register)
    TextView btnRegister;
    @BindView(R.id.ll_qq_login)
    LinearLayout llQqLogin;
    @BindView(R.id.ll_wechat_login)
    LinearLayout llWechatLogin;
    int loginState = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initdata() {
        edUsername.setText("xx000001");
        edPassword.setText("1");
        settingView();
    }

    private void settingView() {
        if (1 == loginState) {
            // 用户名 密码登陆
            tvChangeTip.setText(R.string.change_phone_login);
            edUsername.setHint(R.string.please_input_username);
            edPassword.setHint(R.string.please_input_password);
            tvGetCode.setVisibility(View.GONE);
        } else if (2 == loginState) {
            // 手机验证码登陆
            tvChangeTip.setText(R.string.change_password_login);
            edUsername.setHint(R.string.input_phone_number);
            edPassword.setHint(R.string.please_input_checkcode);
            tvGetCode.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initTitle() {

    }

    @OnClick({R.id.img_change, R.id.tv_get_code, R.id.btn_login, R.id.btn_register,
            R.id.ll_qq_login, R.id.ll_wechat_login})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_change:
                if (1 == loginState) {
                    loginState = 2;
                } else if (2 == loginState) {
                    loginState = 1;
                }
                settingView();
                break;
            case R.id.tv_get_code:
                toastShow(R.string.click_get);
                break;
            case R.id.btn_login:
                toastShow(R.string.login);
                //IMEI
                toastShow("唯一标识：" + UiUtils.getid(LoginActivity.this));
//                startActivity(new Intent(LoginActivity.this, NewsActivity.class));
                login();
                break;
            case R.id.btn_register:
                toastShow(R.string.register_now);
                startActivity(new Intent(LoginActivity.this, ChangePasswordActivity.class));
                break;
            case R.id.ll_qq_login:
                toastShow(R.string.str_qq);
                Platform plat = ShareSDK.getPlatform(QQ.NAME);
                plat.showUser(null);
                break;
            case R.id.ll_wechat_login:
                toastShow(R.string.str_wechat);
                showShare();
                break;
            default:
                break;
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

    /**
     * 登陆
     */
    private void login() {

        //{"loginName":"xx000001","password":"1"}
        LoginRequestBen loginRequestBen = new LoginRequestBen();
        loginRequestBen.setLoginName(edUsername.getText().toString().trim());
        loginRequestBen.setPassword(edPassword.getText().toString().trim());
        final Gson gson = new Gson();
        String route = gson.toJson(loginRequestBen);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), route);
        showProgressDialog();
        addSubscription(apiStores.PressLogin(body), new ApiCallback<HttpResponse<UserBean>>() {
            @Override
            public void onSuccess(HttpResponse<UserBean> model) {
                if (model.isStatus()) {
                    UserBean user = model.getData();
                    SharedPreferences.Editor edit = BaseApplication.preferences.edit();
                    edit.putString(Config.LOGIN_NAME, user.getLogin_name());
                    edit.putString(Config.PASSWORD, user.getPASSWORD());
                    edit.putString(Config.NICK_NAME, user.getNick_name());
                    edit.putString(Config.REAL_NAME, user.getReal_name());
                    edit.putString(Config.WORKPHONE, user.getWorkphone());
                    edit.putString(Config.PHONE, user.getPhone());
                    edit.commit();
                    // TODO refresh MainActivity
                    EventBus.getDefault().post(new MessageEvent("Refresh"));
                    finish();
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


}
