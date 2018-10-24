package com.tkbs.chem.press.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;
import com.tkbs.chem.press.R;
import com.tkbs.chem.press.base.BaseActivity;
import com.tkbs.chem.press.fragment.BookShelfItemFragment;
import com.tkbs.chem.press.fragment.SecondaryClassificationFragment;
import com.tkbs.chem.press.fragment.TextFragment;

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
        viewIndicator.setScrollBar(new ColorBar(getApplicationContext(), getResources().getColor(R.color.hg_app_main_color), 2));
        viewIndicator.setOnTransitionListener(
                new OnTransitionTextListener().setColor(selectColor, unSelectColor));
        viewViewPager.setOffscreenPageLimit(indicators.length);
        indicatorViewPager = new IndicatorViewPager(viewIndicator, viewViewPager);
        indicatorViewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
    }

    @Override
    protected void initTitle() {
        tvTitle.setText("SecondaryClassificationActivity");
    }

    @OnClick({R.id.back})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }

    private class MyAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {

        public MyAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return indicators.length;
        }

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext())
                        .inflate(R.layout.tab_top, container, false);
            }
            TextView textView = (TextView) convertView;
            textView.setText(indicators[position]);
            return convertView;
        }

        @Override
        public Fragment getFragmentForPage(int position) {
            SecondaryClassificationFragment secondaryClassificationFragment = new SecondaryClassificationFragment();
            Bundle bundle = new Bundle();
            bundle.putString("111", "这是第==" + position);
            bundle.putInt("Type", position);
            secondaryClassificationFragment.setArguments(bundle);
            return secondaryClassificationFragment;

        }
    }
}
