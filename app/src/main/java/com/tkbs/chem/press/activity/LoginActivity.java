package com.tkbs.chem.press.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.tkbs.chem.press.R;
import com.tkbs.chem.press.base.BaseActivity;
import com.tkbs.chem.press.base.BaseApplication;
import com.tkbs.chem.press.bean.HttpResponse;
import com.tkbs.chem.press.bean.LoginByPhoneRequestBen;
import com.tkbs.chem.press.bean.LoginRequestBen;
import com.tkbs.chem.press.bean.PhoneCodeBean;
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
    Button tvGetCode;
    @BindView(R.id.btn_login)
    TextView btnLogin;
    @BindView(R.id.btn_register)
    TextView btnRegister;
    @BindView(R.id.ll_qq_login)
    LinearLayout llQqLogin;
    @BindView(R.id.ll_wechat_login)
    LinearLayout llWechatLogin;
    int loginState = 1;
    private PhoneCodeBean phoneCodeData;
    //监听前的文本
    private CharSequence temp;
    //光标开始位置
    private int editStart;
    //光标结束位置
    private int editEnd;
    private int timeCutDown = 60;
    private final int charMaxNum = 11;
    private TextWatcher myTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            temp = charSequence;
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            /** 得到光标开始和结束位置 ,超过最大数后记录刚超出的数字索引进行控制 */
            if (1 == loginState){return;}
            editStart = edUsername.getSelectionStart();
            editEnd = edUsername.getSelectionEnd();
            if (temp.length() >= charMaxNum) {
                if (temp.length() > charMaxNum) {
                    editable.delete(editStart - 1, editEnd);
                    int tempSelection = editStart;
                    edUsername.setText(editable);
                    edUsername.setSelection(tempSelection);
                }

                tvGetCode.setEnabled(true);
            } else {
                tvGetCode.setEnabled(false);
            }
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    String uId = (String) msg.obj;
                    Logger.e("UID:" + uId);
                    loginWeChat(uId);
                    break;
                }
                case 2:
                    if (timeCutDown == 0) {
                        if (null != tvGetCode) {
                            tvGetCode.setEnabled(true);
                            tvGetCode.setText("获取验证码");
                        }
                        if (null != edUsername){
                            edUsername.setEnabled(true);}
                    } else {
                        if (null != tvGetCode){
                            tvGetCode.setEnabled(false);
                            tvGetCode.setText("已发送(" + timeCutDown + ")");}
                        timeCutDown--;
                        mHandler.sendEmptyMessageDelayed(2, 1000);
                    }
                    break;

                default:
                    break;
            }
        }
    };

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
        // 146 ywy
        //edUsername.setText("xx000001");
        //edPassword.setText("1");
        // 105 ywy
//        edUsername.setText("zhangh");
//        edPassword.setText("111111");
        // 103 js
        edUsername.setText("xx000001");
        edPassword.setText("1");
        UiUtils.ClearSp(getApplicationContext());
        settingView();
    }

    private void settingView() {
        if (1 == loginState) {
            // 用户名 密码登陆
            tvChangeTip.setText(R.string.change_phone_login);
            edUsername.setHint(R.string.please_input_username);
            edUsername.addTextChangedListener(myTextWatcher);
            edUsername.setText("");
            edPassword.setText("");
            edPassword.setHint(R.string.please_input_password);
            edPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            tvGetCode.setVisibility(View.GONE);
        } else if (2 == loginState) {
            // 手机验证码登陆
            tvChangeTip.setText(R.string.change_password_login);
            edUsername.setHint(R.string.input_phone_number);
            edUsername.setText("");
            edPassword.setText("");
            edUsername.addTextChangedListener(myTextWatcher);
            edPassword.setHint(R.string.please_input_checkcode);
            edPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
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
                if (edUsername.getText().toString().trim().length()>0 && checkPhoneNumber()){
                    obtainPhoneCode();
                }else {
                    toastShow(R.string.input_login_msg);
                }

                break;
            case R.id.btn_login:
                if (edUsername.getText().toString().trim().length()>0 &&
                        edPassword.getText().toString().trim().length()>0){
                    if (1 == loginState){
                        login();
                    }else {
                        loginByPhone();
                    }
                }else {
                    toastShow(R.string.input_login_msg);
                }

                break;
            case R.id.btn_register:
                toastShow(R.string.register_now);
//                startActivity(new Intent(LoginActivity.this, ChangePasswordActivity.class));
                startActivity(new Intent(LoginActivity.this, RegisterAvtivity.class));
                break;
            case R.id.ll_qq_login:
                toastShow(R.string.str_qq);
                Platform plat = ShareSDK.getPlatform(QQ.NAME);
                plat.showUser(null);
                bingType = "qq";
                break;
            case R.id.ll_wechat_login:
                toastShow(R.string.str_wechat);
//                showShare();
                bingType = "wechat";
                weChatLogin();
//                loginWeChat("oClK75sXKa5gGQtwc0w9p9AWNR_E");
                break;
            default:
                break;
        }

    }

    private String phoneNumber;
    /**
     * 检验电话号码是否符合规范
     *
     * @return
     */
    private boolean checkPhoneNumber() {
        phoneNumber = edUsername.getText().toString().trim();
        if (!UiUtils.isNullorEmpty(phoneNumber) && phoneNumber.matches("1\\d{10}")) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 获取手机验证码
     */
    private void obtainPhoneCode() {
        showProgressDialog();
        addSubscription(apiStores.obtainPhoneCode(edUsername.getText().toString().trim()), new ApiCallback<HttpResponse<PhoneCodeBean>>() {
            @Override
            public void onSuccess(HttpResponse<PhoneCodeBean> model) {
                if (model.isStatus()) {
                    phoneCodeData = model.getData();
                    toastShow(R.string.send_success);
                    mHandler.sendEmptyMessage(2);
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

    /**
     * 微信三方登陆
     */
    private void weChatLogin() {
        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        wechat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

                // 用户 Userid 请求服务器 成功 直接登陆 不成功 注册
//                loginWeChat(platform.getDb().getUserId());
                Message msg = new Message();
                msg.what = 1;
                msg.obj = platform.getDb().getUserId();
                mHandler.sendMessage(msg);

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Logger.e("失败");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Logger.e("用户取消");
            }
        });
        wechat.authorize();
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

    //重写onkeydown方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //点击的为返回键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();// 退出方法
        }
        return true;
    }

    private long time = 0;

    //退出方法
    private void exit() {
        if (System.currentTimeMillis() - time > 2000) {
            time = System.currentTimeMillis();
            toastShow("再点击一次退出应用程序");
        } else {
            Intent intent = new Intent("com.tkbs.chem.press.base.BaseActivity");
            intent.putExtra("closeAll", 1);
            //发送广播
            sendBroadcast(intent);
        }
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
                    edit.putInt(Config.MEMBER_TYPE, user.getMember_type());
                    edit.putInt(Config.MEMBER_STATE, user.getState());
                    edit.commit();
                    //  refresh MainActivity
                    EventBus.getDefault().post(new MessageEvent("Refresh"));
//                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    setResult(RESULT_OK);
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
    /**
     * 手机登陆
     */
    private void loginByPhone() {
        //hash
        //tamp
         //msgNum//验证码
        //phone
        //{"loginName":"xx000001","password":"1"}
        LoginByPhoneRequestBen loginByPhoneRequestBen = new LoginByPhoneRequestBen();
        loginByPhoneRequestBen.setPhone(edUsername.getText().toString().trim());
        loginByPhoneRequestBen.setMsgNum(edPassword.getText().toString().trim());
        loginByPhoneRequestBen.setHash(phoneCodeData.getHash());
        loginByPhoneRequestBen.setTamp(phoneCodeData.getTamp());
        final Gson gson = new Gson();
        String route = gson.toJson(loginByPhoneRequestBen);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), route);
        showProgressDialog();
        addSubscription(apiStores.PressLoginByPhone(body), new ApiCallback<HttpResponse<UserBean>>() {
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
                    edit.putInt(Config.MEMBER_TYPE, user.getMember_type());
                    edit.putInt(Config.MEMBER_STATE, user.getState());
                    edit.commit();
                    //  refresh MainActivity
                    EventBus.getDefault().post(new MessageEvent("Refresh"));
//                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    setResult(RESULT_OK);
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

    /**
     * 微信三方登陆
     */
    private String otherUserId;
    private String bingType;

    private void loginWeChat(final String wechatUId) {
        otherUserId = wechatUId;
        Logger.e("userID:" + otherUserId);
        showProgressDialog();
        addSubscription(apiStores.loginByWechat(wechatUId), new ApiCallback<HttpResponse<UserBean>>() {
            @Override
            public void onSuccess(HttpResponse<UserBean> model) {
                //用户 Userid 请求服务器 成功 直接登陆 不成功 注册
                if (model.isStatus()) {
                    Logger.e("已有账号");
                    UserBean user = model.getData();
                    SharedPreferences.Editor edit = BaseApplication.preferences.edit();
                    edit.putString(Config.LOGIN_NAME, user.getLogin_name());
                    edit.putString(Config.PASSWORD, user.getPASSWORD());
                    edit.putString(Config.NICK_NAME, user.getNick_name());
                    edit.putString(Config.REAL_NAME, user.getReal_name());
                    edit.putString(Config.WORKPHONE, user.getWorkphone());
                    edit.putString(Config.PHONE, user.getPhone());
                    edit.putInt(Config.MEMBER_TYPE, user.getMember_type());
                    edit.putInt(Config.MEMBER_STATE, user.getState());
                    edit.commit();
                    //  refresh MainActivity
                    EventBus.getDefault().post(new MessageEvent("Refresh"));
//                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    setResult(RESULT_OK);
                    finish();
                } else {
                    // 调用注册页面 刷新登陆 6005 黑名单
                    if (model.getErrorCode() == 6011){
                        Logger.e("需要注册");
                        Intent intent = new Intent(LoginActivity.this, ThreePartBindingActivity.class);
                        intent.putExtra("USERID", otherUserId);
                        intent.putExtra("BINGTYPE", bingType);
                        startActivity(intent);
//                    finish();
                        toastShow(model.getErrorDescription());
                    }else {
                        toastShow(model.getErrorDescription());

                    }

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
