package com.tkbs.chem.press.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;
import com.tkbs.chem.press.R;
import com.tkbs.chem.press.base.BaseFragment;
import com.tkbs.chem.press.util.MessageEvent;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;


public class SalesmanManageFragment extends BaseFragment {
    private ScrollIndicatorView manage_indicator;
    private TextView one_key_approve;
    private ViewPager manage_viewPager;
    private IndicatorViewPager indicatorViewPager;

    @Override
    protected View getPreviewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.layout_preview, container, false);
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.fragment_salesman_manage);
        manage_indicator = (ScrollIndicatorView) findViewById(R.id.manage_indicator);
        manage_viewPager = (ViewPager) findViewById(R.id.manage_viewPager);
        int selectColor = getResources().getColor(R.color.hg_app_main_color);
        int unSelectColor = getResources().getColor(R.color.text_main_3);
        manage_indicator.setScrollBar(new ColorBar(getApplicationContext(), getResources().getColor(R.color.hg_app_main_color), 2));
        manage_indicator.setOnTransitionListener(new OnTransitionTextListener().setColor(selectColor, unSelectColor));
        manage_viewPager.setOffscreenPageLimit(4);
        indicatorViewPager = new IndicatorViewPager(manage_indicator, manage_viewPager);
        indicatorViewPager.setAdapter(new MyMangerAdapter(getChildFragmentManager()));
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void RefreshUi(MessageEvent messageEvent) {
        if ("Refresh".endsWith(messageEvent.getMessage())) {
            indicatorViewPager.setAdapter(new MyMangerAdapter(getChildFragmentManager()));
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            // 不在最前端显示 相当于调用了onPause();

        } else {
            // 在最前端显示 相当于调用了onResume();
            indicatorViewPager.setCurrentItem(0,true);
//            indicatorViewPager.setAdapter(new MyMangerAdapter(getChildFragmentManager()));

        }
    }

    private class MyMangerAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {

        public MyMangerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext())
                        .inflate(R.layout.tab_top, container, false);
            }
            TextView textView = (TextView) convertView;
            if (position == 0) {
                textView.setText(R.string.message_manage);
            } else if (position == 1) {
                textView.setText(R.string.sample_book_manage);
            } else if (position == 2) {
                textView.setText(R.string.user_manage);
            } else if (position == 3) {
                textView.setText(R.string.opinion_manage);
            }
            return convertView;
        }

        @Override
        public Fragment getFragmentForPage(int position) {

            if (0 == position) {

                DiscoverFragment discoverFragment = new DiscoverFragment();
                Bundle bundle = new Bundle();
                bundle.putString("111", "这是第==" + position);
                bundle.putInt("Type", position);
                discoverFragment.setArguments(bundle);
                return discoverFragment;
            } else if (1 == position) {

                SampleBookFragment sampleBookFragment = new SampleBookFragment();
                Bundle bundle = new Bundle();
                bundle.putString("111", "这是第==" + position);
                bundle.putInt("Type", position);
                sampleBookFragment.setArguments(bundle);
                return sampleBookFragment;
            } else if (2 == position) {

                UserManageFragment userManageFragment = new UserManageFragment();
                Bundle bundle = new Bundle();
                bundle.putString("111", "这是第==" + position);
                bundle.putInt("Type", position);
                userManageFragment.setArguments(bundle);
                return userManageFragment;
            } else if (3 == position) {

                OpinionManageFragment opinionManageFragment = new OpinionManageFragment();
                Bundle bundle = new Bundle();
                bundle.putString("111", "这是第==" + position);
                bundle.putInt("Type", position);
                opinionManageFragment.setArguments(bundle);
                return opinionManageFragment;
            } else {

                TextFragment bookShelfItemFragment = new TextFragment();
                Bundle bundle = new Bundle();
                bundle.putString("111", "这是第==" + position);
                bundle.putInt("Type", position);
                bookShelfItemFragment.setArguments(bundle);
                return bookShelfItemFragment;
            }

        }
    }
}
