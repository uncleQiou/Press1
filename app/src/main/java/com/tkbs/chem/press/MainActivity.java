package com.tkbs.chem.press;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mob.pushsdk.MobPush;
import com.mob.pushsdk.MobPushCustomMessage;
import com.mob.pushsdk.MobPushNotifyMessage;
import com.mob.pushsdk.MobPushReceiver;
import com.orhanobut.logger.Logger;
import com.tkbs.chem.press.activity.LoginActivity;
import com.tkbs.chem.press.activity.SearchActivity;
import com.tkbs.chem.press.activity.SearchClassifyActivity;
import com.tkbs.chem.press.base.BaseActivity;
import com.tkbs.chem.press.base.BaseApplication;
import com.tkbs.chem.press.bean.HttpResponse;
import com.tkbs.chem.press.bean.UserBean;
import com.tkbs.chem.press.fragment.BookCityFragment;
import com.tkbs.chem.press.fragment.BookShelfFragment;
import com.tkbs.chem.press.fragment.DiscoverFragment;
import com.tkbs.chem.press.fragment.MineTeacherFragment;
import com.tkbs.chem.press.fragment.MinfSaleManFragment;
import com.tkbs.chem.press.fragment.SalesmanManageFragment;
import com.tkbs.chem.press.myinterface.HomeInterface;
import com.tkbs.chem.press.net.ApiCallback;
import com.tkbs.chem.press.util.Config;
import com.tkbs.chem.press.util.MessageEvent;
import com.tkbs.chem.press.view.BadgeView;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.fl_main_content)
    FrameLayout flMainContent;
    @BindView(R.id.rbtn_tab_bookshelf)
    RadioButton rbtnTabBookshelf;
    @BindView(R.id.rbtn_tab_bookcity)
    RadioButton rbtnTabBookcity;
    @BindView(R.id.rbtn_tab_manage)
    RadioButton rbtnTabManage;
    @BindView(R.id.rbtn_tab_mine)
    RadioButton rbtnTabMine;
    @BindView(R.id.group_tab)
    RadioGroup groupTab;
    @BindView(R.id.title_home)
    TextView titleHome;
    @BindView(R.id.title_home_right)
    TextView titleHomeRight;
    @BindView(R.id.rl_titlebar_nomal)
    RelativeLayout rlTitlebarNomal;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.tv_classfy)
    TextView tvClassfy;
    @BindView(R.id.ll_title_serach)
    LinearLayout llTitleSerach;
    @BindView(R.id.rbtn_tab_discover)
    RadioButton rbtnTabDiscover;

    protected Fragment[] mFragments;
    protected int mIndex = 1;
    @BindView(R.id.btn_1)
    Button btn1;
    @BindView(R.id.btn_2)
    Button btn2;
    @BindView(R.id.btn_3)
    Button btn3;
    @BindView(R.id.btn_4)
    Button btn4;
    // 书城
    private BookCityFragment bookCityFragment;
    // 书架
    private BookShelfFragment bookShelfFragment;
    // 发现
    private DiscoverFragment discoverFragment;
    // 我的(教师)
    private MineTeacherFragment mineTeacherFragment;
    // 我的(业务员)
    private MinfSaleManFragment minfSaleManFragment;
    // 管理
    private SalesmanManageFragment salesmanManageFragment;
    // 用户身份
    private int user_type = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initdata() {
        user_type = preference.getInt(Config.MEMBER_TYPE, 3);

        /**
         * 根据用户信息显示底部导航
         */
        if (user_type == 2) {
            rbtnTabManage.setVisibility(View.VISIBLE);
            rbtnTabDiscover.setVisibility(View.GONE);
        } else {
            rbtnTabDiscover.setVisibility(View.VISIBLE);
            rbtnTabManage.setVisibility(View.GONE);
        }
        getWebPath();
        initTabs();
        if (null == badge1){
            badge1 = new BadgeView(this, btn3);
        }
        MobPush.addPushReceiver(new MobPushReceiver() {
            @Override
            public void onCustomMessageReceive(Context context, MobPushCustomMessage message) {
                //接收自定义消息
                //显示红点 透传消息
                if (user_type != 2) {
                    String userGuid = preference.getString(Config.GUID,"");
                    if (message.getContent().equals(userGuid)){
                        remind(1);
                    }
                }
            }

            @Override
            public void onNotifyMessageReceive(Context context, MobPushNotifyMessage message) {
                //接收通知消息
            }

            @Override
            public void onNotifyMessageOpenedReceive(Context context, MobPushNotifyMessage message) {
                //接收通知消息被点击事件
            }

            @Override
            public void onTagsCallback(Context context, String[] tags, int operation, int errorCode) {
                //接收tags的增改删查操作
            }

            @Override
            public void onAliasCallback(Context context, String alias, int operation, int errorCode) {
                //接收alias的增改删查操作
            }
        });
        unReadMessageNum();

    }

    @Override
    protected void initTitle() {

    }

    /**
     * 查询未度消息
     * @param
     */
    private void unReadMessageNum(){
        showProgressDialog();
        addSubscription(apiStores.checkUnReadCount(), new ApiCallback<HttpResponse<Integer>>() {
            @Override
            public void onSuccess(HttpResponse<Integer> model) {
                if (model.isStatus()) {
                    if (user_type != 2){
                        remind(model.getData());
                    }else {
                        remind(0);
                    }

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


    @Subscribe(threadMode = ThreadMode.MainThread)
    public void RefreshUi(MessageEvent messageEvent) {
        if ("Refresh".endsWith(messageEvent.getMessage())) {
            badge1.hide();
            initdata();
        }
    }

    /**
     * 回到书城首页
     */
    public void goBookCity() {
        rbtnTabBookcity.setChecked(true);
        setIndexSelected(1);
        rlTitlebarNomal.setVisibility(View.GONE);
        llTitleSerach.setVisibility(View.VISIBLE);
    }

    private void initTabs() {
        bookCityFragment = new BookCityFragment();
        bookShelfFragment = new BookShelfFragment();
        discoverFragment = new DiscoverFragment();
        mineTeacherFragment = new MineTeacherFragment();
        mineTeacherFragment.setHomeInterface(new HomeInterface() {
            @Override
            public void GoTOBookShelf() {
                rbtnTabBookshelf.setChecked(true);
                setIndexSelected(0);
                rlTitlebarNomal.setVisibility(View.VISIBLE);
                llTitleSerach.setVisibility(View.GONE);
                titleHomeRight.setVisibility(View.GONE);
                titleHome.setText(R.string.my_bookshelf);
            }
        });
        minfSaleManFragment = new MinfSaleManFragment();
        salesmanManageFragment = new SalesmanManageFragment();
//        myStudyFragment = new MyStudyFragment();
        // 根据 用户信息 添加到数组
        /**
         * 1、超级管理员 2、业务员 3、教师 4、游客
         */

        if (2 == user_type) {
            // 业务员
            mFragments = new Fragment[]{bookShelfFragment, bookCityFragment, salesmanManageFragment, minfSaleManFragment};
        } else {
            // 教师
            mFragments = new Fragment[]{bookShelfFragment, bookCityFragment, discoverFragment, mineTeacherFragment};

        }


        //开启事务
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        //添加首页
        ft.add(R.id.fl_main_content, bookCityFragment).commitAllowingStateLoss();
        //默认设置为第0个
        setIndexSelected(1);

    }

    public void setIndexSelected(int index) {
        if (user_type != 2){
            if (index == 2){
                remind(0);
            }else {
                unReadMessageNum();
            }
        }
        if (mIndex == index) {
            return;
        }
        checkBlackUser();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        //隐藏
        ft.hide(mFragments[mIndex]);

        //判断是否添加
        if (!mFragments[index].isAdded()) {
            if (isStateEnable()) {
                ft.add(R.id.fl_main_content, mFragments[index]).show(mFragments[index]);
            }
        } else {
            if (isStateEnable()) {
                ft.show(mFragments[index]);
            }
        }

        ft.commitAllowingStateLoss();

        //再次赋值
        mIndex = index;
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

    @OnClick({R.id.rbtn_tab_bookshelf, R.id.rbtn_tab_bookcity,
            R.id.rbtn_tab_manage, R.id.rbtn_tab_mine, R.id.ll_search,
            R.id.rbtn_tab_discover, R.id.tv_classfy})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rbtn_tab_bookshelf:
                setIndexSelected(0);
                rlTitlebarNomal.setVisibility(View.VISIBLE);
                llTitleSerach.setVisibility(View.GONE);
                titleHomeRight.setVisibility(View.GONE);
                titleHome.setText(R.string.my_bookshelf);
                break;
            case R.id.rbtn_tab_bookcity:
                setIndexSelected(1);
//                titleHomeRight.setVisibility(View.VISIBLE);
//                titleHome.setText(R.string.my_bookshelf);
                rlTitlebarNomal.setVisibility(View.GONE);
                llTitleSerach.setVisibility(View.VISIBLE);
                break;
            case R.id.rbtn_tab_manage:
                rlTitlebarNomal.setVisibility(View.GONE);
                llTitleSerach.setVisibility(View.GONE);
                setIndexSelected(2);
                titleHomeRight.setVisibility(View.GONE);
                titleHome.setText(R.string.home_table3);
                break;
            case R.id.rbtn_tab_discover:
                rlTitlebarNomal.setVisibility(View.VISIBLE);
                llTitleSerach.setVisibility(View.GONE);
                setIndexSelected(2);
                titleHomeRight.setVisibility(View.GONE);
                titleHome.setText(R.string.discover);
                break;
            case R.id.rbtn_tab_mine:
                setIndexSelected(3);
                titleHomeRight.setVisibility(View.GONE);
                rlTitlebarNomal.setVisibility(View.GONE);
                titleHome.setText(R.string.home_table4);
                llTitleSerach.setVisibility(View.GONE);
                if (2 == user_type) {
                    EventBus.getDefault().post(new MessageEvent("RefreshTJ"));
                }
                break;
            case R.id.ll_search:
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                break;
            case R.id.tv_classfy:
                startActivityForResult(new Intent(MainActivity.this, SearchClassifyActivity.class), 0);
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
                case 0:
                    //  将获取的结果 发送给 搜索接口
                    String result = data.getStringExtra("result");
                    Logger.e(result);
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    intent.putExtra("Classy", result);
                    startActivity(intent);
                    break;
                case Config.ACCOUNT_SWITCHING:
                    goBookCity();
                    break;
                default:
                    break;
            }
        }
    }

    private int user_sate;

    /**
     * 检查用户是否是黑名单用户
     */
    private void checkBlackUser() {
        showProgressDialog();
        addSubscription(apiStores.updateUserInfo(), new ApiCallback<HttpResponse<UserBean>>() {
            @Override
            public void onSuccess(HttpResponse<UserBean> model) {
                if (model.isStatus()) {
                    UserBean user = model.getData();
                    SharedPreferences.Editor edit = BaseApplication.preferences.edit();
                    edit.putString(Config.LOGIN_NAME, user.getLogin_name());
                    edit.putString(Config.GUID, user.getGuid());
                    edit.putString(Config.PASSWORD, user.getPASSWORD());
                    edit.putString(Config.NICK_NAME, user.getNick_name());
                    edit.putString(Config.REAL_NAME, user.getReal_name());
                    edit.putString(Config.WORKPHONE, user.getWorkphone());
                    edit.putString(Config.PHONE, user.getPhone());
                    edit.putInt(Config.MEMBER_TYPE, user.getMember_type());
                    edit.putInt(Config.MEMBER_STATE, user.getState());
                    edit.commit();
                    user_sate = user.getState();
                    if (user_sate == 1) {
                        // 黑名单
                        toastShow("用户已被禁用，请联系管理员");
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivityForResult(intent, Config.ACCOUNT_SWITCHING);
                    }
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

    private void getWebPath() {
        showProgressDialog();
        addSubscription(apiStores.GetWebPath(), new ApiCallback<HttpResponse<String>>() {
            @Override
            public void onSuccess(HttpResponse<String> model) {
                if (model.isStatus()) {
                    BaseApplication.imgBasePath = model.data;
                    Logger.e("imagePath == " + BaseApplication.imgBasePath);
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

    private BadgeView badge1;
    private void remind(int flg) {
        //BadgeView的具体使用
//        BadgeView badge1 = new BadgeView(this, view);
        // 创建一个BadgeView对象，view为你需要显示提醒的控件
        badge1.setText(flg+"");
        // 需要显示的提醒类容
        badge1.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        // 显示的位置.右上角,BadgeView.POSITION_BOTTOM_LEFT,下左，还有其他几个属性
        badge1.setTextColor(Color.WHITE);
        // 文本颜色
//        badge1.setBadgeBackgroundColor(Color.RED);
        // 提醒信息的背景颜色，自己设置
        badge1.setBackgroundResource(R.mipmap.red_point);
        //设置背景图片
        badge1.setTextSize(12);
        // 文本大小
        badge1.setBadgeMargin(40, 15);
        // 水平和竖直方向的间距
//        badge1.setBadgeMargin(5);
        //各边间隔
//         badge1.toggle(); //显示效果，如果已经显示，则影藏，如果影藏，则显示
        //badge1.show();
        // 只有显示
        // badge1.hide();//影藏显示
        if (flg > 0){
            badge1.show();
        }else {
            badge1.hide();
        }
    }


}
