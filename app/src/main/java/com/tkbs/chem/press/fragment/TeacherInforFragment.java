package com.tkbs.chem.press.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tkbs.chem.press.R;
import com.tkbs.chem.press.base.BaseFragment;
import com.tkbs.chem.press.bean.HttpResponse;
import com.tkbs.chem.press.bean.UserInfoManageDataBean;
import com.tkbs.chem.press.bean.UserManageDataBean;
import com.tkbs.chem.press.net.ApiCallback;
import com.tkbs.chem.press.util.TimeUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/10/19.
 */
public class TeacherInforFragment extends BaseFragment {

    private TextView tvRealName;
    private TextView tvSex;
    private TextView tvBirthDate;
    private TextView tvContactWay;
    private TextView tvMailbox;
    private TextView tvLocation;
    private TextView tvSchool;
    private TextView tvFaculty;
    private TextView tvTeacherJob;
    private TextView tvOfficePhone;
    private TextView tvTeachingCourse;
    private String guid;

    @Override
    protected View getPreviewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.layout_preview, container, false);
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_teacher_infor);
        guid = getArguments().getString("guid");
        tvRealName = (TextView) findViewById(R.id.tv_real_name);
        tvSex = (TextView) findViewById(R.id.tv_sex);
        tvBirthDate = (TextView) findViewById(R.id.tv_birth_date);
        tvContactWay = (TextView) findViewById(R.id.tv_contact_way);
        tvMailbox = (TextView) findViewById(R.id.tv_mailbox);
        tvLocation = (TextView) findViewById(R.id.tv_location);
        tvSchool = (TextView) findViewById(R.id.tv_school);
        tvFaculty = (TextView) findViewById(R.id.tv_faculty);
        tvTeacherJob = (TextView) findViewById(R.id.tv_teacher_job);
        tvOfficePhone = (TextView) findViewById(R.id.tv_office_phone);
        tvTeachingCourse = (TextView) findViewById(R.id.tv_teaching_course);
        getUserInfo(guid);

//        tvRealName.setText("张志");
//        tvSex.setText("男");
//        tvBirthDate.setText("2018年10月19日10:56:21");
//        tvContactWay.setText("18804952321");
//        tvMailbox.setText("88026667@qq.com");
//        tvLocation.setText("北京市海淀区");
//        tvSchool.setText("清华大学");
//        tvFaculty.setText("土木工程系");
//        tvTeacherJob.setText("主任");
//        tvOfficePhone.setText("010-110110");
//        tvTeachingCourse.setText("工程造价");

    }

    private void getUserInfo(String guid) {
        showProgressDialog();
        addSubscription(apiStores.UserDetail(guid), new ApiCallback<HttpResponse<UserInfoManageDataBean>>() {
            @Override
            public void onSuccess(HttpResponse<UserInfoManageDataBean> model) {
                if (model.isStatus()) {
                    UserInfoManageDataBean dataBean = model.getData();
                    tvRealName.setText(dataBean.getRealName());
                    tvSex.setText(dataBean.getSex());
                    tvBirthDate.setText(dataBean.getBirthday());
                    tvContactWay.setText(dataBean.getPhone());
                    tvMailbox.setText(dataBean.getMail());
                    tvLocation.setText(dataBean.getAreaName());
                    tvSchool.setText(dataBean.getOrganization());
                    tvFaculty.setText(dataBean.getDepartment());
                    tvTeacherJob.setText(dataBean.getJob());
                    tvOfficePhone.setText(dataBean.getWorkphone());
                    tvTeachingCourse.setText(dataBean.getCourse());
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
