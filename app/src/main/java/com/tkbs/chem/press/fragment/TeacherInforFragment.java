package com.tkbs.chem.press.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tkbs.chem.press.R;
import com.tkbs.chem.press.base.BaseFragment;
import com.tkbs.chem.press.bean.HttpResponse;
import com.tkbs.chem.press.bean.LoginRequestBen;
import com.tkbs.chem.press.bean.UpdateTeacherInfoBean;
import com.tkbs.chem.press.bean.UserInfoManageDataBean;
import com.tkbs.chem.press.bean.UserManageDataBean;
import com.tkbs.chem.press.net.ApiCallback;
import com.tkbs.chem.press.util.TimeUtils;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;

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
    private EditText tvSchool;
    private EditText tvFaculty;
    private TextView tvTeacherJob;
    private TextView tvOfficePhone;
    private TextView tvTeachingCourse;
    private TextView tv_save_modify;
    private String guid;
    private boolean isBtnModify = false;
    private Handler handler = new Handler();
    private String beforeText = "";

    private TextWatcher myTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (!editable.toString().equals(beforeText)) {
//            if (isSoftShowing(getActivity())) {
                if (delayRun != null) {
                    //每次editText有变化的时候，则移除上次发出的延迟线程
                    handler.removeCallbacks(delayRun);
                }
                //延迟800ms，如果不再输入字符，则执行该线程的run方法
                handler.postDelayed(delayRun, 800);
            }
//            changeButtonState();
        }
    };

    /**
     * 延迟线程，看是否还有下一个字符输入
     */
    private Runnable delayRun = new Runnable() {

        @Override
        public void run() {
            // 修改按钮颜色 添加点击事件
            if (!isBtnModify) {
                changeButtonState();
            }
        }
    };

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
        tvSchool = (EditText) findViewById(R.id.tv_school);
        tvFaculty = (EditText) findViewById(R.id.tv_faculty);
        tvTeacherJob = (TextView) findViewById(R.id.tv_teacher_job);
        tvOfficePhone = (TextView) findViewById(R.id.tv_office_phone);
        tv_save_modify = (TextView) findViewById(R.id.tv_save_modify);
        tvTeachingCourse = (TextView) findViewById(R.id.tv_teaching_course);
        getUserInfo(guid);
        tvSchool.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                beforeText = tvSchool.getText().toString().trim();
                tvSchool.addTextChangedListener(myTextWatcher);
                if (b){
                    isBtnModify = false;
                }
            }
        });
        tvFaculty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                beforeText = tvSchool.getText().toString().trim();
                tvFaculty.addTextChangedListener(myTextWatcher);
                if (b){
                    isBtnModify = false;
                }
            }
        });

    }

    private void changeButtonState() {
        isBtnModify = true;
        tv_save_modify.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_blue));
        tv_save_modify.setTextColor(getResources().getColor(R.color.hg_app_main_color));
        tv_save_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTeaInfo();

            }
        });
    }

    private void setTeaInfo() {
        showProgressDialog();
        UpdateTeacherInfoBean updateTeacherInfoBean = new UpdateTeacherInfoBean();
        updateTeacherInfoBean.setGuid(guid);
        updateTeacherInfoBean.setOrganization(tvSchool.getText().toString().trim());
        updateTeacherInfoBean.setDepartment(tvFaculty.getText().toString().trim());
        final Gson gson = new Gson();
        String route = gson.toJson(updateTeacherInfoBean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), route);
        addSubscription(apiStores.setTesInfo(body), new ApiCallback<HttpResponse<Object>>() {
            @Override
            public void onSuccess(HttpResponse<Object> model) {
                if (model.isStatus()) {
                    toastShow(R.string.save_modify);
                    getUserInfo(guid);
                    // 按钮重置
                    tv_save_modify.setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_gray));
                    tv_save_modify.setTextColor(getResources().getColor(R.color.text_main_9));
                    tv_save_modify.setOnClickListener(null);
                    if (delayRun != null) {
                        //每次editText有变化的时候，则移除上次发出的延迟线程
                        handler.removeCallbacks(delayRun);
                    }
//                    tvSchool.addTextChangedListener(null);
                    tvSchool.removeTextChangedListener(myTextWatcher);
                    tvFaculty.removeTextChangedListener(myTextWatcher);
//                    tvFaculty.addTextChangedListener(null);
                    tvSchool.clearFocus();
                    tvFaculty.clearFocus();
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

    /**
     * 判断软键盘是否显示方法
     *
     * @param activity
     * @return
     */

    public static boolean isSoftShowing(Activity activity) {
        //获取当屏幕内容的高度
        int screenHeight = activity.getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        //DecorView即为activity的顶级view
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        //考虑到虚拟导航栏的情况（虚拟导航栏情况下：screenHeight = rect.bottom + 虚拟导航栏高度）
        //选取screenHeight*2/3进行判断
        return screenHeight * 2 / 3 > rect.bottom + getSoftButtonsBarHeight(activity);
    }

    /**
     * 底部虚拟按键栏的高度
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static int getSoftButtonsBarHeight(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        //这个方法获取可能不是真实屏幕的高度
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        //获取当前屏幕的真实高度
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }


}
