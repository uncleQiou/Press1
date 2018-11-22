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
import com.tkbs.chem.press.activity.ChartDemoActivity;
import com.tkbs.chem.press.activity.MyApplyActivity;
import com.tkbs.chem.press.activity.MyCustomizedActivity;
import com.tkbs.chem.press.activity.MyOpinionActivity;
import com.tkbs.chem.press.activity.SettingActivity;
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
    private LinearLayout ll_my_bookshelf;
    private TextView tv_my_name;
    private TextView tv_phone;
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
        img_set = (ImageView) findViewById(R.id.img_set);
        img_set.setOnClickListener(this);
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
        String name = preference.getString(Config.NICK_NAME, "");
        if (name.length() == 0) {
            name = preference.getString(Config.REAL_NAME, "");
        }
        tv_my_name.setText(name);
        tv_phone.setText(preference.getString(Config.PHONE, ""));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_personal_information:
                toastShow(R.string.personal_information);
                getActivity().startActivity(new Intent(getActivity(), ChartDemoActivity.class));
                break;
            case R.id.ll_my_apply:
                toastShow(R.string.my_apply);
                getActivity().startActivity(new Intent(getActivity(), MyApplyActivity.class));
                break;
            case R.id.ll_my_opinion:
                toastShow(R.string.my_opinion);
                getActivity().startActivity(new Intent(getActivity(), MyOpinionActivity.class));
                break;
            case R.id.ll_personal_customization:
                toastShow(R.string.personal_custom);
                getActivity().startActivity(new Intent(getActivity(), MyCustomizedActivity.class));
                break;
            case R.id.ll_my_bookshelf:
                toastShow(R.string.my_bookshelf);
                homeInterface.GoTOBookShelf();

                break;
            case R.id.img_set:
                getActivity().startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            default:
                break;
        }

    }

    public void setHomeInterface(HomeInterface homeInterface) {
        this.homeInterface = homeInterface;
    }
}
