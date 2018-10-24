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

/**
 * Created by Administrator on 2018/10/12.
 */
public class BookShelfFragment extends BaseFragment {

    private ScrollIndicatorView bookSelfIndicator;
    private ViewPager bookShelfViewPager;
    private IndicatorViewPager indicatorViewPager;

    @Override
    protected View getPreviewLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.layout_preview, container, false);
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_book_shelf);
        bookSelfIndicator = (ScrollIndicatorView) findViewById(R.id.book_shelf_indicator);
        bookShelfViewPager = (ViewPager) findViewById(R.id.book_shelf_viewPager);
        float unSelectSize = 12;
        float selectSize = unSelectSize * 1.3f;
        int selectColor = getResources().getColor(R.color.hg_app_main_color);
        int unSelectColor = getResources().getColor(R.color.text_main_3);
        bookSelfIndicator.setScrollBar(new ColorBar(getApplicationContext(), getResources().getColor(R.color.hg_app_main_color), 3));
        bookSelfIndicator.setOnTransitionListener(new OnTransitionTextListener().setColor(selectColor, unSelectColor).setSize(selectSize, unSelectSize));
        bookShelfViewPager.setOffscreenPageLimit(4);
        indicatorViewPager = new IndicatorViewPager(bookSelfIndicator, bookShelfViewPager);
        indicatorViewPager.setAdapter(new MyBookShelfAdapter(getChildFragmentManager()));


    }

    private class MyBookShelfAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {

        public MyBookShelfAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            // TODO 根据身份 教师 4 业务员 2
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
                textView.setText(R.string.bf_free_book);
            } else if (position == 1) {
                textView.setText(R.string.bf_give_book);
            } else if (position == 2) {
                textView.setText(R.string.bf_buy_book);
            } else if (position == 3) {
                textView.setText(R.string.bf_collect_book);
            }
            return convertView;
        }

        @Override
        public Fragment getFragmentForPage(int position) {
            BookShelfItemFragment bookShelfItemFragment = new BookShelfItemFragment();
            Bundle bundle = new Bundle();
            bundle.putString("111", "这是第==" + position);
            bundle.putInt("Type", position);
            bookShelfItemFragment.setArguments(bundle);
            return bookShelfItemFragment;
//            if (position == 0) {
//                TextFragment textFragment = new TextFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString("111", "这是第==" + position);
//                bundle.putInt("Type", position);
//                textFragment.setArguments(bundle);
//                return textFragment;
//            } else if (position == 1) {
//                TextFragment textFragment = new TextFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString("111", "这是第==" + position);
//                bundle.putInt("Type", position);
//                textFragment.setArguments(bundle);
//                return textFragment;
//            } else {
//                TextFragment textFragment = new TextFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString("111", "这是第==" + position);
//                bundle.putInt("Type", position);
//                textFragment.setArguments(bundle);
//                return textFragment;
//            }

        }
    }
}
