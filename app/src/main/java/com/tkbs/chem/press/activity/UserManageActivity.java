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
import com.tkbs.chem.press.fragment.FragmentBookPurchaseList;
import com.tkbs.chem.press.fragment.SampleBookListFragment;
import com.tkbs.chem.press.fragment.SecondaryClassificationFragment;
import com.tkbs.chem.press.fragment.TeacherInforFragment;
import com.tkbs.chem.press.fragment.TextFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class UserManageActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_teacher_name)
    TextView tvTeacherName;
    @BindView(R.id.tv_teacher_subject)
    TextView tvTeacherSubject;
    @BindView(R.id.tv_register_time)
    TextView tvRegisterTime;
    @BindView(R.id.indicator)
    ScrollIndicatorView indicator;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private IndicatorViewPager indicatorViewPager;
    private String[] indicators;

    private String guid;
    private String name;
    private long date;
    private int state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_manage;
    }

    @Override
    protected void initdata() {
        guid = getIntent().getStringExtra("guid");
        name = getIntent().getStringExtra("name");
        date = getIntent().getLongExtra("date", 0);
        state = getIntent().getIntExtra("state", 0);
        tvTeacherName.setText(name);
        tvTeacherSubject.setText("状态：" + state);
        indicators = new String[]{getResources().getString(R.string.personal_information),
                getResources().getString(R.string.sample_book_list),
                getResources().getString(R.string.buy_book_list)};
        tvRegisterTime.setText("2018年10月18日18:47:17");
        int selectColor = getResources().getColor(R.color.hg_app_main_color);
        int unSelectColor = getResources().getColor(R.color.text_main_3);
        indicator.setScrollBar(new ColorBar(getApplicationContext(), getResources().getColor(R.color.hg_app_main_color), 2));
        indicator.setOnTransitionListener(
                new OnTransitionTextListener().setColor(selectColor, unSelectColor));
        viewPager.setOffscreenPageLimit(indicators.length);
        indicatorViewPager = new IndicatorViewPager(indicator, viewPager);
        indicatorViewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
    }

    @Override
    protected void initTitle() {

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
            if (0 == position) {
                TeacherInforFragment teacherInforFragment = new TeacherInforFragment();
                Bundle bundle = new Bundle();
                bundle.putString("111", "这是第==" + position);
                bundle.putString("guid", guid);
                teacherInforFragment.setArguments(bundle);
                return teacherInforFragment;
            } else if (1 == position) {
                SampleBookListFragment sampleBookListFragment = new SampleBookListFragment();
                Bundle bundle = new Bundle();
                bundle.putString("111", "这是第==" + position);
                bundle.putInt("Type", position);
                sampleBookListFragment.setArguments(bundle);
                return sampleBookListFragment;
            } else if (2 == position) {
                FragmentBookPurchaseList fragmentBookPurchaseList = new FragmentBookPurchaseList();
                Bundle bundle = new Bundle();
                bundle.putString("111", "这是第==" + position);
                bundle.putInt("Type", position);
                fragmentBookPurchaseList.setArguments(bundle);
                return fragmentBookPurchaseList;
            } else {
                TextFragment textFragment = new TextFragment();
                Bundle bundle = new Bundle();
                bundle.putString("111", "这是第==" + position);
                bundle.putInt("Type", position);
                textFragment.setArguments(bundle);
                return textFragment;
            }


        }
    }
}
