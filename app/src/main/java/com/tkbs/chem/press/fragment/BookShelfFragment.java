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
import com.tkbs.chem.press.util.Config;
import com.tkbs.chem.press.util.MessageEvent;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by Administrator on 2018/10/12.
 */
public class BookShelfFragment extends BaseFragment {

    private ScrollIndicatorView bookSelfIndicator;
    private ViewPager bookShelfViewPager;
    private IndicatorViewPager indicatorViewPager;
    private int user_type;

    @Override
    protected View getPreviewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.layout_preview, container, false);
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        EventBus.getDefault().register(this);
        user_type = preference.getInt(Config.MEMBER_TYPE, 3);
        setContentView(R.layout.fragment_book_shelf);
        bookSelfIndicator = (ScrollIndicatorView) findViewById(R.id.book_shelf_indicator);
        bookShelfViewPager = (ViewPager) findViewById(R.id.book_shelf_viewPager);
        if (user_type == 2) {
            bookSelfIndicator.setVisibility(View.GONE);
        } else {
            bookSelfIndicator.setVisibility(View.VISIBLE);
        }
        float unSelectSize = 12;
        float selectSize = unSelectSize * 1.3f;
        int selectColor = getResources().getColor(R.color.hg_app_main_color);
        int unSelectColor = getResources().getColor(R.color.text_main_3);
        bookSelfIndicator.setScrollBar(new ColorBar(getApplicationContext(), getResources().getColor(R.color.hg_app_main_color), 3));
        bookSelfIndicator.setOnTransitionListener(new OnTransitionTextListener().setColor(selectColor, unSelectColor).setSize(selectSize, unSelectSize));
        bookShelfViewPager.setOffscreenPageLimit(4);
        indicatorViewPager = new IndicatorViewPager(bookSelfIndicator, bookShelfViewPager);
        indicatorViewPager.setAdapter(new MyBookShelfAdapter(getChildFragmentManager()));
        indicatorViewPager.setOnIndicatorPageChangeListener(new IndicatorViewPager.OnIndicatorPageChangeListener() {
            @Override
            public void onIndicatorPageChange(int preItem, int currentItem) {
                int i = currentItem;
                EventBus.getDefault().post(new MessageEvent("RefreshBookShelf"));
            }
        });


    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void RefreshUi(MessageEvent messageEvent) {
        if ("Refresh".endsWith(messageEvent.getMessage())) {
            user_type = preference.getInt(Config.MEMBER_TYPE, 3);
            indicatorViewPager.setAdapter(new MyBookShelfAdapter(getChildFragmentManager()));
        }
    }

    private class MyBookShelfAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {

        public MyBookShelfAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            //  根据身份 教师 4 业务员 2
            if (2 == user_type) {
                return 1;
            } else {
                return 4;
            }
        }

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext())
                        .inflate(R.layout.tab_top, container, false);
            }
            TextView textView = (TextView) convertView;
            if (2 == user_type) {
                if (position == 0) {
                    //textView.setText(R.string.bf_my_book);
                    textView.setText(R.string.bf_collect_book);
                } else if (position == 1) {
                    textView.setText(R.string.bf_collect_book);
                }
            } else {
                if (position == 0) {
                    textView.setText(R.string.bf_free_book);
                } else if (position == 1) {
                    textView.setText(R.string.bf_give_book);
                } else if (position == 2) {
                    textView.setText(R.string.bf_buy_book);
                } else if (position == 3) {
                    textView.setText(R.string.bf_collect_book);
                }

            }
            return convertView;
        }

        @Override
        public Fragment getFragmentForPage(int position) {
            BookShelfItemFragment bookShelfItemFragment = new BookShelfItemFragment();
            Bundle bundle = new Bundle();
            bundle.putString("111", "这是第==" + position);
            if (user_type == 2) {
                bundle.putInt("Type", position + 4);
            } else {
                bundle.putInt("Type", position);
            }
            bookShelfItemFragment.setArguments(bundle);
            return bookShelfItemFragment;

        }
    }
}
