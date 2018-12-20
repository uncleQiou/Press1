package com.tkbs.chem.press.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.tkbs.chem.press.R;
import com.tkbs.chem.press.base.BaseFragment;
import com.tkbs.chem.press.bean.BarChartBean;
import com.tkbs.chem.press.util.LocalJsonAnalyzeUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 作者    qyl
 * 时间    2018/12/16 15:09
 * 文件    Press
 * 描述
 */
public class SampleBookStatisticsFragment extends BaseFragment implements View.OnClickListener {

    private List<BarChartBean.StFinDateBean.VtDateValueBean> dateValueList;
    private LinearLayout ll_smbook_time;
    private LinearLayout ll_smbook_school;
    private LinearLayout ll_smbook_teacher;
    private BarChart barChart_time;
    private BarChart barChart_school;
    private BarChart barChart_teacher;
    private RadioButton rbtn_tab_yy;
    private RadioButton rbtn_tab_mm;
    private RadioButton rbtn_tab_dd;
    //左侧Y轴
    private YAxis leftAxis;
    //右侧Y轴
    private YAxis rightAxis;
    //X轴
    private XAxis xAxis;
    //图例
    private Legend legend;
    //限制线
    private LimitLine limitLine;

    private TextView tv_tj_chool;

    /**
     * 时间
     */
    private RadioButton tj_time_book;
    private RadioButton tj_time_people;
    /**
     * 学校
     */
    private RadioButton tj_school_book;
    private RadioButton tj_school_people;
    /**
     * 教师
     */
    private RadioButton tj_teacher_book;
    private RadioButton tj_teacher_limit;

    @Override
    protected View getPreviewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.layout_preview, container, false);
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.samplebook_statistics_fragment);
        initView();
        // 设置数据
        BarChartBean barChartBean = LocalJsonAnalyzeUtil.JsonToObject(getActivity(),
                "bar_chart.json", BarChartBean.class);
        dateValueList = barChartBean.getStFinDate().getVtDateValue();
        //将集合 逆序排列，转换成需要的顺序
        Collections.reverse(dateValueList);
        showBarChart(dateValueList, "", getResources().getColor(R.color.chart_line_value));
    }

    /***
     * 初始化页面
     */
    private void initView() {
        ll_smbook_time = (LinearLayout) findViewById(R.id.ll_smbook_time);
        ll_smbook_school = (LinearLayout) findViewById(R.id.ll_smbook_school);
        ll_smbook_teacher = (LinearLayout) findViewById(R.id.ll_smbook_teacher);
        barChart_time = (BarChart) findViewById(R.id.barChart_time);
        initBarChart(barChart_time);
        barChart_school = (BarChart) findViewById(R.id.barChart_school);
        initBarChart(barChart_school);
        barChart_teacher = (BarChart) findViewById(R.id.barChart_teacher);
        initBarChart(barChart_teacher);
        rbtn_tab_yy = (RadioButton) findViewById(R.id.rbtn_tab_yy);
        rbtn_tab_yy.setOnClickListener(this);
        rbtn_tab_mm = (RadioButton) findViewById(R.id.rbtn_tab_mm);
        rbtn_tab_mm.setOnClickListener(this);
        rbtn_tab_dd = (RadioButton) findViewById(R.id.rbtn_tab_dd);
        rbtn_tab_dd.setOnClickListener(this);
        // 学校选择
        tv_tj_chool = (TextView) findViewById(R.id.tv_tj_chool);
        tv_tj_chool.setOnClickListener(this);
        // 时间统计
        tj_time_book = (RadioButton) findViewById(R.id.tj_time_book);
        tj_time_book.setOnClickListener(this);
        tj_time_people = (RadioButton) findViewById(R.id.tj_time_people);
        tj_time_people.setOnClickListener(this);
        // 学校统计
        tj_school_book = (RadioButton) findViewById(R.id.tj_school_book);
        tj_school_book.setOnClickListener(this);
        tj_school_people = (RadioButton) findViewById(R.id.tj_school_people);
        tj_school_people.setOnClickListener(this);
        // 教师统计
        tj_teacher_book = (RadioButton) findViewById(R.id.tj_teacher_book);
        tj_teacher_book.setOnClickListener(this);
        tj_teacher_limit = (RadioButton) findViewById(R.id.tj_teacher_limit);
        tj_teacher_limit.setOnClickListener(this);


    }

    public void showBarChart(List<BarChartBean.StFinDateBean.VtDateValueBean> dateValueList, String name, int color) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < dateValueList.size(); i++) {
            /**
             * 此处还可传入Drawable对象 BarEntry(float x, float y, Drawable icon)
             * 即可设置柱状图顶部的 icon展示
             */
            BarEntry barEntry = new BarEntry(i, (float) dateValueList.get(i).getFValue());
            entries.add(barEntry);
        }
        // 每一个BarDataSet代表一类柱状图
        BarDataSet barDataSet = new BarDataSet(entries, name);
        initBarDataSet(barDataSet, color);


        BarData data = new BarData(barDataSet);
        //单条柱状图宽度
        data.setBarWidth(0.5f);
        barChart_time.setData(data);
        barChart_school.setData(data);
        barChart_teacher.setData(data);
    }

    /**
     * 柱状图始化设置 一个BarDataSet 代表一列柱状图
     *
     * @param barDataSet 柱状图
     * @param color      柱状图颜色
     */
    private void initBarDataSet(BarDataSet barDataSet, int color) {
        barDataSet.setColor(color);
        barDataSet.setFormLineWidth(1f);
        barDataSet.setFormSize(15.f);
        //不显示柱状图顶部值
        barDataSet.setDrawValues(true);
        barDataSet.setValueTextSize(10);
    }

    /**
     * 初始化 柱状图
     */
    private void initBarChart(BarChart barChart) {
        /***图表设置***/
        //背景颜色
        barChart.setBackgroundColor(getResources().getColor(R.color.app_bag));
        //不显示图表网格
        barChart.setDrawGridBackground(false);
        //背景阴影
        barChart.setDrawBarShadow(false);
        barChart.setHighlightFullBarEnabled(false);
        //显示边框
        barChart.setDrawBorders(false);
        //设置动画效果
        barChart.animateY(1000, Easing.EasingOption.Linear);
        barChart.animateX(1000, Easing.EasingOption.Linear);
        // 不显示 右下角描述
        Description description = new Description();
        description.setEnabled(false);
        barChart.setDescription(description);
        /***XY轴的设置***/
        //X轴设置显示位置在底部
        xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        //X轴自定义值
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return dateValueList.get((int) value).getSYearMonth();
            }
        });
        leftAxis = barChart.getAxisLeft();
        rightAxis = barChart.getAxisRight();
        // 不显示 X轴Y轴线条
        xAxis.setDrawAxisLine(false);
        leftAxis.setDrawAxisLine(false);
        rightAxis.setDrawAxisLine(false);
        leftAxis.setEnabled(false);
        barChart.setDrawGridBackground(false);
        //不显示X轴网格线
        xAxis.setDrawGridLines(false);
        //右侧Y轴网格线设置为虚线
        rightAxis.enableGridDashedLine(10f, 10f, 0f);
        rightAxis.setDrawGridLines(false);

        rightAxis.setEnabled(false);
        //保证Y轴从0开始，不然会上移一点
        leftAxis.setAxisMinimum(0f);
        rightAxis.setAxisMinimum(0f);

        /***折线图例 标签 设置***/
        legend = barChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(11f);
        //显示位置
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //是否绘制在图表里面
        legend.setDrawInside(false);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rbtn_tab_yy:
                rbtn_tab_yy.setTextColor(getResources().getColor(R.color.white));
                rbtn_tab_mm.setTextColor(getResources().getColor(R.color.text_main_3));
                rbtn_tab_dd.setTextColor(getResources().getColor(R.color.text_main_3));
                toastShow(rbtn_tab_yy.getText().toString());
                break;

            case R.id.rbtn_tab_mm:
                rbtn_tab_mm.setTextColor(getResources().getColor(R.color.white));
                rbtn_tab_yy.setTextColor(getResources().getColor(R.color.text_main_3));
                rbtn_tab_dd.setTextColor(getResources().getColor(R.color.text_main_3));
                toastShow(rbtn_tab_mm.getText().toString());
                break;

            case R.id.rbtn_tab_dd:
                rbtn_tab_dd.setTextColor(getResources().getColor(R.color.white));
                rbtn_tab_mm.setTextColor(getResources().getColor(R.color.text_main_3));
                rbtn_tab_yy.setTextColor(getResources().getColor(R.color.text_main_3));
                toastShow(rbtn_tab_dd.getText().toString());
                break;
            case R.id.tv_tj_chool:
                showDialog();
                break;
            // 时间
            case R.id.tj_time_book:
                tj_time_book.setTextColor(getResources().getColor(R.color.white));
                tj_time_people.setTextColor(getResources().getColor(R.color.text_main_3));
                toastShow(tj_time_book.getText().toString());
                break;
            case R.id.tj_time_people:
                tj_time_people.setTextColor(getResources().getColor(R.color.white));
                tj_time_book.setTextColor(getResources().getColor(R.color.text_main_3));
                toastShow(tj_time_people.getText().toString());
                break;
            // 学校
            case R.id.tj_school_book:
                tj_school_book.setTextColor(getResources().getColor(R.color.white));
                tj_school_people.setTextColor(getResources().getColor(R.color.text_main_3));
                toastShow(tj_school_book.getText().toString());
                break;
            case R.id.tj_school_people:
                tj_school_people.setTextColor(getResources().getColor(R.color.white));
                tj_school_book.setTextColor(getResources().getColor(R.color.text_main_3));
                toastShow(tj_school_people.getText().toString());
                break;
            // 教师
            case R.id.tj_teacher_book:
                tj_teacher_book.setTextColor(getResources().getColor(R.color.white));
                tj_teacher_limit.setTextColor(getResources().getColor(R.color.text_main_3));
                toastShow(tj_teacher_book.getText().toString());
                break;
            case R.id.tj_teacher_limit:
                tj_teacher_limit.setTextColor(getResources().getColor(R.color.white));
                tj_teacher_book.setTextColor(getResources().getColor(R.color.text_main_3));
                toastShow(tj_teacher_limit.getText().toString());
                break;
            default:
                break;
        }
    }

    /**
     * 显示学校选择对话框
     */
    private void showDialog() {
        DialogItemAdapter adapter = new DialogItemAdapter(getActivity(), setDialogData());

        AlertDialog alertDialog = new AlertDialog
                .Builder(getActivity())
                .setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv_tj_chool.setText(listDialog.get(which));
                        dialog.dismiss();
                    }
                }).create();
        alertDialog.show();

    }

    private List<String> listDialog;

    private List<String> setDialogData() {
        listDialog = new ArrayList<String>();

        listDialog.add("北京大学");
        listDialog.add("清华大学");
        listDialog.add("大连交通大学");
        listDialog.add("郑州大学");
        listDialog.add("兰州大学");
        listDialog.add("呼伦贝尔学院");

        return listDialog;
    }

    /**
     * dilog adapter
     */
    public class DialogItemAdapter extends BaseAdapter {
        //这里可以传递个对象，用来控制不同的item的效果
        //比如每个item的背景资源，选中样式等
        public List<String> list;
        LayoutInflater inflater;

        public DialogItemAdapter(Context context, List<String> list) {
            this.list = list;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public String getItem(int i) {
            if (i == getCount() || list == null) {
                return null;
            }
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.dialog_item_tj, null);
                holder.tv_school = (TextView) convertView.findViewById(R.id.tv_school);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_school.setText(getItem(position));
            return convertView;
        }

        public class ViewHolder {
            public TextView tv_school;
        }
    }

}
