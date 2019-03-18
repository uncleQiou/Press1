package com.tkbs.chem.press.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.tkbs.chem.press.R;
import com.tkbs.chem.press.base.BaseActivity;
import com.tkbs.chem.press.bean.HttpResponse;
import com.tkbs.chem.press.bean.RechargeRecordDataBean;
import com.tkbs.chem.press.net.ApiCallback;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;
import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;
import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;

public class RechargeRecordActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.recycler)
    RefreshRecyclerView recycler;
    @BindView(R.id.tv_dis_month)
    TextView tvDisMonth;
    @BindView(R.id.tv_pay)
    TextView tvPay;
    @BindView(R.id.img_calendar)
    ImageView imgCalendar;
    @BindView(R.id.img_share)
    ImageView imgShare;
    @BindView(R.id.ll_recharge_date)
    LinearLayout llRechargeDate;
    private Handler mHandler;
    private int page = 1;
    private MyRecordAdapter myAdapter;
    private String searchYYMM;

    private int mYear;
    private int mMonth;
    private int mDay;
    private boolean noData = true;

    private String strDisMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recharge_record;
    }

    @Override
    protected void initdata() {
        mHandler = new Handler();
        // 当前 年月
        Calendar c = Calendar.getInstance();
        // 获取当前年份
        mYear = c.get(Calendar.YEAR);
        // 获取当前月份
        mMonth = c.get(Calendar.MONTH) + 1;
        searchYYMM = mYear + "-" + mMonth;
        strDisMonth = mYear + "年" + mMonth + "月";
        tvDisMonth.setText(strDisMonth);
        String total = getResources().getString(R.string.recharge_total, 0);
        tvPay.setText(total);
        myAdapter = new MyRecordAdapter(this);
        recycler.setSwipeRefreshColors(0xFF437845, 0xFFE44F98, 0xFF2FAC21);
        recycler.setLayoutManager(new GridLayoutManager(this, 1));
        recycler.setAdapter(myAdapter);
        recycler.setRefreshAction(new Action() {
            @Override
            public void onAction() {
                page = 1;
                getData(true);
            }
        });
        recycler.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                page++;
                getData(false);

            }
        });

        recycler.post(new Runnable() {
            @Override
            public void run() {
                searchYYMM = "";
                recycler.showSwipeRefresh();
                getData(true);
            }
        });
        recycler.getNoMoreView().setText("没有更多数据了");
    }

    @Override
    protected void initTitle() {
        title.setText(R.string.recharge_record);
        tvRight.setText(R.string.recharge_str);
    }


    private void getData(final boolean isRefresh) {
        showProgressDialog();
        addSubscription(apiStores.getRechargeRecord(page, searchYYMM, 1), new ApiCallback<HttpResponse<RechargeRecordDataBean>>() {
            @Override
            public void onSuccess(HttpResponse<RechargeRecordDataBean> model) {
                if (model.isStatus()) {
                    // 第一次 没有记录就隐藏
                    if (model.getData().getList().size()==0){
                        if (noData){
                            llRechargeDate.setVisibility(View.GONE);
                        }

                    }
                    noData = false;
                    tvDisMonth.setText(strDisMonth);
                    String total = getResources().getString(R.string.recharge_total, model.getData().getTotalPrice());
                    tvPay.setText(total);
                    if (isRefresh) {
                        page = 1;
                        myAdapter.clear();
                        myAdapter.addAll(model.getData().getList());
                        recycler.dismissSwipeRefresh();
                        recycler.getRecyclerView().scrollToPosition(0);

                    } else {
                        myAdapter.addAll(model.getData().getList());
                    }
                    if (model.getData().getList().size() < 10) {
                        recycler.showNoMore();
                    }
                } else {
                    recycler.dismissSwipeRefresh();
                    toastShow(model.getErrorDescription());
                }

            }

            @Override
            public void onFailure(String msg) {
                toastShow(msg);
            }

            @Override
            public void onFinish() {
                recycler.dismissSwipeRefresh();
                dismissProgressDialog();

            }
        });
    }

    private MyRecordData[] getTestData() {
        return new MyRecordData[]{
                new MyRecordData("￥2"),
                new MyRecordData("￥4"),
                new MyRecordData("￥200"),
                new MyRecordData("￥200"),
                new MyRecordData("￥800"),
                new MyRecordData("￥2000"),
                new MyRecordData("￥2"),

        };
    }

    @OnClick({R.id.back, R.id.img_calendar, R.id.tv_right})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.img_calendar:
                showDatePickerDialog();
                break;
            case R.id.tv_right:
                Intent intent = new Intent(RechargeRecordActivity.this, RechargeActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 日期选择 年月
     */
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        mYear = yy;
        int mm = calendar.get(Calendar.MONTH);
        mMonth = mm + 1;
        int dd = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dlg = new DatePickerDialog(new ContextThemeWrapper(this,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar), null, yy, mm, dd) {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                LinearLayout mSpinners = (LinearLayout) findViewById(getContext().getResources().getIdentifier("android:id/pickers", null, null));
                if (mSpinners != null) {
                    NumberPicker mMonthSpinner = (NumberPicker) findViewById(getContext().getResources().getIdentifier("android:id/month", null, null));
                    NumberPicker mYearSpinner = (NumberPicker) findViewById(getContext().getResources().getIdentifier("android:id/year", null, null));
                    mSpinners.removeAllViews();
                    if (mMonthSpinner != null) {
                        mSpinners.addView(mMonthSpinner);
                    }
                    if (mYearSpinner != null) {
                        mSpinners.addView(mYearSpinner);
                    }
                }
                View dayPickerView = findViewById(getContext().getResources().getIdentifier("android:id/day", null, null));
                if (dayPickerView != null) {
                    dayPickerView.setVisibility(View.GONE);
                }
            }


            @Override
            public void onDateChanged(DatePicker view, int year, int month, int day) {
                super.onDateChanged(view, year, month, day);
                setTitle(year + "年" + (month + 1) + "月");
                mYear = year;
                mMonth = month + 1;

            }
        };
        dlg.setTitle(yy + "年" + (mm + 1) + "月");
        //手动设置按钮
        dlg.setButton(DialogInterface.BUTTON_POSITIVE, "完成", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                searchYYMM = mYear + "-" + mMonth;
                strDisMonth = mYear + "年" + mMonth + "月";
                recycler.post(new Runnable() {
                    @Override
                    public void run() {
                        recycler.showSwipeRefresh();
                        page = 1;
                        getData(true);
                    }
                });
            }
        });
        //取消按钮，如果不需要直接不设置即可
        dlg.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dlg.show();

    }

    class MyRecordAdapter extends RecyclerAdapter<RechargeRecordDataBean.ListBean> {
        private Context context;
        /**
         * 实现单选，保存当前选中的position
         */
        private int mSelectedPos = 0;

        public MyRecordAdapter(Context context) {
            super(context);
            this.context = context;
        }

        @Override
        public BaseViewHolder<RechargeRecordDataBean.ListBean> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
            return new MyRecordHolder(parent);
        }


        class MyRecordHolder extends BaseViewHolder<RechargeRecordDataBean.ListBean> {

            private TextView tv_recharge_num;
            private TextView tv_recharge_value;
            private TextView tv_recharge_date;

            public MyRecordHolder(ViewGroup parent) {
                super(parent, R.layout.recharge_record_item);
            }

            @Override
            public void onInitializeView() {
                super.onInitializeView();
                tv_recharge_num = findViewById(R.id.tv_recharge_num);
                tv_recharge_value = findViewById(R.id.tv_recharge_value);
                tv_recharge_date = findViewById(R.id.tv_recharge_date);
            }

            @Override
            public void setData(final RechargeRecordDataBean.ListBean data) {
                super.setData(data);
                tv_recharge_num.setText("￥" + data.getPay_price());
                tv_recharge_date.setText(data.getPay_date());
                String recharge_num = String.format(getResources().getString(R.string.recharge_token),
                        data.getFill_value());
                tv_recharge_value.setText(recharge_num);

            }

            @Override
            public void onItemViewClick(RechargeRecordDataBean.ListBean data) {
                super.onItemViewClick(data);

            }


        }


    }

    class MyRecordData {
        private String name;

        public MyRecordData(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
