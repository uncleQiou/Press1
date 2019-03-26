package com.tkbs.chem.press.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;
import com.tkbs.chem.press.R;
import com.tkbs.chem.press.base.BaseActivity;
import com.tkbs.chem.press.bean.BookCityResCatagory;
import com.tkbs.chem.press.bean.HttpResponse;
import com.tkbs.chem.press.fragment.SecondaryClassificationFragment;
import com.tkbs.chem.press.net.ApiCallback;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class SecondaryClassificationActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.img_serache)
    ImageView imgSerache;
    @BindView(R.id.img_classification)
    ImageView imgClassification;
    @BindView(R.id.view_indicator)
    ScrollIndicatorView viewIndicator;
    @BindView(R.id.view_viewPager)
    ViewPager viewViewPager;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private IndicatorViewPager indicatorViewPager;
    private String[] indicators = {"本科", "高职", "中职", "中职", "中小学", "教材综合", "大学", "研究生", "博士", "博士后"};
    private String guid = "";
    private String titleStr = "";
    private ArrayList<BookCityResCatagory> indicatorData;
    private  int currentItemIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_secondary_classification;
    }

    @Override
    protected void initdata() {
        int selectColor = getResources().getColor(R.color.hg_app_main_color);
        int unSelectColor = getResources().getColor(R.color.text_main_3);
        // 获取indicator数据
        guid = getIntent().getStringExtra("guid");
        titleStr = getIntent().getStringExtra("title");
        currentItemIndex = getIntent().getIntExtra("index",0);
        getIndicatorData();
        viewIndicator.setScrollBar(new ColorBar(getApplicationContext(), getResources().getColor(R.color.hg_app_main_color), 2));
        viewIndicator.setOnTransitionListener(
                new OnTransitionTextListener().setColor(selectColor, unSelectColor));
        viewViewPager.setOffscreenPageLimit(indicators.length);
        indicatorViewPager = new IndicatorViewPager(viewIndicator, viewViewPager);
//        indicatorViewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
    }

    private void getIndicatorData() {
        showProgressDialog();
        addSubscription(apiStores.SecondClassificIndicator(guid), new ApiCallback<HttpResponse<ArrayList<BookCityResCatagory>>>() {
            @Override
            public void onSuccess(HttpResponse<ArrayList<BookCityResCatagory>> model) {
                if (model.isStatus()) {
                    indicatorData = model.getData();
                    indicatorViewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
                    indicatorViewPager.setCurrentItem(currentItemIndex,true);

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

    @Override
    protected void initTitle() {
        tvTitle.setText(titleStr);
    }

    @OnClick({R.id.back, R.id.img_serache, R.id.img_classification})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.img_classification:
                startActivityForResult(new Intent(SecondaryClassificationActivity.this, SearchClassifyActivity.class), 0);
                break;
            case R.id.img_serache:
                startActivity(new Intent(SecondaryClassificationActivity.this, SearchActivity.class));
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
                    Intent intent = new Intent(SecondaryClassificationActivity.this, SearchActivity.class);
                    intent.putExtra("Classy", result);
                    startActivity(intent);
//                    finish();
                    break;
                default:
                    break;
            }
        }
    }

    private class MyAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {

        public MyAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return indicatorData.size();
        }

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext())
                        .inflate(R.layout.tab_top, container, false);
            }
            TextView textView = (TextView) convertView;
            textView.setText(indicatorData.get(position).getTitle());
            return convertView;
        }

        @Override
        public Fragment getFragmentForPage(int position) {
            SecondaryClassificationFragment secondaryClassificationFragment = new SecondaryClassificationFragment();
            Bundle bundle = new Bundle();
            bundle.putString("111", "这是第==" + position);
            bundle.putString("Type", indicatorData.get(position).getGuid());
            secondaryClassificationFragment.setArguments(bundle);
            return secondaryClassificationFragment;

        }
    }
}
