package com.tkbs.chem.press.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkbs.chem.press.R;
import com.tkbs.chem.press.activity.LoginActivity;
import com.tkbs.chem.press.activity.MyAccountActivity;
import com.tkbs.chem.press.activity.MyApplyActivity;
import com.tkbs.chem.press.activity.MyOpinionActivity;
import com.tkbs.chem.press.activity.PersonalTailorActivity;
import com.tkbs.chem.press.activity.SettingActivity;
import com.tkbs.chem.press.activity.TeaPersonalCenterActivity;
import com.tkbs.chem.press.base.BaseFragment;
import com.tkbs.chem.press.myinterface.HomeInterface;
import com.tkbs.chem.press.util.Config;
import com.tkbs.chem.press.util.MessageEvent;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by Administrator on 2018/10/13.
 */
public class MineTeacherFragment extends BaseFragment implements View.OnClickListener {
    private LinearLayout ll_personal_information;
    private LinearLayout ll_my_apply;
    private LinearLayout ll_my_opinion;
    private LinearLayout ll_personal_customization;
    private LinearLayout ll_my_account;
    private LinearLayout ll_my_bookshelf;
    private TextView tv_my_name;
    private TextView tv_phone;
    private TextView tv_go_login;
    private ImageView img_set;
    private HomeInterface homeInterface;

    @Override
    protected View getPreviewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.layout_preview, container, false);
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_mine_teacher);
        EventBus.getDefault().register(this);
        ll_personal_information = (LinearLayout) findViewById(R.id.ll_personal_information);
        ll_personal_information.setOnClickListener(this);
        ll_my_apply = (LinearLayout) findViewById(R.id.ll_my_apply);
        ll_my_apply.setOnClickListener(this);
        ll_my_opinion = (LinearLayout) findViewById(R.id.ll_my_opinion);
        ll_my_opinion.setOnClickListener(this);
        ll_personal_customization = (LinearLayout) findViewById(R.id.ll_personal_customization);
        ll_personal_customization.setOnClickListener(this);
        ll_my_bookshelf = (LinearLayout) findViewById(R.id.ll_my_bookshelf);
        ll_my_bookshelf.setOnClickListener(this);
        tv_my_name = (TextView) findViewById(R.id.tv_my_name);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_go_login = (TextView) findViewById(R.id.tv_go_login);
        tv_go_login.setOnClickListener(this);
        img_set = (ImageView) findViewById(R.id.img_set);
        img_set.setOnClickListener(this);
        ll_my_account = (LinearLayout) findViewById(R.id.ll_my_account);
        ll_my_account.setOnClickListener(this);
        setUserData();
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void RefreshUi(MessageEvent messageEvent) {
        if ("Refresh".endsWith(messageEvent.getMessage())) {
            setUserData();
        }
    }

    /**
     * 设置用户数据
     */
    private void setUserData() {
//        String name = preference.getString(Config.NICK_NAME, "");
//        if (name.length() == 0) {
//        }
        String name = preference.getString(Config.REAL_NAME, "");
        tv_my_name.setText(name);
        tv_phone.setText(preference.getString(Config.PHONE, ""));
        int user_type = preference.getInt(Config.MEMBER_TYPE, 3);
        if (user_type == 5) {
            tv_go_login.setVisibility(View.VISIBLE);
        } else {
            tv_go_login.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_personal_information:
//                toastShow(R.string.personal_information);
//                getActivity().startActivity(new Intent(getActivity(), ChartDemoActivity.class));
                getActivity().startActivity(new Intent(getActivity(), TeaPersonalCenterActivity.class));
                break;
            case R.id.ll_my_apply:
//                toastShow(R.string.my_apply);
                getActivity().startActivity(new Intent(getActivity(), MyApplyActivity.class));
                break;
            case R.id.ll_my_account:
                getActivity().startActivityForResult(new Intent(getActivity(), MyAccountActivity.class), Config.ACCOUNT_SWITCHING);
                break;
            case R.id.ll_my_opinion:
                getActivity().startActivity(new Intent(getActivity(), MyOpinionActivity.class));
                break;
            case R.id.ll_personal_customization:
//                toastShow(R.string.personal_custom);
                getActivity().startActivityForResult(new Intent(getActivity(), PersonalTailorActivity.class), Config.ACCOUNT_SWITCHING);
                break;
            case R.id.ll_my_bookshelf:
//                toastShow(R.string.my_bookshelf);
                homeInterface.GoTOBookShelf();

                break;
            case R.id.img_set:
                getActivity().startActivityForResult(new Intent(getActivity(), SettingActivity.class), Config.ACCOUNT_SWITCHING);
                break;
            case R.id.tv_go_login:
                getActivity().startActivityForResult(new Intent(getActivity(), LoginActivity.class), Config.ACCOUNT_SWITCHING);
                break;
            default:
                break;
        }

    }

    public void setHomeInterface(HomeInterface homeInterface) {
        this.homeInterface = homeInterface;
    }
}
