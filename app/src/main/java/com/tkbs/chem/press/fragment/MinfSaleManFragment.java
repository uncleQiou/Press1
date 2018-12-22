package com.tkbs.chem.press.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;
import com.tkbs.chem.press.R;
import com.tkbs.chem.press.activity.ChartDemoActivity;
import com.tkbs.chem.press.activity.NewsActivity;
import com.tkbs.chem.press.activity.SalesmanPersonalCenterActivity;
import com.tkbs.chem.press.activity.SettingActivity;
import com.tkbs.chem.press.base.BaseFragment;
import com.tkbs.chem.press.util.Config;
import com.tkbs.chem.press.util.MessageEvent;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 作者    qyl
 * 时间    2018/11/30 13:44
 * 文件    Press
 * 描述
 */
public class MinfSaleManFragment extends BaseFragment implements View.OnClickListener {

    private ImageView img_message;
    private ImageView img_set;
    private TextView tv_my_name;
    private TextView tv_phone;
    private ScrollIndicatorView indicator;
    private LinearLayout ll_edit;
    private ViewPager viewPager;
    private String[] indicators;
    private IndicatorViewPager indicatorViewPager;
    protected boolean isRefresh = false;

    @Override
    protected View getPreviewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.layout_preview, container, false);
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_mine_saleman);
        img_message = (ImageView) findViewById(R.id.img_message);
        img_set = (ImageView) findViewById(R.id.img_set);
        img_message.setOnClickListener(this);
        img_set.setOnClickListener(this);
        tv_my_name = (TextView) findViewById(R.id.tv_my_name);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        indicator = (ScrollIndicatorView) findViewById(R.id.indicator);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        ll_edit = (LinearLayout) findViewById(R.id.ll_edit);
        ll_edit.setOnClickListener(this);
        EventBus.getDefault().register(this);
        indicators = new String[]{getResources().getString(R.string.apply_samplebook_count),
                getResources().getString(R.string.give_book_count)};

        int selectColor = getResources().getColor(R.color.hg_app_main_color);
        int unSelectColor = getResources().getColor(R.color.text_main_3);
        indicator.setScrollBar(new ColorBar(getApplicationContext(), getResources().getColor(R.color.hg_app_main_color), 2));
        indicator.setOnTransitionListener(
                new OnTransitionTextListener().setColor(selectColor, unSelectColor));
        viewPager.setOffscreenPageLimit(indicators.length);
        indicatorViewPager = new IndicatorViewPager(indicator, viewPager);
        indicatorViewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
        setUserData();
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void RefreshUi(MessageEvent messageEvent) {
        if ("Refresh".endsWith(messageEvent.getMessage())) {
            isRefresh = true;
            setUserData();
        }
    }

    /**
     * 设置用户数据
     */
    public void setUserData() {
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
            case R.id.img_message:
                // 消息
                getActivity().startActivity(new Intent(getActivity(), NewsActivity.class));
                break;
            case R.id.img_set:
                // 设置
                getActivity().startActivityForResult(new Intent(getActivity(), SettingActivity.class), Config.ACCOUNT_SWITCHING);
                break;
            case R.id.ll_edit:
                //  个人信息页面 H5 页面
//                toastShow("编辑个人信息");
//                getActivity().startActivity(new Intent(getActivity(), ChartDemoActivity.class));
                getActivity().startActivity(new Intent(getActivity(), SalesmanPersonalCenterActivity.class));
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
                SampleBookStatisticsFragment samplebookstatisticsfragment = new SampleBookStatisticsFragment();
//                TextFragment samplebookstatisticsfragment = new TextFragment();
                Bundle bundle = new Bundle();
                bundle.putString("111", "这是第==" + position);
//                bundle.putString("guid", guid);
                samplebookstatisticsfragment.setArguments(bundle);
                return samplebookstatisticsfragment;
            } else if (1 == position) {
                GiveBookStatisticsFragment givebookstatisticsfragment = new GiveBookStatisticsFragment();
//                TextFragment givebookstatisticsfragment = new TextFragment();
                Bundle bundle = new Bundle();
                bundle.putString("111", "这是第==" + position);
//                bundle.putString("guid", guid);
                givebookstatisticsfragment.setArguments(bundle);
                return givebookstatisticsfragment;
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
