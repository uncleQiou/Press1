package com.tkbs.chem.press.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.orhanobut.logger.Logger;
import com.tkbs.chem.press.R;
import com.tkbs.chem.press.base.BaseActivity;

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
    private Handler mHandler;
    private int page = 1;
    private MyRecordAdapter myAdapter;

    private int mYear;
    private int mMonth;
    private int mDay;

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
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isRefresh) {
                    page = 1;
                    myAdapter.clear();
                    myAdapter.addAll(getTestData());
                    recycler.dismissSwipeRefresh();
                    recycler.getRecyclerView().scrollToPosition(0);
                    recycler.showNoMore();
                } else {
                    myAdapter.addAll(getTestData());
                    if (page >= 1) {
                        recycler.showNoMore();
                    }
                }
            }
        }, 1000);
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

    @OnClick({R.id.back, R.id.img_calendar})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.img_calendar:
                showDatePickerDialog();
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
        int mm = calendar.get(Calendar.MONTH);
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
            }
        };
        dlg.setTitle(yy + "年" + (mm + 1) + "月");
        //手动设置按钮
        dlg.setButton(DialogInterface.BUTTON_POSITIVE, "完成", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                recycler.post(new Runnable() {
                    @Override
                    public void run() {
                        recycler.showSwipeRefresh();
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

    class MyRecordAdapter extends RecyclerAdapter<MyRecordData> {
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
        public BaseViewHolder<MyRecordData> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
            return new MyRecordHolder(parent);
        }


        class MyRecordHolder extends BaseViewHolder<MyRecordData> {

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
            public void setData(final MyRecordData data) {
                super.setData(data);
                tv_recharge_num.setText(data.getName());
                tv_recharge_date.setText("2018-10-15");
//                tv_recharge_value

            }

            @Override
            public void onItemViewClick(MyRecordData data) {
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
