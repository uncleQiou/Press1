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
import com.tkbs.chem.press.fragment.PayRecordItemFragment;
import com.tkbs.chem.press.fragment.TextFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class PayRecordActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.pay_record_indicator)
    ScrollIndicatorView payRecordIndicator;
    @BindView(R.id.pay_record_viewPager)
    ViewPager payRecordViewPager;
    private IndicatorViewPager indicatorViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay_record;
    }

    @Override
    protected void initdata() {
        float unSelectSize = 12;
        float selectSize = unSelectSize * 1.3f;
        int selectColor = getResources().getColor(R.color.hg_app_main_color);
        int unSelectColor = getResources().getColor(R.color.text_main_3);
        payRecordIndicator.setScrollBar(new ColorBar(getApplicationContext(), getResources().getColor(R.color.hg_app_main_color), 3));
        payRecordIndicator.setOnTransitionListener(new OnTransitionTextListener().setColor(selectColor, unSelectColor).setSize(selectSize, unSelectSize));
        payRecordViewPager.setOffscreenPageLimit(4);
        indicatorViewPager = new IndicatorViewPager(payRecordIndicator, payRecordViewPager);
        indicatorViewPager.setAdapter(new PayRecordAdapter(getSupportFragmentManager()));
    }

    @Override
    protected void initTitle() {
        title.setText(R.string.pay_record);
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

    private class PayRecordAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {

        public PayRecordAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext())
                        .inflate(R.layout.tab_top, container, false);
            }
            TextView textView = (TextView) convertView;
            if (position == 0) {
                textView.setText(R.string.pay_record_all);
            } else if (position == 1) {
                textView.setText(R.string.pay_done);
            } else {
                textView.setText(R.string.pay_undone);
            }
            return convertView;
        }

        @Override
        public Fragment getFragmentForPage(int position) {
            PayRecordItemFragment textFragment = new PayRecordItemFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("orderState",position);
            textFragment.setArguments(bundle);
            return textFragment;

        }
    }
}
